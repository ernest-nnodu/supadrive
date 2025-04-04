package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class File {

    private Integer fileid;
    private String filename;
    private String contenttype;
    private String filesize;
    private Integer userid;
    private byte[] filedata;

    public File() {
    }

    public File(String filename, String contenttype, String filesize, Integer userid, byte[] filedata) {
        this.filename = filename;
        this.contenttype = contenttype;
        this.filesize = filesize;
        this.userid = userid;
        this.filedata = filedata;
    }
}
