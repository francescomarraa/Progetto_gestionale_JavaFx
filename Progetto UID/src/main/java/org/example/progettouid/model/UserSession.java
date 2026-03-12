package org.example.progettouid.model;

public final class UserSession {

    private static UserSession instance;
    private String email;

    private UserSession(String _mail){
        this.email = _mail;
    }
    public static UserSession getInstance(){
        return instance;
    }
    public static UserSession getInstance(String email){
        if(instance==null){
            instance = new UserSession(email);
        }
        return instance;
    }

    public static void cleanUserSession() {
        instance = null;
    }


    public String getEmail() {
        return email;
    }

}
