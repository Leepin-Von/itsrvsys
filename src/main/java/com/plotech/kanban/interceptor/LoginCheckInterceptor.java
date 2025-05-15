package com.plotech.kanban.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.plotech.kanban.exception.CommonBaseErrorCode;
import com.plotech.kanban.pojo.vo.R;
import com.plotech.kanban.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录检查拦截器，用于检查用户是否登录并验证JWT令牌。
 */
@Slf4j
@Component
@AllArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    /**
     * JWT工具实例，用于解析和验证JWT令牌。
     */
    private final JwtUtil jwtUtil;

    /**
     * 在请求处理之前进行拦截。
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param handler  处理器
     * @return 是否继续处理请求
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求URL
        String url = request.getRequestURL().toString();
        // 如果是登录请求，则直接放行
        if (url.contains("signIn")) {
            return true;
        }
        // 积木报表内置了Interceptor，由它自己处理，这里就直接放行
        if (url.contains("jmreport")) {
            return true;
        }
        // 积木BI同上
        if (url.contains("drag") || url.contains("jimubi")) {
            return true;
        }
        if(url.contains("approval")) {
            return true;
        }
        // 获取请求头中的token
        String token = request.getHeader("token");
        log.info("请求URL --> 【{}】", url);
        log.info("token == null? --> 【{}】", token == null);
        // 如果token为空，则返回401未授权
        if (!StringUtils.hasLength(token)) {
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            String noPermission = JSONObject.toJSONString(R.err(CommonBaseErrorCode.USER_NOT_PERMISSION));
            response.getWriter().write(noPermission);
            return false;
        }
        try {
            // 解析JWT令牌
            jwtUtil.parseJwt(token);
        } catch (Exception e) {
            // 如果解析失败，则返回401未授权
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            String noPermission = JSONObject.toJSONString(R.err(CommonBaseErrorCode.USER_NOT_PERMISSION));
            response.getWriter().write(noPermission);
            return false;
        }
        // 如果验证通过，则继续处理请求
        return true;
    }

    /**
     * 在请求处理之后进行拦截。
     *
     * @param request      请求对象
     * @param response     响应对象
     * @param handler      处理器
     * @param modelAndView 模型和视图
     * @throws Exception 异常
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 在请求完成之后进行拦截。
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param handler  处理器
     * @param ex       异常
     * @throws Exception 异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}