package com.tux.dms.dto;

import java.util.List;

public class ImageUploadResponse {

    private String message;
    private List<String> pdf;
    private List<String> img;

    public List<String> getPdf() {
        return pdf;
    }

    public List<String> getImg() {
        return img;
    }

    public void setPdf(List<String> pdf) {
        this.pdf = pdf;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
