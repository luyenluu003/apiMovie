package com.alibou.security.feature.chatbot.model;

import com.alibou.security.api.v1.dto.movie.MovieDto;
import com.alibou.security.feature.movie.model.Movie;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatbotResponse implements Serializable {

    private static final long serialVersionUID = 4444463620012687991L;
    private String message;
    private List<Movie> recommendedMovies;
}
