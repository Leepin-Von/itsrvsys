package com.plotech.kanban.controller;

import com.plotech.kanban.pojo.vo.TransferDataRequest;
import com.plotech.kanban.pojo.vo.TransferDataResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class DataTransferController {

    @Resource
    private RestTemplate restTemplate;

    @PostMapping("/transfer")
    public Object transferData(@RequestBody TransferDataRequest requestData) {
        String targetUrl = "http://10.2.85.30:6666/ERP/MES/TServerMethods1/ISSAPIFun";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/json;charset=UTF-8"));
        HttpEntity<TransferDataRequest> httpEntity = new HttpEntity<>(requestData, headers);
        return restTemplate.postForObject(targetUrl, httpEntity, TransferDataResponse.class);
    }
}
