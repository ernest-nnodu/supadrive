package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Credential {

    private Integer credentialid;
    private String url;
    private String username;
    private String password;
    private Integer userid;

    public Credential() {
    }

    public Credential(String url, String username, String password, Integer userid) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.userid = userid;
    }
}
