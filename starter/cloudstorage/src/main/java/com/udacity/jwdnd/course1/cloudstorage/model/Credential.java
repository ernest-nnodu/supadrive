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
    private String enckey;
    private Integer userid;
    private String temppassword;

    public Credential() {
    }

    public Credential(String url, String username, String password, String enckey, Integer userid) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.enckey = enckey;
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "Credential{" +
                "credentialid=" + credentialid +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enckey='" + enckey + '\'' +
                ", userid=" + userid +
                ", temppassword='" + temppassword + '\'' +
                '}';
    }
}
