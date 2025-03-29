package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {

    private Integer userid;
    private String firstname;
    private String lastname;
    private String username;
    private String salt;
    private String password;

    public User() {
    }

    public User(String firstname, String lastname, String username, String salt, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.salt = salt;
        this.password = password;
    }

}
