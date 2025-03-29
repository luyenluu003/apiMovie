package com.alibou.security.feature.favorite.service;

import com.alibou.security.api.v1.dto.favorite.FavoriteWithMovieDto;
import com.alibou.security.feature.favorite.dao.FavoriteDao;
import com.alibou.security.feature.favorite.model.Favorite;
import com.alibou.security.feature.movie.dao.MovieDao;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;

import java.util.Collections;
import java.util.List;

@Service
@Log4j2
@CacheConfig(cacheManager = "cacheManager3Hours")
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteDao favoriteDao;
    @Autowired
    private View error;

    @Autowired
    private MovieDao movieDao;

    @Override
    public Favorite likeFavorite(String email, String movieCode) {
        log.info("Like Favorite - email: {}, movieCode: {}", email, movieCode);

        Favorite favorite = favoriteDao.likeMovieByEmail(email, movieCode);
        if (favorite == null) {
            log.warn("Can't like movie - favorite record not found or error while updating.");
            return null;
        }

        return favorite;
    }

    @Override
    public Favorite unlikeFavorite(String email, String movieCode) {
        log.info("Like Favorite - email: {}, movieCode: {}", email, movieCode);

        Favorite favorite = favoriteDao.unLikeMovieByEmail(email, movieCode);
        if (favorite == null) {
            log.warn("Can't unlike movie - favorite record not found or error while updating.");
            return null;
        }

        return favorite;
    }

    @Override
//    @Cacheable(value = "favorite", key = "'check-favorite:'.concat(#email).concat(#movieCode)")
    public Favorite getFavoriteByEmail(String email, String movieCode) {
        log.info("Get Favorite - email: {}, movieCode: {}", email, movieCode);
        Favorite favorite = favoriteDao.getFavoriteByEmail(email, movieCode);
        if (favorite == null) {
            log.warn("Can't get Favorite - favorite record not found or error .");
            return null;
        }
        return favorite;
    }

    @Override
    @Cacheable(value = "favorite", key = "'list-favorite-by-email:'.concat(#email).concat(#page).concat(#size)")
    public List<FavoriteWithMovieDto> getPlayListFavoriteByEmailAndMovieCode(String email, Integer page, Integer size) {
        log.info("Get Favorite - email: {}, movieCode: {}", email);

        List<FavoriteWithMovieDto> favorites = favoriteDao.getFavoriteActiveByEmailandMovielCode(email, page, size);

        if (favorites == null || favorites.isEmpty()) {
            log.warn("Can't get Favorite - favorite record not found or error .");
            return Collections.emptyList();
        }

        return favorites;
    }

    @CacheEvict(value = "favorite", allEntries = true)
    public void clearAllFavoriteCache() {
        log.info("Clearing all favorite cache for all users...");
    }
}
