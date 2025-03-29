package com.alibou.security.feature.episode.dao;

import com.alibou.security.feature.episode.model.Episode;

import java.util.List;

public interface EpisodeDao {
    public List<Episode> getAllEpisodesByMovieCode(String movieCode);
}