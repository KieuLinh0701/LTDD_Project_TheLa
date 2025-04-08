package com.TheLa.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReviewImageDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("imageId")
	private Long imageId;
    @SerializedName("image")
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
}
