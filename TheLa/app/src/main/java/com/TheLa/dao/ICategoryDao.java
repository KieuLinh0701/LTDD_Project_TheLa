package com.TheLa.dao;


import com.TheLa.models.CategoryModel;

import java.util.List;

public interface ICategoryDao {
    List<CategoryModel> getAllActiveAndNotDeletedCategories();
}
