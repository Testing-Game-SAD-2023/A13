package com.groom.manvsclass.util.filesystem.upload;

public class FileUploadResponse {
    private String fileName;
    private String downloadUri;
    private long size;
    //MODIFICA (11/02/2024)
    private String errorMessage;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    // public FileUploadResponse(String errorMessage) {
    //     this.errorMessage = errorMessage;
    // }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    //FINE MODIFICA

}
