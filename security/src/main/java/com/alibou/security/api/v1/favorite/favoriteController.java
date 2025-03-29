package com.alibou.security.api.v1.favorite;

import com.alibou.security.api.v1.dto.SuccessResDto;
import com.alibou.security.api.v1.dto.favorite.FavoriteDto;
import com.alibou.security.api.v1.dto.favorite.FavoriteWithMovieDto;
import com.alibou.security.feature.favorite.model.Favorite;
import com.alibou.security.feature.favorite.service.FavoriteService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@Validated
@RequestMapping("/v1/favorite")
public class favoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/likeFavorite")
    public ResponseEntity<?> likeFavorite(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("email") String email,
            @RequestParam("movieCode") String movieCode,
            HttpServletRequest request
    ){
        Long start = System.currentTimeMillis();

        log.info("CHECK EMAIL: {} , MOVIE CODE: {}", email, movieCode);

        Favorite favorite = favoriteService.likeFavorite(email, movieCode);

        FavoriteDto favoriteDto = new FavoriteDto(
                favorite.getId(),
                favorite.getMovieCode(),
                favorite.getEmail(),
                favorite.getActive(),
                favorite.getFavoriteDate(),
                favorite.getUnfavoriteDate()
        );
        favoriteService.clearAllFavoriteCache();
        Long t = System.currentTimeMillis() - start;
        log.info( "|END|Executime=" + t);
        SuccessResDto data = SuccessResDto.builder()
                .message("Like Success")
                .data(favoriteDto)
                .build();
        return ResponseEntity.ok().body(data);
    }

    @PostMapping("/unlikeFavorite")
    public ResponseEntity<?> unlikeFavorite(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("email") String email,
            @RequestParam("movieCode") String movieCode,
            HttpServletRequest request
    ){
        Long start = System.currentTimeMillis();

        log.info("CHECK EMAIL: {} , MOVIE CODE: {}", email, movieCode);

        Favorite favorite = favoriteService.unlikeFavorite(email, movieCode);

        FavoriteDto favoriteDto = new FavoriteDto(
                favorite.getId(),
                favorite.getMovieCode(),
                favorite.getEmail(),
                favorite.getActive(),
                favorite.getFavoriteDate(),
                favorite.getUnfavoriteDate()
        );

        favoriteService.clearAllFavoriteCache();

        Long t = System.currentTimeMillis() - start;
        log.info( "|END|Executime=" + t);
        SuccessResDto data = SuccessResDto.builder()
                .message("unLike Success")
                .data(favoriteDto)
                .build();
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/checkFavorite")
    public ResponseEntity<?> checkFavorite(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("email") String email,
            @RequestParam("movieCode") String movieCode,
            HttpServletRequest request
    ){
        Long start = System.currentTimeMillis();
        Favorite favorite = favoriteService.getFavoriteByEmail(email, movieCode);

        FavoriteDto favoriteDto = new FavoriteDto(
                favorite.getId(),
                favorite.getMovieCode(),
                favorite.getEmail(),
                favorite.getActive(),
                favorite.getFavoriteDate(),
                favorite.getUnfavoriteDate()
        );

        Long t = System.currentTimeMillis() - start;
        log.info( "|END|Executime=" + t);
        SuccessResDto data = SuccessResDto.builder()
                .message("Success")
                .data(favoriteDto)
                .build();
        return ResponseEntity.ok().body(data);
    }

    @GetMapping("/playlistFavorite")
    public ResponseEntity<List<FavoriteWithMovieDto>> playlistFavorite(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("email") String email,
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            HttpServletRequest request
    ) {
        Long start = System.currentTimeMillis();
        log.info("CHECK EMAIL: {} ", email);

        List<FavoriteWithMovieDto> favorites = favoriteService.getPlayListFavoriteByEmailAndMovieCode(email, page, size);

        if (favorites == null || favorites.isEmpty()) {
            log.info("[favorites]: No favorites found");
            return ResponseEntity.noContent().build();
        }

        List<FavoriteWithMovieDto> favoriteWithMovieDtos = favorites.stream()
                .map(fav -> new FavoriteWithMovieDto(
                        fav.getActive(),
                        fav.getEmail(),
                        fav.getMovieCode(),
                        fav.getFavoriteDate(),
                        fav.getUnfavoriteDate(),
                        fav.getMovieName(),
                        fav.getDescription(),
                        fav.getReleaseDate(),
                        fav.getMovieGenre(),
                        fav.getIsHot(),
                        fav.getImageUrl()
                ))
                .collect(Collectors.toList());

        Long t = System.currentTimeMillis() - start;
        log.info("|END| Execution time = " + t);

        return ResponseEntity.ok(favoriteWithMovieDtos);
    }

    @DeleteMapping("/clearAllCacheFavorites")
    public ResponseEntity<String> clearAllCacheFavorites() {
        try {
            // Xóa cache cho tất cả các user
            favoriteService.clearAllFavoriteCache();
            return ResponseEntity.ok("All favorite cache cleared!");
        } catch (Exception e) {
            log.error("Error clearing cache: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to clear favorite cache!");
        }
    }

}
