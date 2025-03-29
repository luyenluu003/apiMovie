package com.alibou.security.feature.category.dao;
import com.alibou.security.feature.category.model.Category;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Log4j2
@Repository
public class CategoryDaoImpl implements CategoryDao {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Category> getAllCategoryByCategoryId(String categoryId) {
        try{
            String sql = String.format(
                    "SELECT category_code, name, description "+
                            "FROM category " +
                            "WHERE category_code = :categoryId"
            );

            SqlParameterSource params = new MapSqlParameterSource("categoryId", categoryId);

            return jdbcTemplate.query(sql, params, (rs, rowNum) -> Category.builder()
                    .categoryCode(rs.getString("category_code"))
                    .description(rs.getString("description"))
                    .name(rs.getString("name"))
                    .build());
        }catch (Exception e){
            log.error("Error while getting category list", e);
        }
        return Collections.emptyList();
    }

    @Override
    public Category getCategoryByCategoryId(String categoryId){
        try{
            String sql = "SELECT category_code, description, name " +
                    "FROM category " +
                    "WHERE category_code = :categoryId";

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("categoryId", categoryId);

            return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) ->
                    Category.builder()
                            .categoryCode(rs.getString("category_code"))
                            .description(rs.getString("description"))
                            .name(rs.getString("name"))
                            .build()
            );
        } catch (EmptyResultDataAccessException e) {
            log.warn("Category not found with categoryId: {}", categoryId);
        } catch (Exception e) {
            log.error("Error while getting category: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Category> getCategoryByListId(List<String> listId) {
        try{
            List<Category> listCategory = new ArrayList<>();
            for(String id : listId){
                Category category = getCategoryByCategoryId(id);
                if(category !=null ){
                    listCategory.add(category);
                }
            }
            return listCategory;
        } catch (Exception e){
            log.error("Error retrieving category: {}", e.getMessage(), e);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Category> getCategoryAll() {
        String sql = "SELECT * FROM category WHERE status = 1";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> Category.builder()
                    .id(rs.getLong("id"))
                    .categoryCode(rs.getString("category_code"))
                    .description(rs.getString("description"))
                    .name(rs.getString("name"))
                    .build());
        } catch (Exception e) {
            log.error("Error retrieving category: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }

}
