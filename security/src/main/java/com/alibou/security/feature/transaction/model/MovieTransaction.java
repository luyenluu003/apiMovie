package com.alibou.security.feature.transaction.model;

import com.alibou.security.feature.user.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieTransaction implements Serializable {
    private static final long serialVersionUID = 1142069820653687126L;
    private Long id;
    private String userId;
    private String packageId;
    private Double amount;
    private String currency; //tiền tệ
    private Date transactionDate; //Ngày giao dịch
    private String paymentMethod; //Phương thức thanh toán
    private String status;
    private String paymentId;
    private User user;

}
