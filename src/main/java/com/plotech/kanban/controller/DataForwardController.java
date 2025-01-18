package com.plotech.kanban.controller;

import com.plotech.kanban.exception.CommonBaseException;
import com.plotech.kanban.pojo.vo.TransferDataRequest;
import com.plotech.kanban.pojo.vo.TransferDataResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * 数据转发，用于将数据转发到指定的目标URL。
 */
@RestController
@RequestMapping("/api")
public class DataForwardController {

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

    public DataForwardController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private String getTargetUrl() {
        return "dev".equalsIgnoreCase(currentEnv) ? devApi : prodApi;
    }

    /**
     * 转发数据到目标URL。
     *
     * @param requestData 要转发的数据
     * @return 转发结果
     */
    @PostMapping("/forward")
    public Object transferData(@RequestBody TransferDataRequest requestData) {
        // 目标URL
        String targetUrl = getTargetUrl();
        HttpHeaders headers = new HttpHeaders();
        // 设置请求头内容类型为JSON
        headers.setContentType(MediaType.valueOf("application/json;charset=UTF-8"));
        HttpEntity<TransferDataRequest> httpEntity = new HttpEntity<>(requestData, headers);
        try {
            // 发送POST请求并返回结果
            return restTemplate.postForObject(targetUrl, httpEntity, TransferDataResponse.class);
        } catch (HttpServerErrorException e) {
            return new CommonBaseException(10000, e.getStatusText());
        }
    }
}