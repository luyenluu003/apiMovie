package com.alibou.security.feature.user.service;

import com.alibou.security.feature.user.dao.UserDao;
import com.alibou.security.feature.user.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Log4j2
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;


    public void activateVip(String userId, Integer days, String vip){
        User user = userDao.userFindByuserId(userId);
        if (user == null) {
            log.error("User not found for userId: {}", userId);
            throw new RuntimeException("User not found");
        }
        Date startDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_YEAR, days);
        Date endDate = calendar.getTime();
        user.setVipStartDate(startDate);
        user.setVipEndDate(endDate);
        user.setVipLevel(vip);
        userDao.updateUserVip(user);

    }


    public Boolean isVipActive(String userId){
        User user = userDao.userFindByuserId(userId);
        if (user == null) {
            log.error("User not found for userId: {}", userId);
            throw new RuntimeException("User not found");
        }
        if (user.getVipStartDate() == null || user.getVipEndDate() == null) {
            return false;
        }
        Date now = new Date();
        return now.after(user.getVipStartDate()) && now.before(user.getVipEndDate());
    }
}
