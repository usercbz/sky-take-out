package com.sky.controller.user;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import static com.sky.constant.RedisConstant.STATUS_KEY;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
public class ShopController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("status")
    public Result<Integer> getStatus() {
        String shopStatus = redisTemplate.opsForValue().get(STATUS_KEY);
        assert shopStatus != null;
        return Result.success(Integer.valueOf(shopStatus));
    }

}
