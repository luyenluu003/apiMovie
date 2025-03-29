package com.alibou.security.feature.banner.service;

import com.alibou.security.api.v1.dto.banner.BannerDto;
import com.alibou.security.feature.banner.model.Banner;

import java.util.List;

public interface BannerSerivce {
    List<BannerDto> findALlBanners(String userId);

    void clearAllBannersCache();
}
