package com.TheLa.services;

import com.TheLa.models.ReviewModel;

import java.util.List;

public interface IReviewService {
    List<ReviewModel> findReviewByProductId(Long productId);
}
