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

@RestController
@RequestMapping("/api")
public class LoginController {
    @Resource
    private UserService userService;
    @Resource
    private JwtUtil jwtUtil;

    @PostMapping("/signUp")
    public R signUp(@RequestBody User user) {
        userService.saveUser(user);
        return R.ok();
    }

    @PostMapping("/signIn")
    public R signIn(@RequestBody User user) {
        userService.existUser(user);
        Map<String, Object> token = new HashMap<>();
        token.put("token",user);
        return R.ok(jwtUtil.generateJwt(token));
    }
}
