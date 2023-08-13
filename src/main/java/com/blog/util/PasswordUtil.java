package com.blog.util;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;



public class PasswordUtil {

    public static String encodePassword(String password) {
        return DigestUtils.sha256Hex(password);
    }

    public static boolean checkPassword(String inputPassword, String hashedPassword) {
        String hashedInputPassword = encodePassword(inputPassword);
        return hashedInputPassword.equals(hashedPassword);
    }

    public static Boolean isValidatePassword(String password) {
        return StringUtils.isNotBlank(password) && password.length() >= 8;
    }
}
