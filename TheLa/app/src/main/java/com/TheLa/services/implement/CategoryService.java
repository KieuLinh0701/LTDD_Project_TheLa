package com.TheLa.services.implement;

import androidx.lifecycle.ViewModel;

import com.TheLa.dao.implement.CategoryDao;
import com.TheLa.models.CategoryModel;
import com.TheLa.services.ICategoryService;

import java.util.List;

public class CategoryService extends ViewModel implements ICategoryService {
    private final CategoryDao categoryDao;

    public CategoryService() {
        categoryDao = new CategoryDao();
    }

    @Override
    public List<CategoryModel> getAllActiveAndNotDeletedCategories() {
        return categoryDao.getAllActiveAndNotDeletedCategories();
    }
}
