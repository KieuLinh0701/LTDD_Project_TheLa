package com.TheLa.services.implement;

import androidx.lifecycle.ViewModel;

import com.TheLa.dao.implement.CategoryDao;
import com.TheLa.dao.implement.ReviewDao;
import com.TheLa.models.CategoryModel;
import com.TheLa.models.ReviewModel;
import com.TheLa.services.ICategoryService;
import com.TheLa.services.IReviewService;

import java.util.List;

public class ReviewService extends ViewModel implements IReviewService {
    private final ReviewDao dao;

    public ReviewService() {
        dao = new ReviewDao();
    }

    @Override
    public List<ReviewModel> findReviewByProductId(Long productId) {
        return dao.findReviewByProductId(productId);
    }
}
