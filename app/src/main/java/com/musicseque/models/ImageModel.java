package com.musicseque.models;

public class ImageModel {
    String image_url;
    String base_url;
    boolean isImage;
    int Id;
    boolean isSelected;

    public ImageModel(String base_url, String image_url, boolean isImage, int id, boolean isSelected) {
        this.image_url = image_url;
        this.base_url = base_url;
        this.isImage = isImage;
        this.Id = id;
        this.isSelected = isSelected;
    }


    public String getBase_url() {
        return base_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public boolean isImage() {
        return isImage;
    }

    public int getId() {
        return Id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setBase_url(String base_url) {
        this.base_url = base_url;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
