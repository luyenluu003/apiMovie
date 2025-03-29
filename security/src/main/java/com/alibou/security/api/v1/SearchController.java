package com.alibou.security.api.v1;

import com.alibou.security.feature.search.model.ResultItem;
import com.alibou.security.feature.search.service.SearchService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;

@RestController
@Log4j2
@Validated
@RequestMapping("/v1/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping(value = "/group")
    @ResponseBody
    public ResponseEntity<?> searchByGroup(
            @RequestHeader("Accept-language") @NotBlank String lang,
            @RequestParam("userId") @NotBlank String userId,
//            @RequestHeader("token") @NotBlank String token,
            // Param
            @RequestParam("timestamp") long timestamp,
            @RequestParam("groupType") @NotBlank String groupType,
            @RequestParam("qrTxt") @NotBlank String qrTxt,
            @RequestParam("page") @Min(0) Integer page,
            @RequestParam("size") @Min(1) @Max(100) Integer size
    ) {
        Long start = System.currentTimeMillis();

        List<ResultItem> list;
        switch (groupType) {
            case "movie":
                list = searchService.searchByMovie(userId, qrTxt, page, size, timestamp);
                break;
            case "actor":
                list = searchService.searchByActor(userId, qrTxt, page, size, timestamp);
                break;
            case "category":
                list = searchService.searchByCategory(userId, qrTxt, page, size, timestamp);
                break;
            default:
                throw new InvalidParameterException("Invalid groupType: " + groupType);
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("data", list);

        Long stop = System.currentTimeMillis();
        long time = stop - start;
        log.info("request searchByGroup: " + "  " + "|qrTxt:" + qrTxt + "|groupType:" + groupType + "|userId: " + userId + "|executeTime: " + time);
        return ResponseEntity.ok().body(data);
    }
}
