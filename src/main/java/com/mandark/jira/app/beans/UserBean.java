package com.mandark.jira.app.beans;

import com.mandark.jira.app.enums.UserRole;
import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.spi.app.EntityBean;


public class UserBean implements EntityBean<User> {

    // Fields
    // ------------------------------------------------------------------------

    private String userName;

    private String password;

    private String mail;

    private UserRole role;


    // Constructors
    // ------------------------------------------------------------------------

    public UserBean() {
        super();
        // TODO Auto-generated constructor stub
    }


    public UserBean(String userName, String password, String mail, UserRole role) {
        super();
        this.userName = userName;
        this.password = password;
        this.mail = mail;
        this.role = role;
    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }


}
