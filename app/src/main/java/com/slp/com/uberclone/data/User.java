package com.slp.com.uberclone.data;

/**
 * Created by Lakshmiprasad on 06/11/2017.
 */

public class User {
    private String userName;
    private boolean isDriver;
    private String emailId;

    public User(String userName, boolean isDriver, String emailId) {
        this.userName = userName;
        this.isDriver = isDriver;
        this.emailId = emailId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isDriver() {
        return isDriver;
    }

    public void setDriver(boolean driver) {
        isDriver = driver;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
