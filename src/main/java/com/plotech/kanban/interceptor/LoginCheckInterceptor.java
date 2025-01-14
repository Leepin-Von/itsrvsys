package com.plotech.kanban.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.plotech.kanban.exception.CommonBaseErrorCode;
import com.plotech.kanban.pojo.vo.R;
import com.plotech.kanban.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURL().toString();
        if (url.contains("signIn") || url.contains("signUp")) {
            return true;
        }
        String token = request.getHeader("token");
        if (!StringUtils.hasLength(token)) {
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            String noPermission = JSONObject.toJSONString(R.err(CommonBaseErrorCode.USER_NOT_PERMISSION));
            response.getWriter().write(noPermission);
            return false;
        }
        try {
            jwtUtil.parseJwt(token);
        } catch (Exception e) {
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            String noPermission = JSONObject.toJSONString(R.err(CommonBaseErrorCode.USER_NOT_PERMISSION));
            response.getWriter().write(noPermission);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
