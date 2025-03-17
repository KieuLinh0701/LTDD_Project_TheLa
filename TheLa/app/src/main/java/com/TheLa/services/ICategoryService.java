package com.TheLa.services;

import com.TheLa.models.CategoryModel;

import java.util.List;

public interface ICategoryService {
    List<CategoryModel> getAllActiveAndNotDeletedCategories();
}
