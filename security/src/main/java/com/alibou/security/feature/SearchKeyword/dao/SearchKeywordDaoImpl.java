package com.alibou.security.feature.SearchKeyword.dao;

import com.alibou.security.feature.SearchKeyword.model.Searchkeyword;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Log4j2
@Repository
public class SearchKeywordDaoImpl implements SearchkeywordDao{

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Searchkeyword> getAllSearchKeyword(){
        try{
            String sql = String.format(
                    "SELECT id, description, keyword " +
                            "FROM search_keyword " +
                            "WHERE status = 1"
            );

            SqlParameterSource sqlParameterSource = new MapSqlParameterSource();

            return jdbcTemplate.query(sql, sqlParameterSource, (rs, rowNum) -> Searchkeyword.builder()
                    .id(rs.getLong("id"))
                    .description(rs.getString("description"))
                    .keyword(rs.getString("keyword"))
            .build());

        }catch (Exception e) {
            log.error("Error retrieving searchkeyword: {}", e.getMessage(), e);
        }
        return Collections.emptyList();
    }
}
