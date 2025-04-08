package com.TheLa.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class ReviewDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @SerializedName("reviewId")
	private long reviewId;
    @SerializedName("rating")
    private int rating;
    @SerializedName("content")
    private String content;
    @SerializedName("reviewDate")
    private Timestamp reviewDate;
    @SerializedName("user")
    private UserDto user;
    @SerializedName("reviewImages")
    private List<ReviewImageDto> reviewImages;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public List<ReviewImageDto> getReviewImages() {
        return reviewImages;
    }

    public void setReviewImages(List<ReviewImageDto> reviewImages) {
        this.reviewImages = reviewImages;
    }
}
