package com.plotech.itsrvsys.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.plotech.itsrvsys.exception.CommonBaseErrorCode;
import com.plotech.itsrvsys.exception.CommonBaseException;
import com.plotech.itsrvsys.pojo.vo.TransferDataRequest;
import com.plotech.itsrvsys.pojo.vo.TransferDataResponse;
import com.plotech.itsrvsys.pojo.vo.TransferDataWithTypeRequest;
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

    /**
     * 通用的数据转发方法，支持动态指定响应数据类型
     *
     * @param requestData 请求参数
     * @return 包含数据和总数的Map
     */
    @Override
    public <T> HashMap<String, Object> transferDataWithGeneric(TransferDataWithTypeRequest requestData) {
        String targetUrl = getTargetUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/json;charset=UTF-8"));

        HttpEntity<TransferDataRequest> httpEntity = new HttpEntity<>(requestData, headers);
        log.info("请求【{}】的请求体：【{}】", targetUrl, JSONObject.toJSONString(httpEntity));

        try {
            TransferDataResponse response = restTemplate.postForObject(targetUrl, httpEntity, TransferDataResponse.class);
            log.info("请求【{}】返回的响应体：【{}】", targetUrl, JSONObject.toJSONString(response));

            if ("OK".equals(response.getState())) {
                TypeReference<List<T>> typeReference;
                try {
                    String targetClassName = requestData.getTargetType();
                    if (targetClassName == null || targetClassName.isEmpty()) {
                        throw new CommonBaseException(CommonBaseErrorCode.NO_TARGET_TYPE_PARAM);
                    }
                    Class<?> targetClass = Class.forName(targetClassName);
                    typeReference = new TypeReference<List<T>>(targetClass) {};
                } catch (ClassNotFoundException e) {
                    throw new CommonBaseException(CommonBaseErrorCode.NO_TARGET_TYPE_CLASS);
                }
                // 使用传入的typeReference来解析数据
                List<T> fullList = JSONObject.parseObject(JSONObject.toJSONString(response.getData()), typeReference);
                int pageSize = requestData.getPageSize();
                int pageNo = requestData.getPageNo();
                int fromIndex = (pageNo - 1) * pageSize;
                int toIndex = Math.min(fromIndex + pageSize, fullList.size());
                if (fromIndex > toIndex) {
                    log.warn("分页参数非法：fromIndex({}) > toIndex({})", fromIndex, toIndex);
                    fromIndex = toIndex;
                }
                if (fromIndex >= fullList.size() || fromIndex < 0) {
                    throw new CommonBaseException(CommonBaseErrorCode.PAGE_PARAM_ERROR);
                }
                List<T> pagedList = fullList.subList(fromIndex, toIndex);
                HashMap<String, Object> result = new HashMap<>();
                result.put("data", pagedList);
                result.put("total", fullList.size());
                result.put("pageSize", pageSize);
                result.put("pageNo", pageNo);
                return result;
            } else {
                throw new CommonBaseException(20000, response.getErrMsg());
            }
        } catch (HttpServerErrorException e) {
            throw new CommonBaseException(10000, e.getStatusText());
        }
    }

}
