package com.TheLa.repository;


import com.TheLa.models.Category;

import java.util.List;

public interface ICategoryRepository {
    List<Category> getAllActiveAndNotDeletedCategories();
}
