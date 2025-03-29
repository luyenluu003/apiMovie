package com.alibou.security.feature.search.service;

import com.alibou.security.feature.search.model.ResultGroup;
import com.alibou.security.feature.search.model.ResultItem;

import java.util.List;

public interface SearchService {

    List<ResultGroup> searchAllContent(String userId, String qrTxt, String lang, long timestamp);
    List<ResultItem> searchByMovie(String userId, String qrTxt, int page, int size, long timestamp);

    List<ResultItem> searchByActor(String userId, String qrTxt, int page, int size, long timestamp);

    List<ResultItem> searchByCategory(String userId, String qrTxt, int page, int size, long timestamp);
}
