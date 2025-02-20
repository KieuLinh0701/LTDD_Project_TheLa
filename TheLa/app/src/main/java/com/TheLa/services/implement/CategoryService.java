package com.TheLa.services.implement;

import androidx.lifecycle.ViewModel;

import com.TheLa.models.Category;
import com.TheLa.repository.implement.CategoryRepository;
import com.TheLa.services.ICategoryService;

import java.util.List;

public class CategoryService extends ViewModel implements ICategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService() {
        categoryRepository = new CategoryRepository();
    }

    @Override
    public List<Category> getAllActiveAndNotDeletedCategories() {
        return categoryRepository.getAllActiveAndNotDeletedCategories();
    }
}
