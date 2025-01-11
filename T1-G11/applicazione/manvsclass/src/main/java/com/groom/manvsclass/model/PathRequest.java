package com.groom.manvsclass.model;

import org.springframework.web.multipart.MultipartFile;

public class PathRequest {

    private String path;
    private MultipartFile file;

    public PathRequest(String path, MultipartFile file) {
        this.path = path;
        this.file = file;
    }

    public PathRequest() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}