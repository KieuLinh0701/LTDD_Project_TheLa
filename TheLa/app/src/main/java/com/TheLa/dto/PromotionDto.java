package com.TheLa.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;

public class PromotionDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("promotionId")
    private Long promotionId;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String image;
    @SerializedName("description")
    private String description;
    @SerializedName("discountPercentage")
    private Double discountPercentage;
    @SerializedName("minimumOrderValue")
    private Double minimumOrderValue;
    @SerializedName("startDate")
    private Timestamp startDate;
    @SerializedName("endDate")
    private Timestamp endDate;
    @SerializedName("quantity")
    private Long quantity;
    @SerializedName("quantityUsed")
    private Long quantityUsed;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getMinimumOrderValue() {
        return minimumOrderValue;
    }

    public void setMinimumOrderValue(Double minimumOrderValue) {
        this.minimumOrderValue = minimumOrderValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getQuantityUsed() {
        return quantityUsed;
    }

    public void setQuantityUsed(Long quantityUsed) {
        this.quantityUsed = quantityUsed;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }
}