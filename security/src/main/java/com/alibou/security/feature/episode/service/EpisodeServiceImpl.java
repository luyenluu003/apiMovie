package com.alibou.security.feature.episode.service;

import com.alibou.security.api.v1.dto.episode.EpisodeDto;
import com.alibou.security.api.v1.dto.movie.MovieDto;
import com.alibou.security.feature.episode.dao.EpisodeDao;
import com.alibou.security.feature.episode.model.Episode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@CacheConfig(cacheManager = "cacheManager3Hours")
public class EpisodeServiceImpl implements EpisodeService {

    @Autowired
    private EpisodeDao episodeDao;

    @Cacheable(value = "episodes", key = "'list-episodes:'.concat(#userId).concat(#movieCode)")
    public List<EpisodeDto> findAllEpisodesByMovieCode(String userId, String movieCode) {
        log.info("Retrieving episodes for series with movieCode={}", movieCode);

        List<Episode> episodes = episodeDao.getAllEpisodesByMovieCode(movieCode);

        if(episodes == null || episodes.isEmpty()) {
            return Collections.emptyList();
        }

        return episodes.stream().map(episode ->
                EpisodeDto.builder()
                        .id(episode.getId())
                        .duration(episode.getDuration())
                        .description(episode.getDescription())
                        .episodeNumber(episode.getEpisodeNumber())
                        .releaseDate(episode.getReleaseDate())
                        .videoUrl(episode.getVideoUrl())
                        .build()
        ).collect(Collectors.toList());
    }
}
