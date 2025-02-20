package com.TheLa.dao;


import com.TheLa.models.Category;

import java.util.List;

public interface ICategoryDao {
    List<Category> getAllActiveAndNotDeletedCategories();
}
