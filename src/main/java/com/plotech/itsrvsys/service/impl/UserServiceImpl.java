package com.plotech.itsrvsys.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.plotech.itsrvsys.exception.CommonBaseException;
import com.plotech.itsrvsys.pojo.entity.User;
import com.plotech.itsrvsys.pojo.vo.TransferDataRequest;
import com.plotech.itsrvsys.pojo.vo.TransferDataResponse;
import com.plotech.itsrvsys.service.UserService;
import com.plotech.itsrvsys.util.SaltUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    /**
     * RestTemplate实例，用于发送HTTP请求。
     */
    private final RestTemplate restTemplate;
    @Value("${current.env}")
    private String currentEnv;
    @Value("${dev.api}")
    private String devApi;
    @Value("${prod.api}")
    private String prodApi;
    private final SaltUtil saltUtil;

    public UserServiceImpl(RestTemplate restTemplate, SaltUtil saltUtil) {
        this.restTemplate = restTemplate;
        this.saltUtil = saltUtil;
    }

    private String getTargetUrl() {
        if ("dev".equalsIgnoreCase(currentEnv)) {
            return devApi;
        } else {
            return prodApi;
        }
    }

    @Override
    public boolean authConfirm(User user) {
        if ("TEST".equals(user.getUsername())) {
            // 测试账号直接通过
            return true;
        }
        // 生成盐
        String salt = saltUtil.generateSalt();
        // 对密码进行加盐MD5加密处理
        String password = user.getPassword();
        if (StringUtils.hasText(password)) {
            // 是否有登入系统权限
            user.setPassword(DigestUtil.md5Hex(user.getPassword().toUpperCase() + salt));
        } else {
            // 是否有某功能权限
            user.setPassword("");
        }
        // 目标路径
        String targetUrl = getTargetUrl();
        // 请求体
        TransferDataRequest request = new TransferDataRequest();
        request.setDocType("ChkUserPermissions");
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("UserId", user.getUsername());
        parameters.put("PWD", user.getPassword());
        parameters.put("ItemId", user.getItemId());
        parameters.put("CheckNum", salt);
        request.setParameters(parameters);
        // 请求头
        HttpHeaders headers = new HttpHeaders();
        // 设置请求头内容类型为JSON
        headers.setContentType(MediaType.valueOf("application/json;charset=UTF-8"));
        HttpEntity<TransferDataRequest> httpEntity = new HttpEntity<>(request, headers);
        log.info("访问【{}】的请求体：【{}】", targetUrl, JSONObject.toJSONString(httpEntity));
        TransferDataResponse response = restTemplate.postForObject(targetUrl, httpEntity, TransferDataResponse.class);
        assert response != null;
        log.info("请求【{}】返回的响应体：【{}】", targetUrl, JSONObject.toJSONString(response));
        if (response.getState().equals("OK")) {
            return true;
        } else {
            throw new CommonBaseException(20000, response.getErrMsg());
        }
    }
}