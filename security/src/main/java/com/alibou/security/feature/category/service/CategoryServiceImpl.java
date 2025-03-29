package com.alibou.security.feature.category.service;

import com.alibou.security.api.v1.dto.banner.BannerDto;
import com.alibou.security.feature.category.dao.CategoryDao;
import com.alibou.security.feature.category.model.Category;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Log4j2
@Service
@CacheConfig(cacheManager = "cacheManager3Hours")
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryDao categoryDao;

    public List<Category> getCategoryByListId(String userId, List<String> listId, long timestamp, String id) {
        List<Category> listCategory = categoryDao.getCategoryByListId(listId);

        return listCategory != null ? listCategory : Collections.emptyList();
    }

    @Override
    @Cacheable(value = "categories", key = "'list-categories:'.concat(#userId)")
    public List<Category> findALlCategory(String userId) {
        String cacheKey = "list-banner:".concat(userId);
        log.info("Cache Key: {}", cacheKey);
        List<Category> categories = categoryDao.getCategoryAll();

        if (categories == null || categories.isEmpty()) {
            return Collections.emptyList();
        }

        return categories;
    }

    @CacheEvict(value = "categories", allEntries = true)
    public void clearAllCategoriesCache() {
        log.info("Clearing all categories cache for all users...");
    }

}
