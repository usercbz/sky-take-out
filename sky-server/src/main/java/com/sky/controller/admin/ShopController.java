package com.sky.controller.admin;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import static com.sky.constant.RedisConstant.STATUS_KEY;

@RestController
@RequestMapping("/admin/shop")
@Slf4j
public class ShopController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PutMapping("/{status}")
    public Result<String> setStatus(@PathVariable Integer status) {
        redisTemplate.opsForValue().set(STATUS_KEY, String.valueOf(status));
        return Result.success();
    }

    @GetMapping("status")
    public Result<Integer> getStatus() {
        String shopStatus = redisTemplate.opsForValue().get(STATUS_KEY);
        assert shopStatus != null;
        return Result.success(Integer.valueOf(shopStatus));
    }

}
