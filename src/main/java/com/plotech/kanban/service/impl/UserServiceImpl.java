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
 * @author OF00047
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2025-01-08 16:01:29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Override
    public boolean saveUser(User user) {
        String username = user.getUsername();
        if (StringUtils.isBlank(username)) {
            throw new CommonBaseException(CommonBaseErrorCode.USERNAME_ISNULL);
        }
        if (this.getOne(new QueryWrapper<User>().eq("username", username)) != null) {
            throw new CommonBaseException(CommonBaseErrorCode.USER_EXIST);
        }
        user.setPassword(SaltMD5Util.generateSaltPassword(user.getPassword()));
        user.setStatus(0);
        return save(user);
    }

    @Override
    public boolean existUser(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        if (StringUtils.isBlank(username)) {
            throw new CommonBaseException(CommonBaseErrorCode.USERNAME_ISNULL);
        }
        User userFromDB = this.getOne(new QueryWrapper<User>().eq("username", username).eq("status", 0));
        if (userFromDB == null) {
            throw new CommonBaseException(CommonBaseErrorCode.USER_NOT_EXIST);
        }
        if (!SaltMD5Util.verifySaltPassword(password, userFromDB.getPassword())) {
            throw new CommonBaseException(CommonBaseErrorCode.USER_PASSWORD_ERROR);
        } else {
            return true;
        }
    }
}




