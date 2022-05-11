package com.example.articleProgram.sessionManager;

import com.example.articleProgram.model.User;

public class SessionManagerFactory {

    private static User u = null;
    public SessionManagerFactory (User user) {
        this.u = user;
    }
    public User getLoggedInUser() {
        return this.u;
    }
}
