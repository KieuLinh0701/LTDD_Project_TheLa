package com.TheLa.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CategoryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("categoryId")
	private Long categoryId;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String image;

    private transient boolean isSelected = false;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}