package org.harry.email.utils;

public class EmailUtils {

    public static String getEmailMessage(String name, String host, String token){
        return "Hello " + name + ", \n\nWelcome to the Trust." +
                "\n\nYour new Tranxactrust account has been created successfully. Please click the link below to verify " +
                "your account. \n\n" + getVerificationUrl(host, token) + "\n\n\nThe support Team";
    }

    public static String getVerificationUrl(String host, String token) {
        return host + "/api/users?token=" + token;
    }
}

