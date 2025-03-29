package com.alibou.security.feature.category.service;

import com.alibou.security.api.v1.dto.banner.BannerDto;
import com.alibou.security.feature.category.model.Category;

import java.util.List;

public interface CategoryService {

    List<Category>  getCategoryByListId(String userId, List<String> listId, long timestamp, String id);

    List<Category> findALlCategory(String userId);

    void clearAllCategoriesCache();
}
