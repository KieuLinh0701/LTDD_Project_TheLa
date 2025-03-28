package com.TheLa.models;

import java.io.Serializable;

public class CategoryModel implements Serializable {
    private Long categoryId;
    private String name;
    private String image;
    private boolean isActive;
    private boolean isDelete;
    private Boolean isSelected;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public Boolean isSelected() {
        return isSelected != null && isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}


