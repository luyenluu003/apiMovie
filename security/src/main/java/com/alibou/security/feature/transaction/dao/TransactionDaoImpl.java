package com.alibou.security.feature.transaction.dao;

import com.alibou.security.feature.transaction.model.MovieTransaction;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
public class TransactionDaoImpl implements TransactionDao{

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Integer saveTransaction(MovieTransaction transaction){
        try{
            String sql = "INSERT INTO movie_transaction (amount, currency, package_id, " +
                    "payment_method, status, transaction_date, user_id, payment_id) " +
                    "VALUES (:amount, :currency, :package_id, " +
                    ":payment_method, :status, :transaction_date, :user_id, :payment_id) ";
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("amount", transaction.getAmount())
                    .addValue("currency", transaction.getCurrency())
                    .addValue("package_id", transaction.getPackageId())
                    .addValue("payment_method",transaction.getPaymentMethod())
                    .addValue("status",transaction.getStatus())
                    .addValue("transaction_date",transaction.getTransactionDate())
                    .addValue("user_id",transaction.getUserId())
                    .addValue("payment_id",transaction.getPaymentId());

            return jdbcTemplate.update(sql, params);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return null;
    }

    @Override
    public MovieTransaction findByPaymentId(String paymentId){
        try {
            String sql = "SELECT * " +
                    "FROM movie_transaction " +
                    "WHERE payment_id = :paymentId ";

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("paymentId", paymentId);

            return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) ->
                    MovieTransaction.builder()
                            .amount(rs.getDouble("amount"))
                            .currency(rs.getString("currency"))
                            .packageId(rs.getString("package_id"))
                            .paymentId(rs.getString("payment_id"))
                            .paymentMethod(rs.getString("payment_method"))
                            .status(rs.getString("status"))
                            .transactionDate(rs.getDate("transaction_date"))
                            .userId(rs.getString("user_id"))
                            .build()
            );

        } catch (EmptyResultDataAccessException e) {
            log.warn("Transaction not found with id: {}", paymentId);
        } catch (Exception e) {
            log.error("Error retrieving Transaction: {}", e.getMessage(), e);
        }
        return null;
    }

    public MovieTransaction getTransactionByUserId(String userId){
        try {
            String sql = "SELECT * " +
                    "FROM movie_transaction " +
                    "WHERE user_id = :userId ";

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("userId", userId);

            return jdbcTemplate.queryForObject(sql, params, (rs, rowNum) ->
                    MovieTransaction.builder()
                            .amount(rs.getDouble("amount"))
                            .currency(rs.getString("currency"))
                            .packageId(rs.getString("package_id"))
                            .paymentId(rs.getString("payment_id"))
                            .paymentMethod(rs.getString("payment_method"))
                            .status(rs.getString("status"))
                            .transactionDate(rs.getDate("transaction_date"))
                            .userId(rs.getString("user_id"))
                            .build()
            );

        } catch (EmptyResultDataAccessException e) {
            log.warn("Transaction not found with id: {}", userId);
        } catch (Exception e) {
            log.error("Error retrieving Transaction: {}", e.getMessage(), e);
        }
        return null;
    }
}
