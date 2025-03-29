package com.alibou.security.feature.favorite.service;

import com.alibou.security.api.v1.dto.favorite.FavoriteWithMovieDto;
import com.alibou.security.feature.favorite.model.Favorite;

import java.util.List;

public interface FavoriteService {
    Favorite likeFavorite(String email, String movieCode);

    Favorite unlikeFavorite(String email, String movieCode);

    Favorite getFavoriteByEmail(String email, String movieCode);

    List<FavoriteWithMovieDto> getPlayListFavoriteByEmailAndMovieCode(String email, Integer page, Integer size);

    void clearAllFavoriteCache();
}
