package com.TheLa.services.implement;

import androidx.lifecycle.ViewModel;

import com.TheLa.models.Category;
import com.TheLa.dao.implement.CategoryDao;
import com.TheLa.services.ICategoryService;

import java.util.List;

public class CategoryService extends ViewModel implements ICategoryService {
    private final CategoryDao categoryDao;

    public CategoryService() {
        categoryDao = new CategoryDao();
    }

    @Override
    public List<Category> getAllActiveAndNotDeletedCategories() {
        return categoryDao.getAllActiveAndNotDeletedCategories();
    }
}
