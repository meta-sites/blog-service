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

    public static Date stringToDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.parse(dateString);
    }

    public static String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String findLongestCommonSubstring(String str1, String str2) {
        int m = str1.length();
        int n = str2.length();
        int[][] dp = new int[m + 1][n + 1];
        int maxLength = 0;
        int endIndex = -1;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    if (dp[i][j] > maxLength) {
                        maxLength = dp[i][j];
                        endIndex = i - 1;
                    }
                } else {
                    dp[i][j] = 0;
                }
            }
        }

        if (maxLength == 0) {
            return "";
        }

        return str1.substring(endIndex - maxLength + 1, endIndex + 1);
    }
}
