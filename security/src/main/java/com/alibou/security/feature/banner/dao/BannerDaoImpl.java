package com.alibou.security.feature.banner.dao;

import com.alibou.security.api.v1.dto.banner.BannerDto;
import com.alibou.security.feature.banner.model.Banner;
import com.alibou.security.feature.category.model.Category;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Repository
@Log4j2
public class BannerDaoImpl implements BannerDao{

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<BannerDto> getAllBanners(LocalDate currentDate) {
        try{
            String sql = "SELECT  banner_image, end_date, movie_banner_code, position, start_date, title " +
                    "FROM mov_banner " +
                    "WHERE status =1 " +
                    "AND :currentDate BETWEEN start_date AND end_date " +
                    "ORDER BY position ASC";

            SqlParameterSource params = new MapSqlParameterSource("currentDate", currentDate);
            return jdbcTemplate.query(sql, params, (rs, rowNum) -> BannerDto.builder()
                    .bannerImage(rs.getString("banner_image"))
                    .endDate(rs.getDate("end_date"))
                    .movieBannerCode(rs.getString("movie_banner_code"))
                    .position(rs.getInt("position"))
                    .title(rs.getString("title"))
                    .startDate(rs.getDate("start_date"))
                    .build());
        }catch (Exception e){
            log.error("Error while getting banner list", e);
        }
        return Collections.emptyList();
    }
}
