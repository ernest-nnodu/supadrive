package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Note {

    private Integer noteid;
    private String notetitle;
    private String notedescription;
    private Integer userid;

    public Note() {
    }

    public Note(String notetitle, String notedescription, Integer userid) {
        this.notetitle = notetitle;
        this.notedescription = notedescription;
        this.userid = userid;
    }
}
