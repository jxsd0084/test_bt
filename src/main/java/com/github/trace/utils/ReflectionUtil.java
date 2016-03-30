package com.github.trace.utils;

import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by chenlong on 2016/3/26.
 */
public class ReflectionUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    public static JSONArray convertToJSON(Object obj) {
        JSONArray jsonArray = new JSONArray();
        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
            try {
                for (Field field : fields) {
                    PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), clazz);
                    Method getMethod = descriptor.getReadMethod();
                    jsonArray.add(getMethod.invoke(obj));
                }
            } catch (IntrospectionException e){
                LOGGER.error("IntrospectionException...", e);
            } catch (IllegalAccessException e) {
                LOGGER.error("IllegalAccessException... ", e);
            } catch (InvocationTargetException e) {
                LOGGER.error("InvocationTargetException... ", e);
            }
        return jsonArray;
    }

}
