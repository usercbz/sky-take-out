package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.result.Result;
import com.sky.vo.UserLoginVO;

import java.time.LocalDateTime;
import java.util.List;

public interface UserService extends IService<User> {
    /**
     * 用户微信登录
     * @param loginDTO 登录信息 code
     * @return 用户id token openid
     */
    Result<UserLoginVO> login(UserLoginDTO loginDTO);

    /**
     * 根据创建时间获取用户
     * @param begin
     * @param end
     * @return
     */
    List<User> getUsersByTime(LocalDateTime begin, LocalDateTime end);

}
