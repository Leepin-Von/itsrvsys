package com.plotech.kanban.controller;

import com.plotech.kanban.pojo.entity.User;
import com.plotech.kanban.pojo.vo.R;
import com.plotech.kanban.service.UserService;
import com.plotech.kanban.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录，用于处理用户登录和注册请求。
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class SignInController {
    /**
     * 用户服务实例，用于处理用户相关的业务逻辑。
     */
    private final UserService userService;
    /**
     * JWT工具实例，用于生成和验证JWT令牌。
     */
    private final JwtUtil jwtUtil;

    /**
     * 用户登录。
     *
     * @param user 用户登录信息
     * @return 登录结果，包括JWT令牌
     */
    @PostMapping("/signIn")
    public R signIn(@RequestBody User user) {
        userService.authConfirm(user);
        Map<String, Object> userInfo = new HashMap<>();
        // 将用户信息放入令牌中
        userInfo.put("username",user.getUsername());
        userInfo.put("signInTime",new Date());
        // 生成JWT令牌
        return R.ok(jwtUtil.generateJwt(userInfo));
    }
}