package com.plotech.kanban.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.plotech.kanban.pojo.entity.User;

/**
 * @author OF00047
 * @description 针对表【user】的数据库操作Service
 * @createDate 2025-01-08 16:01:29
 */
public interface UserService extends IService<User> {
    /**
     * 保存新用户。
     *
     * @param user 要保存的用户信息
     * @return 是否保存成功
     */
    boolean saveUser(User user);

    /**
     * 验证用户是否存在。
     *
     * @param user 用户信息
     * @return 是否验证成功
     */
    boolean existUser(User user);
}
