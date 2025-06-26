package com.plotech.itsrvsys.service;

import com.plotech.itsrvsys.pojo.entity.User;

public interface UserService {

    /**
     * 验证用户是否有登录权限或某功能权限
     * @param user 用户实体类
     * @return true：有权限；false：无权限
     */
    boolean authConfirm(User user);
}
