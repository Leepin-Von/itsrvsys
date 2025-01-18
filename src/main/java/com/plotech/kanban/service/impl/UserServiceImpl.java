package com.plotech.kanban.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.plotech.kanban.exception.CommonBaseException;
import com.plotech.kanban.pojo.entity.User;
import com.plotech.kanban.pojo.vo.TransferDataRequest;
import com.plotech.kanban.pojo.vo.TransferDataResponse;
import com.plotech.kanban.service.UserService;
import com.plotech.kanban.util.SaltMD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final RestTemplate restTemplate;
    @Value("${current.env}")
    private String currentEnv;
    @Value("${dev.api}")
    private String devApi;
    @Value("${prod.api}")
    private String prodApi;

    public UserServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String getTargetUrl() {
        return "dev".equalsIgnoreCase(currentEnv) ? devApi : prodApi;
    }

    @Override
    public boolean authConfirm(User user) {
        // 不知道ERP里能用的账号密码，所以无奈写了这段测试用，用户名为test直接过
        // 正式环境下要删掉
//        log.info(JSONObject.toJSONString(user));
//        if ("test".equalsIgnoreCase(user.getUsername())) return true;
        // 对密码进行加盐MD5加密处理
        user.setPassword(SaltMD5Util.generateSaltPassword(user.getPassword().toUpperCase()));
        // 目标路径
        String targetUrl = getTargetUrl();
        // 请求体
        TransferDataRequest request = new TransferDataRequest();
        request.setDocType("ChkUserPermissions");
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("UserId", user.getUsername());
        parameters.put("PWD", user.getPassword());
        parameters.put("ItemId", user.getItemId());
        request.setParameters(parameters);
        // 请求头
        HttpHeaders headers = new HttpHeaders();
        // 设置请求头内容类型为JSON
        headers.setContentType(MediaType.valueOf("application/json;charset=UTF-8"));
        HttpEntity<TransferDataRequest> httpEntity = new HttpEntity<>(request, headers);
        TransferDataResponse response = restTemplate.postForObject(targetUrl, httpEntity, TransferDataResponse.class);
        assert response != null;
        log.info(JSONObject.toJSONString(response)); // 记录响应体到日志
        if (response.getState().equals("OK")) {
            return true;
        } else {
            throw new CommonBaseException(20000, response.getErrMsg());
        }
    }
}