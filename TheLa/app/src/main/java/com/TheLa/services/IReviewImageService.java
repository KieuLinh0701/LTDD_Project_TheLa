package com.TheLa.services;


import com.TheLa.models.ReviewImageModel;

import java.util.List;

public interface IReviewImageService {
    List<ReviewImageModel> findReviewImageByReviewId(Long reviewId);
}
