package com.slp.com.uberclone.data;

/**
 * Created by Lakshmiprasad on 06/11/2017.
 */

public class User {

    private String userName;
    private boolean driver;
    private String emailId;

    public User(String userName, boolean driver, String emailId) {
        this.userName = userName;
        this.driver = driver;
        this.emailId = emailId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isDriver() {
        return driver;
    }

    public void setDriver(boolean driver) {
        this.driver = driver;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

}
