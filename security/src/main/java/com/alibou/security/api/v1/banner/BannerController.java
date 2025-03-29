package com.alibou.security.api.v1.banner;

import com.alibou.security.api.v1.dto.banner.BannerDto;
import com.alibou.security.api.v1.dto.favorite.FavoriteWithMovieDto;
import com.alibou.security.feature.banner.model.Banner;
import com.alibou.security.feature.banner.service.BannerSerivce;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping("/v1/banner")
public class BannerController {

    @Autowired
    private BannerSerivce bannerService;

    @GetMapping("/bannerAll")
    public ResponseEntity<List<BannerDto>> bannerAll(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("userId") String userId,
            HttpServletRequest request
    ) {
        Long start = System.currentTimeMillis();
        log.info("CHECK userId: {} ", userId);

        List<BannerDto> banners = bannerService.findALlBanners(userId);

        if (banners == null || banners.isEmpty()) {
            log.info("[banners]: No banners found");
            return ResponseEntity.noContent().build();
        }

        Long t = System.currentTimeMillis() - start;
        log.info("|END| Execution time = " + t);

        return ResponseEntity.ok(banners);
    }

    @DeleteMapping("/clearCacheBanners")
    public ResponseEntity<String> clearAllBannersCache() {
        try {
            bannerService.clearAllBannersCache();
            return ResponseEntity.ok("All banner cache cleared!");
        } catch (Exception e) {
            log.error("Error clearing cache: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to clear banner cache!");
        }
    }
}
