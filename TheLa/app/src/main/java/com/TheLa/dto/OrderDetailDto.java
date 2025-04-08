package com.TheLa.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderDetailDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("orderDetailId")
    private Long orderDetailId;
    @SerializedName("quantity")
    private Integer quantity;
    @SerializedName("price")
    private BigDecimal price;
    @SerializedName("productSize")
    private ProductSizeDto productSize;
    @SerializedName("review")
    private ReviewDto review;

    public Long getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductSizeDto getProductSize() {
        return productSize;
    }

    public void setProductSize(ProductSizeDto productSize) {
        this.productSize = productSize;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ReviewDto getReview() {
        return review;
    }

    public void setReview(ReviewDto review) {
        this.review = review;
    }
}
