package com.application.system.core.libraries.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;

/**
 * @auther ttm
 * @date 2018/9/17
 */
public class Json {

    public static String toJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = "";

        try {
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            result = objectMapper.writeValueAsString(object);
        } catch (Exception arg3) {
            arg3.printStackTrace();
        }

        return result;
    }

    public static Object fromJson(String requestStr, Class clazz) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Object object = null;

        try {
            object = objectMapper.readValue(requestStr, clazz);
        } catch (Exception arg4) {
            arg4.printStackTrace();
        }

        return object;
    }

    public static Object fromJson(String requestStr, Class collectionClazz, Class... elementClazzes) {
        if (StringUtils.isEmpty(requestStr)) {
            return null;
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            Object object = null;

            try {
                JavaType e = objectMapper.getTypeFactory().constructParametricType(collectionClazz, elementClazzes);
                object = objectMapper.readValue(requestStr, e);
            } catch (Exception arg5) {
                arg5.printStackTrace();
            }

            return object;
        }
    }

}
