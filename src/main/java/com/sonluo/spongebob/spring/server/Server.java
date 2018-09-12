package com.sonluo.spongebob.spring.server;

import com.sonluo.spongebob.spring.server.impl.DefaultBeanOperator;
import com.sonluo.spongebob.spring.server.impl.DefaultNameConverter;
import com.sonluo.spongebob.spring.server.impl.DefaultServiceMapping;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import javax.annotation.Nullable;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author sunqian
 */
public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final ApplicationContext applicationContext;
    private final ServiceMapping serviceMapping;
    private final NameConverter nameConverter;
    private final BeanOperator beanOperator;

    private ServiceCallExceptionInterceptor serviceCallExceptionInterceptor;

    private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    public Server(ApplicationContext applicationContext,
                  ServiceMapping serviceMapping,
                  NameConverter nameConverter,
                  BeanOperator beanOperator) {
        this.applicationContext = applicationContext;
        this.serviceMapping = serviceMapping;
        this.nameConverter = nameConverter;
        this.beanOperator = beanOperator;
    }

    public Server(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.serviceMapping = new DefaultServiceMapping();
        this.nameConverter = new DefaultNameConverter();
        this.beanOperator = DefaultBeanOperator.INSTANCE;
    }

    @Nullable
    public Object doService(Request request) {
        ServiceCall serviceCall = serviceMapping.getServiceCall(request.getUrl());
        if (serviceCall == null) {
            if (serviceCallExceptionInterceptor != null) {
                return serviceCallExceptionInterceptor.doIntercept(request, new ServiceNotFoundException(), new HashMap<>());
            }
            throw new ServiceNotFoundException();
        }
        return serviceCall.doService(request);
    }

    public void init() {
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(ApiService.class);
        Map<String, ServiceCall> serviceCalls = new HashMap<>();
        Map<String, ServiceCallInterceptorGroup> groupMap = findInterceptorGroups();
        Map<String, ServiceCallInterceptor> interceptorMap = findInterceptors(groupMap.values());
        map.forEach((name, bean) -> {
            ApiServiceMapping apiServiceMapping = bean.getClass().getAnnotation(ApiServiceMapping.class);
            Method[] methods = bean.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (!Modifier.isPublic(method.getModifiers()) || method.getAnnotation(ApiServiceMapping.class) == null) {
                    continue;
                }
                ApiServiceMapping apiMapping = method.getDeclaredAnnotation(ApiServiceMapping.class);
                String url = nameConverter.join(nameConverter.toServiceName(name, apiServiceMapping),
                        nameConverter.toMethodName(method, apiMapping));
                ServiceCallDescriptor serviceCallDescriptor = createServiceCall(bean, method, apiMapping, groupMap, interceptorMap);
                serviceCalls.put(url, serviceCallDescriptor.getServiceCall());
                logger.info("Register service call {}, [{}].", url, serviceCallDescriptor.getInterceptorDescriptor());
            }
        });
        serviceMapping.init(serviceCalls);

        Map<String, ServiceCallExceptionInterceptor> handlerMap = applicationContext.getBeansOfType(ServiceCallExceptionInterceptor.class);
        if (handlerMap.size() > 1) {
            throw new IllegalStateException("Only one ServiceCallExceptionInterceptor exists!");
        }
        if (handlerMap.size() == 1) {
            handlerMap.forEach((name, bean) -> serviceCallExceptionInterceptor = bean);
        }

        logger.info("Spongebob Service startup!");
    }

    private ServiceCallDescriptor createServiceCall(Object bean, Method method, ApiServiceMapping apiServiceMapping,
                                                    Map<String, ServiceCallInterceptorGroup> interceptorGroupMap,
                                                    Map<String, ServiceCallInterceptor> interceptorMap) {
        String groupName = apiServiceMapping.group();
        ServiceCallInterceptorGroup group = interceptorGroupMap.get(groupName);
        if (group == null) {
            throw new IllegalStateException("Cannot find interceptor group \"" + groupName
                    + "\" at " + method);
        }
        List<ServiceCallInterceptor> interceptors = new ArrayList<>(group.getInterceptors());
        if (ArrayUtils.isNotEmpty(apiServiceMapping.include())) {
            String[] includes = apiServiceMapping.include();
            for (int i = 0; i < includes.length; i++) {
                ServiceCallInterceptor interceptor = interceptorMap.get(includes[i]);
                if (interceptor == null) {
                    throw new IllegalStateException("Cannot find interceptor \"" + includes[i]
                            + "\" at " + method);
                }
                interceptors.add(interceptor);
            }
        }
        if (ArrayUtils.isNotEmpty(apiServiceMapping.exclude())) {
            String[] excludes = apiServiceMapping.include();
            for (int i = 0; i < excludes.length; i++) {
                ServiceCallInterceptor interceptor = interceptorMap.get(excludes[i]);
                if (interceptor == null) {
                    throw new IllegalStateException("Cannot find interceptor \"" + excludes[i]
                            + "\" at " + method);
                }
                interceptors.remove(interceptor);
            }
        }

        if (interceptors.isEmpty()) {
            return new ServiceCallDescriptor() {
                @Override
                public ServiceCall getServiceCall() {
                    return new InterceptedServiceCall(bean, method, null, null);
                }

                @Override
                public String getInterceptorDescriptor() {
                    return buildInterceptorDescription(groupName, apiServiceMapping.include(), apiServiceMapping.exclude());
                }
            };
        }

        interceptors.sort(Comparator.comparingInt(ServiceCallInterceptor::getOrder));
        ServiceCallInterceptor[] prefix = null;
        ServiceCallInterceptor[] suffix = null;

        int i = 0;
        for (ServiceCallInterceptor interceptor : interceptors) {
            if (interceptor.getOrder() > 0) {
                suffix = interceptors.subList(i, interceptors.size()).toArray(new ServiceCallInterceptor[interceptors.size() - i]);
                break;
            }
            i++;
        }
        prefix = interceptors.subList(0, i).toArray(new ServiceCallInterceptor[i]);

        ServiceCallInterceptor[] prefixRef = prefix;
        ServiceCallInterceptor[] suffixRef = suffix;
        return new ServiceCallDescriptor() {
            @Override
            public ServiceCall getServiceCall() {
                return new InterceptedServiceCall(bean, method, prefixRef, suffixRef);
            }

            @Override
            public String getInterceptorDescriptor() {
                return buildInterceptorDescription(groupName, apiServiceMapping.include(), apiServiceMapping.exclude());
            }
        };
    }

    private String buildInterceptorDescription(String groupName, @Nullable String[] includes, @Nullable String[] excludes) {
        StringBuilder description = new StringBuilder();
        description.append("interceptor group: ");
        description.append(StringUtils.isEmpty(groupName) ? "[default]" : groupName);
        description.append("(");
        description.append(ArrayUtils.isEmpty(includes) ? "includes: []" : "includes: [" + StringUtils.join(includes) + "]");
        description.append(", ");
        description.append(ArrayUtils.isEmpty(excludes) ? "excludes: []" : "excludes: [" + StringUtils.join(excludes) + "]");
        description.append(")");
        return description.toString();
    }

    private Map<String, ServiceCallInterceptorGroup> findInterceptorGroups() {
        Map<String, ServiceCallInterceptorGroup> result = new HashMap<>();
        result.put("", new GlobalServiceCallInterceptorGroup(findInterceptors().values()));
        applicationContext.getBeansOfType(ServiceCallInterceptorGroup.class).forEach((name, bean) -> {
            ServiceCallInterceptorGroup group = processInterceptorGroup(name, bean);
            result.put(group.getName(), group);
        });
        return result;
    }

    private ServiceCallInterceptorGroup processInterceptorGroup(String beanName, ServiceCallInterceptorGroup original) {
        if (StringUtils.isEmpty(original.getName())) {
            return new InterceptorGroupProxy(beanName, original);
        } else {
            return original;
        }
    }

    private Map<String, ServiceCallInterceptor> findInterceptors() {
        Map<String, ServiceCallInterceptor> result = new HashMap<>();
        applicationContext.getBeansOfType(ServiceCallInterceptor.class).forEach((name, bean) -> {
            ServiceCallInterceptor interceptor = processInterceptor(name, bean);
            result.put(interceptor.getName(), interceptor);
        });
        return result;
    }

    private ServiceCallInterceptor processInterceptor(String beanName, ServiceCallInterceptor original) {
        if (StringUtils.isEmpty(original.getName())) {
            return new InterceptorProxy(beanName, original);
        } else {
            return original;
        }
    }

    private Map<String, ServiceCallInterceptor> findInterceptors(Collection<ServiceCallInterceptorGroup> groups) {
        Map<String, ServiceCallInterceptor> result = new HashMap<>();
        groups.forEach(group ->
                group.getInterceptors().forEach(interceptor -> {
                    if (result.containsKey(interceptor.getName())) {
                        throw new IllegalStateException("Same name of interceptor: " + interceptor.getName());
                    }
                    if (StringUtils.isEmpty(interceptor.getName())) {
                        throw new IllegalStateException("An interceptor has empty name in group " + group.getName());
                    }
                    result.put(interceptor.getName(), interceptor);
                }));
        return result;
    }

    private class GlobalServiceCallInterceptorGroup implements ServiceCallInterceptorGroup {

        private final List<ServiceCallInterceptor> interceptors;

        private GlobalServiceCallInterceptorGroup(Collection<ServiceCallInterceptor> interceptors) {
            List<ServiceCallInterceptor> temp = new ArrayList<>(interceptors.size());
            temp.addAll(interceptors);
            temp.sort(Comparator.comparingInt(ServiceCallInterceptor::getOrder));
            this.interceptors = Collections.unmodifiableList(temp);
        }

        @Override
        public String getName() {
            return "";
        }

        @Override
        public List<ServiceCallInterceptor> getInterceptors() {
            return interceptors;
        }
    }

    private class InterceptorGroupProxy implements ServiceCallInterceptorGroup {

        private final String beanName;
        private final ServiceCallInterceptorGroup group;

        private InterceptorGroupProxy(String beanName, ServiceCallInterceptorGroup group) {
            this.beanName = beanName;
            this.group = group;
        }

        @Override
        public String getName() {
            return beanName;
        }

        @Override
        public List<ServiceCallInterceptor> getInterceptors() {
            return group.getInterceptors();
        }
    }

    private class InterceptorProxy implements ServiceCallInterceptor {

        private final String beanName;
        private final ServiceCallInterceptor interceptor;

        private InterceptorProxy(String beanName, ServiceCallInterceptor interceptor) {
            this.beanName = beanName;
            this.interceptor = interceptor;
        }

        @Override
        public int getOrder() {
            return interceptor.getOrder();
        }

        @Override
        public void doIntercept(Request request, @Nullable Object result, Map<Object, Object> ext) {
            interceptor.doIntercept(request, result, ext);
        }

        @Override
        @Nullable
        public String getName() {
            return beanName;
        }
    }

    private class InterceptedServiceCall implements ServiceCall {
        private final Object service;
        private final Method method;
        private final @Nullable
        ServiceCallInterceptor[] prefix;
        private final @Nullable
        ServiceCallInterceptor[] suffix;

        public InterceptedServiceCall(Object service, Method method,
                                      @Nullable ServiceCallInterceptor[] prefix,
                                      @Nullable ServiceCallInterceptor[] suffix) {
            this.service = service;
            this.method = method;
            this.prefix = prefix;
            this.suffix = suffix;
        }

        @Override
        public Object doService(Request request) {
            Map<Object, Object> requestLocal = null;
            try {
                if ((prefix != null && prefix.length > 0) || (suffix != null && suffix.length > 0)) {
                    requestLocal = new HashMap<>();
                }

                if (prefix != null) {
                    for (int i = 0; i < prefix.length; i++) {
                        prefix[i].doIntercept(request, null, requestLocal);
                    }
                }

                Object result = null;

                try {
                    int parameterCount = method.getParameterCount();
                    if (parameterCount == 0) {
                        result = method.invoke(service);
                    } else {
                        Request requestProxy = null;
                        String[] parameterNames = null;
                        Object content = null;
                        boolean gotContent = false;
                        Type[] types = method.getGenericParameterTypes();
                        Object[] args = new Object[types.length];
                        for (int i = 0; i < types.length; i++) {
                            Type type = types[i];
                            if (type instanceof Class) {
                                Class cls = (Class) type;
                                if (!gotContent) {
                                    content = request.getContent();
                                }
                                if (content != null && cls.isAssignableFrom(content.getClass())) {
                                    args[i] = content;
                                    continue;
                                }
                                if (Request.class.equals(cls)) {
                                    args[i] = request;
                                    continue;
                                }
                                if (Session.class.equals(cls)) {
                                    args[i] = request.getSession(true);
                                    continue;
                                }
                                if (Client.class.equals(cls)) {
                                    args[i] = request.getClient();
                                    continue;
                                }
                                if (Channel.class.equals(cls)) {
                                    Session session = request.getSession(true);
                                    if (session == null) {
                                        args[i] = null;
                                    } else {
                                        args[i] = session.getDefaultChannel();
                                    }
                                    continue;
                                }
                                if (isBasicType(cls)) {
                                    if (content == null) {
                                        args[i] = null;
                                    } else {
                                        if (parameterNames == null) {
                                            parameterNames = parameterNameDiscoverer.getParameterNames(method);
                                        }
                                        String propertyName = parameterNames[i];
                                        try {
                                            args[i] = beanOperator.getProperty(content, propertyName);
                                        } catch (Exception e) {
                                            args[i] = null;
                                        }
                                    }
                                    continue;
                                }
                            }
                            if (type instanceof ParameterizedType) {
                                if (Request.class.equals(((ParameterizedType) type).getRawType())) {
                                    if (requestProxy == null) {
                                        if (!gotContent) {
                                            content = request.getContent();
                                        }
                                        if (content == null) {
                                            requestProxy = request;
                                        } else {
                                            requestProxy = new RequestProxy(
                                                    request,
                                                    content,
                                                    ((ParameterizedType) type).getActualTypeArguments()[0]);
                                        }
                                    }
                                    args[i] = requestProxy;
                                    continue;
                                }
                            }
                            args[i] = beanOperator.convert(request.getContent(), type);
                        }
                        result = method.invoke(service, args);
                    }
                } catch (InvocationTargetException e) {
                    if (e.getTargetException() instanceof RuntimeException) {
                        throw (RuntimeException) e.getTargetException();
                    }
                    throw new IllegalStateException(e.getTargetException());
                } catch (Exception e) {
                    if (e instanceof RuntimeException) {
                        throw (RuntimeException) e;
                    }
                    throw new IllegalStateException(e);
                }

                if (suffix != null) {
                    for (int i = 0; i < suffix.length; i++) {
                        suffix[i].doIntercept(request, result, requestLocal);
                    }
                }

                return result;
            } catch (Throwable e) {
                if (serviceCallExceptionInterceptor != null) {
                    return serviceCallExceptionInterceptor.doIntercept(request, e, requestLocal);
                }
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new IllegalStateException(e);
            }
        }

        private boolean isBasicType(Class type) {
            return String.class.isAssignableFrom(type)
                    || type.isPrimitive()
                    || Number.class.isAssignableFrom(type)
                    || Character.class.isAssignableFrom(type);
        }
    }

    private interface ServiceCallDescriptor {

        ServiceCall getServiceCall();

        String getInterceptorDescriptor();
    }

    private class RequestProxy<T> implements Request<T> {

        private final Request<T> original;
        private final T content;

        private RequestProxy(Request<T> original, T content, Type type) {
            this.original = original;
            this.content = beanOperator.convert(content, type);
        }

        @Override
        public String getId() {
            return original.getId();
        }

        @Override
        public String getProtocol() {
            return original.getProtocol();
        }

        @Override
        public String getUrl() {
            return original.getUrl();
        }

        @Override
        public String getRemoteAddress() {
            return original.getRemoteAddress();
        }

        @Override
        public T getContent() {
            return content;
        }

        @Override
        public Client getClient() {
            return original.getClient();
        }

        @Nullable
        @Override
        public Session getSession(boolean create) {
            return original.getSession(create);
        }

        @Nullable
        @Override
        public Object getAttribute(String name) {
            return original.getAttribute(name);
        }

        @Override
        public void setAttribute(String name, Object attribute) {
            original.setAttribute(name, attribute);
        }

        @Override
        public void removeAttribute(String name) {
            original.removeAttribute(name);
        }

        @Override
        public Map<String, Object> getAttributes() {
            return original.getAttributes();
        }
    }
}
