package com.alibou.security.api.v1.searchkeyword;

import com.alibou.security.feature.SearchKeyword.model.Searchkeyword;
import com.alibou.security.feature.SearchKeyword.service.SearchKeywordService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/v1/searchkeyword")
@Log4j2
public class SearchKeywordController {

    @Autowired
    private SearchKeywordService searchKeywordService;

    @GetMapping("/allSearchKeyword")
    public ResponseEntity<List<Searchkeyword>> findAllSearchKeyword(
            @RequestHeader("Accept-language") String lang,
            HttpServletRequest request
    ) {
        Long start = System.currentTimeMillis();

        List<Searchkeyword> searchkeywords = searchKeywordService.findAllKeywords();

        // Kiểm tra nếu không có dữ liệu
        if (searchkeywords == null || searchkeywords.isEmpty()) {
            log.info("[searchkeywords]:" + "No searchkeywords found");
            return ResponseEntity.noContent().build();
        }
        Long t = System.currentTimeMillis() - start;
        log.info("|END|Executime=" + t);
        return ResponseEntity.ok(searchkeywords);
    }

    @DeleteMapping("/clearAllCacheSearchKeyword")
    public ResponseEntity<String> clearAllCacheSearchKeyword() {
        try {
            // Xóa cache cho tất cả các user
            searchKeywordService.clearAllSearchKeywordCache();
            return ResponseEntity.ok("All search keyword cache cleared!");
        } catch (Exception e) {
            log.error("Error clearing cache: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to clear search keyword cache!");
        }
    }
}
