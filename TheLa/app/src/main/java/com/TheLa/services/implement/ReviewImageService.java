package com.TheLa.services.implement;

import com.TheLa.dao.implement.ProductImageDao;
import com.TheLa.dao.implement.ReviewImageDao;
import com.TheLa.models.ProductImageModel;
import com.TheLa.models.ReviewImageModel;
import com.TheLa.services.IProductImageService;
import com.TheLa.services.IReviewImageService;

import java.util.List;

public class ReviewImageService implements IReviewImageService {

    private final ReviewImageDao dao;

    public ReviewImageService() {
        dao = new ReviewImageDao();
    }
    @Override
    public List<ReviewImageModel> findReviewImageByReviewId(Long reviewId) {
        return dao.findReviewImageByReviewId(reviewId);
    }
}
