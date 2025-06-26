package com.plotech.itsrvsys.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.plotech.itsrvsys.exception.CommonBaseException;
import com.plotech.itsrvsys.pojo.entity.PRptPre;
import com.plotech.itsrvsys.pojo.vo.TransferDataRequest;
import com.plotech.itsrvsys.pojo.vo.TransferDataResponse;
import com.plotech.itsrvsys.service.DataForwardService;
import com.plotech.itsrvsys.util.SaltUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class DataForwardServiceImpl implements DataForwardService {

    /**
     * RestTemplate实例，用于发送HTTP请求。
     */
    private final RestTemplate restTemplate;
    private final SaltUtil saltUtil;
    @Value("${current.env}")
    private String currentEnv;
    @Value("${dev.api}")
    private String devApi;
    @Value("${prod.api}")
    private String prodApi;

    public DataForwardServiceImpl(RestTemplate restTemplate, SaltUtil saltUtil) {
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
    public Object transferData(TransferDataRequest requestData) {
        // 目标URL
        String targetUrl = getTargetUrl();
        HttpHeaders headers = new HttpHeaders();
        // 设置请求头内容类型为JSON
        headers.setContentType(MediaType.valueOf("application/json;charset=UTF-8"));
        HttpEntity<TransferDataRequest> httpEntity = new HttpEntity<>(requestData, headers);
        log.info("请求【{}】的请求体：【{}】", targetUrl, JSONObject.toJSONString(httpEntity));
        try {
            // 发送POST请求并返回结果
            TransferDataResponse response = restTemplate.postForObject(targetUrl, httpEntity, TransferDataResponse.class);
            log.info("请求【{}】返回的响应体：【{}】", targetUrl, JSONObject.toJSONString(response)); // 记录响应体到日志
            return response;
        } catch (HttpServerErrorException e) {
            throw new CommonBaseException(10000, e.getStatusText());
        }
    }

    @Override
    public Object userChgPwd(TransferDataRequest requestData) {
        // 目标URL
        String targetUrl = getTargetUrl();
        if ("UserChgPwd".equals(requestData.getDocType())) {
            HashMap<String, Object> parameters = requestData.getParameters();
            // 生成盐
            String salt = saltUtil.generateSalt();
            // 对密码进行加盐MD5加密处理
            String password = parameters.get("PWD").toString();
            parameters.remove("PWD");
            parameters.put("PWD", DigestUtil.md5Hex(password.toUpperCase() + salt));
            parameters.put("CheckNum", salt);
            requestData.setParameters(parameters);
        }
        HttpHeaders headers = new HttpHeaders();
        // 设置请求头内容类型为JSON
        headers.setContentType(MediaType.valueOf("application/json;charset=UTF-8"));
        HttpEntity<TransferDataRequest> httpEntity = new HttpEntity<>(requestData, headers);
        log.info("请求【{}】的请求体：【{}】", targetUrl, JSONObject.toJSONString(httpEntity));
        try {
            // 发送POST请求并返回结果
            TransferDataResponse response = restTemplate.postForObject(targetUrl, httpEntity, TransferDataResponse.class);
            log.info("请求【{}】返回的响应体：【{}】", targetUrl, JSONObject.toJSONString(response)); // 记录响应体到日志
            return response;
        } catch (HttpServerErrorException e) {
            throw new CommonBaseException(20000, e.getStatusText());
        }
    }

    @Override
    public HashMap<String, Object> transferDataForApprovalCenterWithPage(TransferDataRequest requestData) {
        // 目标URL
        String targetUrl = getTargetUrl();
        HttpHeaders headers = new HttpHeaders();
        // 设置请求头内容类型为JSON
        headers.setContentType(MediaType.valueOf("application/json;charset=UTF-8"));
//        HashMap<String, Object> parameters = requestData.getParameters();
//        int pageNo = (int) parameters.get("pageNo") - 1; // 第一页传1，改从0开始（作为下标），所以减1
//        parameters.remove("pageNo");
//        requestData.setParameters(parameters);
        HttpEntity<TransferDataRequest> httpEntity = new HttpEntity<>(requestData, headers);
        log.info("请求【{}】的请求体：【{}】", targetUrl, JSONObject.toJSONString(httpEntity));
        try {
            // 发送POST请求并返回结果
            TransferDataResponse response = restTemplate.postForObject(targetUrl, httpEntity, TransferDataResponse.class);
            log.info("请求【{}】返回的响应体：【{}】", targetUrl, JSONObject.toJSONString(response)); // 记录响应体到日志
            assert response != null;
            if ("OK".equals(response.getState())) {
                List<PRptPre> fullList = JSONObject.parseObject(JSONObject.toJSONString(response.getData()), new TypeReference<ArrayList<PRptPre>>() {
                });
                HashMap<String, Object> result = new HashMap<>();
                result.put("data", fullList);
                result.put("total", fullList.size());
                return result;
            } else {
                throw new CommonBaseException(20000, response.getErrMsg());
            }
        } catch (HttpServerErrorException e) {
            throw new CommonBaseException(10000, e.getStatusText());
        }
    }
}
