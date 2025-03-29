package com.alibou.security.feature.favorite.dao;

import com.alibou.security.api.v1.dto.favorite.FavoriteWithMovieDto;
import com.alibou.security.feature.favorite.model.Favorite;

import java.util.List;

public interface FavoriteDao {
    Favorite likeMovieByEmail(String email, String movieCode);

    Favorite unLikeMovieByEmail(String email, String movieCode);

    Favorite getFavoriteByEmail(String email, String movieCode);

    List<FavoriteWithMovieDto> getFavoriteActiveByEmailandMovielCode(String email, Integer page, Integer size);

}
