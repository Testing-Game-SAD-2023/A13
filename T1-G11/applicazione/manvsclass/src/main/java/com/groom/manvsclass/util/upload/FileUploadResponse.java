package com.groom.manvsclass.util.upload;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileUploadResponse {
    private String fileName;
    private String downloadUri;
    private long size;
    private String errorMessage;


}
