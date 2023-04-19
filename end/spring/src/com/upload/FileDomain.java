package com.upload;

import org.springframework.web.multipart.MultipartFile;

public class FileDomain {
    private String description;
    private MultipartFile file;
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public MultipartFile getMyfile() {
        return file;
    }
    public void setMyfile(MultipartFile myfile) {
        this.file = myfile;
    }
}

