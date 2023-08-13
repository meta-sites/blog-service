package com.blog.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilFunction {

    public static String createUrlFriendlyFromTitle(String title) {
        String normalizedText = Normalizer.normalize(title, Normalizer.Form.NFD);
        String replaceText =  normalizedText.replaceAll("[^\\p{ASCII}]", "");
        return replaceText.replaceAll("[^a-zA-Z0-9 ]","").replace(" ","-").toLowerCase();
    }

    public static String convertObjectToString(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T clearReferenceUserObject(T object, Class<T> destinationType) throws JsonProcessingException {
        String userString = UtilFunction.convertObjectToString(object);
        return MapperUtil.mapStringToObject(userString, destinationType);
    }

    public static Date StringToDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.parse(dateString);
    }
}
