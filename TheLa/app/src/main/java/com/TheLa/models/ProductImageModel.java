package com.TheLa.models;

public class ProductImageModel {
    private Long imageId;
    private Long productId;
    private String image;
    private Boolean isMain;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getImageId() {
        return imageId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Boolean getMain() {
        return isMain;
    }

    public void setMain(Boolean main) {
        isMain = main;
    }
}
