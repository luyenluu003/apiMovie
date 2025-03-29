package com.alibou.security.api.v1.category;

import com.alibou.security.feature.category.model.Category;
import com.alibou.security.feature.category.service.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categoryAll")
    public ResponseEntity<List<Category>> categoryAll(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("userId") String userId,
            HttpServletRequest request
    ) {
        Long start = System.currentTimeMillis();
        log.info("CHECK userId: {} ", userId);

        List<Category> categories = categoryService.findALlCategory(userId);

        if (categories == null || categories.isEmpty()) {
            log.info("[categories]: No categories found");
            return ResponseEntity.noContent().build();
        }

        Long t = System.currentTimeMillis() - start;
        log.info("|END| Execution time = " + t);

        return ResponseEntity.ok(categories);
    }

    @DeleteMapping("/clearCacheCategories")
    public ResponseEntity<String> clearCacheCategories() {
        try {
            categoryService.clearAllCategoriesCache();
            return ResponseEntity.ok("All category cache cleared!");
        } catch (Exception e) {
            log.error("Error clearing cache: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to clear category cache!");
        }
    }

}
