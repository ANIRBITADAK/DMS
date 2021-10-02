package com.tux.dms.dto;

public class Image {

    private String image_path;
    private int img_id;

    public Image(String image_path, int img_id) {
        this.image_path = image_path;
        this.img_id = img_id;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public int getImg_id() {
        return img_id;
    }

    public void setImg_id(int img_id) {
        this.img_id = img_id;
    }






}
