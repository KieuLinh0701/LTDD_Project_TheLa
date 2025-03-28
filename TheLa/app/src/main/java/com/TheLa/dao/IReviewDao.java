package com.TheLa.dao;

import com.TheLa.models.ReviewModel;
import com.TheLa.models.SizeModel;

import java.util.List;

public interface IReviewDao {
    List<ReviewModel> findReviewByProductId(Long productId);
}
