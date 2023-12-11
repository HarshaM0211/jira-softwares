package com.mandark.jira.app.dto;

import com.mandark.jira.app.enums.UserRole;
import com.mandark.jira.app.persistence.orm.entity.User;
import com.mandark.jira.spi.app.EntityDTO;


public class UserDTO extends EntityDTO<User> {

    // Fields
    // ------------------------------------------------------------------------

    private final String userName; // firstName + lastName

    private final String email;

    private final UserRole role;

    private final String phone;

    // Constructors
    // ------------------------------------------------------------------------

    public UserDTO(User e) {
        super(e);
        this.userName = e.getFirstName() + e.getLastName();
        this.email = e.getEmail();
        this.role = e.getRole();
        this.phone = e.getPhone();
    }

    // Getters
    // ------------------------------------------------------------------------

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

    // Object Methods
    // ------------------------------------------------------------------------

    @Override
    public String toString() {
        return "UserDTO [userName=" + userName + ", email=" + email + ", role=" + role + ", phone=" + phone + "]";
    }
}
