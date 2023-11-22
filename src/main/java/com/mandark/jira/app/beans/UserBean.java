package com.mandark.jira.app.beans;

import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.spi.app.EntityBean;


public class UserBean implements EntityBean<User> {

    // Fields
    // ------------------------------------------------------------------------

    private String firstName;

    private String lastName;

    private String password;

    private String email;

    private String phone;


    // Constructors
    // ------------------------------------------------------------------------

    public UserBean() {
        super();
    }

    public UserBean(String firstName, String lastName, String password, String email, String phone) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    // Getters and Setters
    // ------------------------------------------------------------------------

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String mail) {
        this.email = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
