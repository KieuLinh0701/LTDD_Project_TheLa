package com.TheLa.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("productId")
    private Long productId;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("status")
    private Boolean status;
    @SerializedName("productMainImage")
    private String productMainImage;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductMainImage() {
        return productMainImage;
    }

    public void setProductMainImage(String productMainImage) {
        this.productMainImage = productMainImage;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
