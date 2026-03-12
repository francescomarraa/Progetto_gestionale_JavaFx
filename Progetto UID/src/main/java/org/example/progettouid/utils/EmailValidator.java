package org.example.progettouid.utils;

import java.util.regex.Pattern;

public class EmailValidator {

    private static final String EmailRegex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern pattern = Pattern.compile(EmailRegex);


    public static boolean isValid(String email) {
        if (email == null) return false;



        return pattern.matcher(email).matches();
    }
}
