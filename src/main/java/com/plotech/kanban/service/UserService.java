package com.plotech.kanban.service;

import com.plotech.kanban.pojo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author OF00047
* @description 针对表【user】的数据库操作Service
* @createDate 2025-01-08 16:01:29
*/
public interface UserService extends IService<User> {
    boolean saveUser(User user);
    boolean existUser(User user);
}
