package com.sonluo.spongebob.spring.server.impl;

import com.alibaba.fastjson.JSON;
import com.sonluo.spongebob.spring.server.BeanOperator;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

public class DefaultBeanOperator implements BeanOperator {

    public static final DefaultBeanOperator INSTANCE = new DefaultBeanOperator();

    @Override
    public <T> T convert(Object src, Type type) {
        if (src.getClass().equals(type)) {
            return (T) src;
        }
        if (type instanceof Class) {
            return convert(src, (Class<T>) type);
        } else {
            String json = JSON.toJSONString(src);
            if (json.startsWith("[")) {
                return JSON.parseArray(json).toJavaObject(type);
            } else {
                return JSON.parseObject(json).toJavaObject(type);
            }
        }
    }

    @Override
    public <T> T convert(Object src, Class<T> type) {
        if (src.getClass().equals(type)) {
            return (T) src;
        }
        if (String.class.equals(type)) {
            return (T) String.valueOf(src);
        } else if (int.class.equals(type) || Integer.class.equals(type)) {
            return (T) new Integer(String.valueOf(src));
        } else if (long.class.equals(type) || Long.class.equals(type)) {
            return (T) new Long(String.valueOf(src));
        } else if (float.class.equals(type) || Float.class.equals(type)) {
            return (T) new Float(String.valueOf(src));
        } else if (double.class.equals(type) || Double.class.equals(type)) {
            return (T) new Double(String.valueOf(src));
        } else if (byte.class.equals(type) || Byte.class.equals(type)) {
            return (T) new Byte(String.valueOf(src));
        } else if (short.class.equals(type) || Short.class.equals(type)) {
            return (T) new Short(String.valueOf(src));
        } else if (char.class.equals(type) || Character.class.equals(type)) {
            String str = String.valueOf(src);
            return (T) new Character(str.length() == 0 ? (char) 0 : str.charAt(0));
        } else if (BigInteger.class.equals(type)) {
            return (T) new BigInteger(String.valueOf(src));
        } else if (BigDecimal.class.equals(type)) {
            return (T) new BigDecimal(String.valueOf(src));
        } else {
            try {
                T dest = type.newInstance();
                BeanUtils.copyProperties(dest, src);
                return dest;
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    @Override
    public Object getProperty(Object obj, String name) {
        try {
            return BeanUtils.getProperty(obj, name);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void setProperty(Object obj, String name, Object property) {
        try {
            BeanUtils.setProperty(obj, name, property);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean isBasicType(Class type) {
        return String.class.isAssignableFrom(type)
                || type.isPrimitive()
                || Number.class.isAssignableFrom(type)
                || Character.class.isAssignableFrom(type);
    }
}
