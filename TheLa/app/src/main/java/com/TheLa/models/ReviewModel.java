package com.TheLa.models;

import java.io.Serializable;
import java.sql.Timestamp;

public class ReviewModel implements Serializable {
    private long reviewId;
    private long productId;
    private UserModel user;
    private int rating; // Số sao (1-5)
    private String content; // Nội dung đánh giá
    private Timestamp reviewDate; // Ngày tạo

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Timestamp getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Timestamp reviewDate) {
        this.reviewDate = reviewDate;
    }

    public long getReviewId() {
        return reviewId;
    }

    public void setReviewId(long reviewId) {
        this.reviewId = reviewId;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
