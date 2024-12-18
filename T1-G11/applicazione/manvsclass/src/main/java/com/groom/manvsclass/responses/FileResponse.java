package com.groom.manvsclass.responses;

public class FileResponse extends Response {

    private String fileName;
    private long size;
    private String path;

    public String getFileName() {
        return fileName;
    }

    public long getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
