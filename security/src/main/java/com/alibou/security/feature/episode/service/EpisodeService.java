package com.alibou.security.feature.episode.service;

import com.alibou.security.api.v1.dto.episode.EpisodeDto;

import java.util.List;

public interface EpisodeService {
    List<EpisodeDto> findAllEpisodesByMovieCode(String userId, String movieCode);
}
