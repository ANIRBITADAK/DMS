package com.tux.dms.cache;

import com.tux.dms.dto.User;

public class SessionCache {

    private static SessionCache instance;

    private String token;
    private User user;

    private SessionCache() {

    }
    public static SessionCache getSessionCache() {
        if (instance == null) {
            instance = new SessionCache();
        }
        return instance;

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
