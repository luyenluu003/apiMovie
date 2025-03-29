package com.alibou.security.feature.banner.service;

import com.alibou.security.api.v1.dto.banner.BannerDto;
import com.alibou.security.feature.banner.dao.BannerDao;
import com.alibou.security.feature.banner.model.Banner;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@Log4j2
@CacheConfig(cacheManager = "cacheManager3Hours")
public class BannerServiceImpl implements BannerSerivce{

    @Autowired
    private BannerDao bannerDao;

    @Override
    @Cacheable(value = "banners", key = "'list-banner:'.concat(#userId)")
    public List<BannerDto> findALlBanners(String userId) {
        String cacheKey = "list-banner:".concat(userId);
        log.info("Cache Key: {}", cacheKey);

        LocalDate today = LocalDate.now();
        log.info("Today: {}", today);
        List<BannerDto> banners = bannerDao.getAllBanners(today);

        if (banners == null || banners.isEmpty()) {
            return Collections.emptyList();
        }

        return banners;
    }

    @CacheEvict(value = "banners", allEntries = true)
    public void clearAllBannersCache() {
        log.info("Clearing all banners cache for all users...");
    }
}
