package com.sky.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sky.context.BaseContext;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.sky.constant.AutoFillConstant.*;

/**
 * MP自动填充
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        setFieldValByName(CREATE_TIME, LocalDateTime.now(), metaObject);
        setFieldValByName(UPDATE_TIME, LocalDateTime.now(), metaObject);
        setFieldValByName(CREATE_USER, BaseContext.getCurrentId(), metaObject);
        setFieldValByName(UPDATE_USER, BaseContext.getCurrentId(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName(UPDATE_TIME, LocalDateTime.now(), metaObject);
        setFieldValByName(UPDATE_USER, BaseContext.getCurrentId(), metaObject);
    }
}
