package com.TheLa.dao;


import com.TheLa.models.CategoryModel;
import com.TheLa.models.ReviewImageModel;

import java.util.List;

public interface IReviewImageDao {
    List<ReviewImageModel> findReviewImageByReviewId(Long reviewId);
}
