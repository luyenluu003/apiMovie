package com.alibou.security.feature.SearchKeyword.service;

import com.alibou.security.feature.SearchKeyword.dao.SearchkeywordDao;
import com.alibou.security.feature.SearchKeyword.model.Searchkeyword;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@CacheConfig(cacheManager = "cacheManager3Hours")
public class SearchKeywordServiceImpl implements SearchKeywordService {
    @Autowired
    private SearchkeywordDao searchkeywordDao;

    @Override
    @Cacheable(value = "searchkeyword", key = "'list-serchkeyword'")
    public List<Searchkeyword> findAllKeywords() {
        List<Searchkeyword> searchkeywords = searchkeywordDao.getAllSearchKeyword();
        log.info("CHECK KEYWORD: {}", searchkeywords);
        if (searchkeywords == null || searchkeywords.isEmpty()) {
            return null;
        }
        return searchkeywords;
    }

    @CacheEvict(value = "searchkeyword", allEntries = true)
    public void clearAllSearchKeywordCache() {
        log.info("Clearing all search keyword cache for all users...");
    }

}
