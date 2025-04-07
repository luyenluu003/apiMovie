package com.alibou.security.api.v1.payment;

import com.alibou.security.api.v1.dto.SuccessResDto;
import com.alibou.security.api.v1.dto.movie.MovieDto;
import com.alibou.security.feature.transaction.dao.TransactionDao;
import com.alibou.security.feature.transaction.model.MovieTransaction;
import com.alibou.security.feature.transaction.service.PayPalService;
import com.alibou.security.feature.user.dao.UserDao;
import com.alibou.security.feature.user.model.User;
import com.alibou.security.feature.user.service.UserService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/v1/payment")
public class PaymentController {

    @Autowired
    private PayPalService payPalService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TransactionDao transactionDao;

    @GetMapping("/check-vip")
    public String checkVipStatus(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("userId") String userId,
            HttpServletRequest request
    ) {
        // Sửa lại log để hiển thị userId
        log.info("CHECK USERID CO KHONG ===> {}", userId);

        User user = userDao.userFindByuserId(userId);
        if (user == null) {
            log.error("USER NOT FOUND: {}", userId); // Cũng thêm placeholder cho log.error
            return "Không tìm thấy người dùng!";
        }

        log.info("CHECK USER =>> {}", user.toString());

        Date currentDate = new Date();
        Date vipEndDate = user.getVipEndDate();
        Date vipStartDate = user.getVipStartDate();

        // Kiểm tra và log chi tiết để debug
        log.info("Checking VIP status for user {}: currentDate={}, vipEndDate={}, vipStartDate={}", userId, currentDate, vipEndDate, vipStartDate);

        // Kiểm tra xem VIP còn hạn không
        boolean isVipActive = vipEndDate != null && vipEndDate.compareTo(currentDate) > 0;
        if (isVipActive) {
            log.info("User {} has active VIP until {}", userId, vipEndDate);
            return "Bạn đã có gói VIP còn hạn đến " + vipEndDate + ". Bạn có muốn gia hạn không?";
        } else {
            if (vipEndDate == null) {
                log.info("User {} has no VIP (vipEndDate is null)", userId);
            } else {
                log.info("User {} VIP expired on {}", userId, vipEndDate);
            }
            return "Bạn chưa có gói VIP hoặc đã hết hạn. Tiến hành thanh toán để kích hoạt!";
        }
    }

    @GetMapping("/check-vip-user")
    public ResponseEntity<?> checkVipUser(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("userId") String userId,
            HttpServletRequest request
    ) {
        Long start = System.currentTimeMillis();

        User user = userDao.userFindByuserId(userId);
        if (user == null) {
            log.error("USER NOT FOUND: {}", userId);
            return ResponseEntity.noContent().build();
        }

        MovieTransaction movieTransaction = transactionDao.getTransactionByUserId(userId);


        if (movieTransaction == null ) {
            log.info("[movieTransaction]:" + "No movieTransaction found");
            return ResponseEntity.noContent().build();
        }
        Long t = System.currentTimeMillis() - start;
        log.info("[movieTransaction]:userId=" + userId + "|END|Executime=" + t);
        SuccessResDto data = SuccessResDto.builder()
                .message("Detail Movie Success")
                .data(movieTransaction)
                .build();
        return ResponseEntity.ok().body(data);
    }

    @PostMapping("/create")
    public String createPayment(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("total") Double total,
            @RequestParam("userId") String userId,
            @RequestParam("packageId") String packageId) {
        try {
            String cancelUrl = "http://localhost:5173/cancel";
            String successUrl = "http://localhost:5173/success?userId=" + userId + "&packageId=" + packageId;
            Payment payment = payPalService.createPayment(
                    total,
                    "USD",
                    "Thanh toán gói VIP xem phim",
                    cancelUrl,
                    successUrl
            );
            log.info("Created payment with ID: {}", payment.getId());
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    log.info("Redirecting to PayPal approval URL: {}", link.getHref());
                    return link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error("Error creating payment", e);
            e.printStackTrace();
        }
        return "Error";
    }

    @GetMapping("/success")
    public String executePayment(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            @RequestParam("userId") String userId,
            @RequestParam("packageId") String packageId) {
        try {
            // Thực thi thanh toán và lưu giao dịch trong PayPalService
            Payment payment = payPalService.executePayment(paymentId, payerId, userId, packageId);

            if (!payment.getState().equals("approved")) {
                log.warn("Payment {} not approved by PayPal", paymentId);
                return "Thanh toán không được phê duyệt!";
            }

            // Lấy thông tin user
            User user = userDao.userFindByuserId(userId);
            if (user == null) {
                log.error("User {} not found", userId);
                return "Không tìm thấy người dùng!";
            }

            // Xác định số ngày VIP
            int days;
            String vip;
            switch (packageId) {
                case "VIP_1M":
                    days = 30;
                    vip = "1";
                    break;
                case "VIP_3M":
                    days = 90;
                    vip = "2";
                    break;
                case "VIP_YEAR":
                    days = 365;
                    vip = "3";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid packageId");
            }

            // Gia hạn VIP
            log.info("Extending VIP for user {} with package {} for {} days", userId, packageId, days);
            userService.activateVip(userId,days,vip);

            return "Thanh toán thành công! Gói VIP đã được gia hạn.";
        } catch (PayPalRESTException e) {
            log.error("Error executing payment: {}", e.getMessage());
            if (e.getMessage().contains("PAYMENT_ALREADY_DONE")) {
                return "Thanh toán đã được thực hiện trước đó!";
            }
            e.printStackTrace();
            return "Thanh toán thất bại: " + e.getMessage();
        }
    }

    @GetMapping("/allTransition/user")
    @ResponseBody
    public ResponseEntity<?> getMovieByCategoryVip(
            @RequestHeader("Accept-language") String lang,
            @RequestParam("userId") String userId,
            @RequestParam("page") Integer page,
            @RequestParam("pageSize") Integer pageSize,
            HttpServletRequest request
    ){
        Long start = System.currentTimeMillis();
        log.info("[TRANSACTION]:" + "userId=" + userId);

        List<MovieTransaction> movieTransactions = transactionDao.getAllTransactionByUserId(userId, page, pageSize);

        // Kiểm tra nếu không có dữ liệu
        if (movieTransactions == null || movieTransactions.isEmpty()) {
            log.info("[TRANSACTION]:" + "No TRANSACTION found");
            return ResponseEntity.noContent().build();
        }
        Long t = System.currentTimeMillis() - start;
        log.info("[TRANSACTION]:userId=" + userId + "|END|Executime=" + t);
        return ResponseEntity.ok(movieTransactions);
    }
}