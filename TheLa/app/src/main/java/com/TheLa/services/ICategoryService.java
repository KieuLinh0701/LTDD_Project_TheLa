package com.TheLa.services;

import com.TheLa.models.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllActiveAndNotDeletedCategories();
}
