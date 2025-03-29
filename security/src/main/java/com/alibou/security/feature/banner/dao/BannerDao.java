package com.alibou.security.feature.banner.dao;

import com.alibou.security.api.v1.dto.banner.BannerDto;
import com.alibou.security.feature.banner.model.Banner;

import java.time.LocalDate;
import java.util.List;

public interface BannerDao {

    List<BannerDto> getAllBanners(LocalDate currentDate);

}
