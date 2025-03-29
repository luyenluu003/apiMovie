package com.alibou.security.feature.SearchKeyword.service;

import com.alibou.security.feature.SearchKeyword.model.Searchkeyword;

import java.util.List;

public interface SearchKeywordService {
    List<Searchkeyword> findAllKeywords();

    void clearAllSearchKeywordCache();
}
