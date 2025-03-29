package com.alibou.security.feature.common.service;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class MediaServiceImpl implements MediaService {

    @Value("${media.domain.image")
    private String mediaDomainImage;


    @Override
    public String getUserAvatar(String userId, Long lastAvatar) {
        if(lastAvatar==null){
            lastAvatar = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH).getTime();
        }
        String uPath = StringUtils.join(userId.split(""),"/");
        return String.format("%s/vcs_medias/user/%s/avatar_%s.jpg?t=$s", mediaDomainImage, uPath, userId, lastAvatar);
    }
}
