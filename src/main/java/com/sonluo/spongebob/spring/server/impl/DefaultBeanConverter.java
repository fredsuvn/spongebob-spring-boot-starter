package com.sonluo.spongebob.spring.server.impl;

import com.alibaba.fastjson.JSON;
import com.sonluo.spongebob.spring.server.BeanConverter;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

public class DefaultBeanConverter implements BeanConverter {

    public static final DefaultBeanConverter INSTANCE = new DefaultBeanConverter();

    @Override
    public <T> T convert(Object src, Type type) {
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

    private <T> T convert(Object src, Class<T> type) {
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
}
