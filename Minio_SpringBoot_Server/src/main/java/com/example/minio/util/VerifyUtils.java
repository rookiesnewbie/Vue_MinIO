package com.example.minio.util;


import com.example.minio.enums.VerifyRegexEnum;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyUtils {

    public static boolean verify(String regex, String value) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static boolean verify(VerifyRegexEnum regex, String value) {
        return verify(regex.getRegex(), value);
    }

    public static void main(String[] args) {
        System.out.println(new File("E:\\代码生成\\..\\workspace-java").exists());

    }
}

