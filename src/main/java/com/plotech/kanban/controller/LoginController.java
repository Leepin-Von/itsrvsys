package com.plotech.kanban.controller;

import com.plotech.kanban.pojo.entity.User;
import com.plotech.kanban.pojo.vo.R;
import com.plotech.kanban.service.UserService;
import com.plotech.kanban.util.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录，用于处理用户登录和注册请求。
 */
@RestController
@RequestMapping("/api")
public class LoginController {
    /**
     * 用户服务实例，用于处理用户相关的业务逻辑。
     */
    @Resource
    private UserService userService;
    /**
     * JWT工具实例，用于生成和验证JWT令牌。
     */
    @Resource
    private JwtUtil jwtUtil;

    /**
     * 注册新用户。
     *
     * @param user 要注册的用户信息
     * @return 注册结果
     */
    @PostMapping("/signUp")
    public R signUp(@RequestBody User user) {
        // 保存用户信息
        userService.saveUser(user);
        return R.ok();
    }

    /**
     * 用户登录。
     *
     * @param user 用户登录信息
     * @return 登录结果，包括JWT令牌
     */
    @PostMapping("/signIn")
    public R signIn(@RequestBody User user) {
        // 验证用户是否存在
        userService.existUser(user);
        Map<String, Object> token = new HashMap<>();
        // 将用户信息放入令牌中
        token.put("token", user);
        // 生成JWT令牌
        return R.ok(jwtUtil.generateJwt(token));
    }
}