package com.alibou.security.feature.category.dao;

import com.alibou.security.feature.category.model.Category;

import java.util.List;

public interface CategoryDao {
    List<Category> getAllCategoryByCategoryId(String categoryId);

    Category getCategoryByCategoryId(String categoryId);

    List<Category> getCategoryByListId(List<String> listId);

    List<Category> getCategoryAll();

}
