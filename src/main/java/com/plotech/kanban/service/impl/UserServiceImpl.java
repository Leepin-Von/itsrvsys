package com.plotech.kanban.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plotech.kanban.exception.CommonBaseErrorCode;
import com.plotech.kanban.exception.CommonBaseException;
import com.plotech.kanban.mapper.UserMapper;
import com.plotech.kanban.pojo.entity.User;
import com.plotech.kanban.service.UserService;
import com.plotech.kanban.util.SaltMD5Util;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类，用于处理用户相关的业务逻辑。
 *
 * @author OF00047
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2025-01-08 16:01:29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Override
    public boolean saveUser(User user) {
        // 获取用户名
        String username = user.getUsername();
        // 如果用户名为空，则抛出异常
        if (StringUtils.isBlank(username)) {
            throw new CommonBaseException(CommonBaseErrorCode.USERNAME_ISNULL);
        }
        // 如果用户已存在，则抛出异常
        if (this.getOne(new QueryWrapper<User>().eq("username", username)) != null) {
            throw new CommonBaseException(CommonBaseErrorCode.USER_EXIST);
        }
        // 对密码进行加密处理
        user.setPassword(SaltMD5Util.generateSaltPassword(user.getPassword()));
        // 设置用户状态为0（启用）
        user.setStatus(0);
        // 保存用户信息
        return save(user);
    }

    @Override
    public boolean existUser(User user) {
        // 获取用户名
        String username = user.getUsername();
        // 获取密码
        String password = user.getPassword();
        // 如果用户名为空，则抛出异常
        if (StringUtils.isBlank(username)) {
            throw new CommonBaseException(CommonBaseErrorCode.USERNAME_ISNULL);
        }
        // 查询用户信息
        User userFromDB = this.getOne(new QueryWrapper<User>().eq("username", username).eq("status", 0));
        // 如果用户不存在，则抛出异常
        if (userFromDB == null) {
            throw new CommonBaseException(CommonBaseErrorCode.USER_NOT_EXIST);
        }
        // 验证密码是否正确
        if (!SaltMD5Util.verifySaltPassword(password, userFromDB.getPassword())) {
            throw new CommonBaseException(CommonBaseErrorCode.USER_PASSWORD_ERROR);
        } else {
            return true;
        }
    }
}