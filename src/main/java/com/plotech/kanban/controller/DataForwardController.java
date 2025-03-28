package com.plotech.kanban.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.plotech.kanban.exception.CommonBaseException;
import com.plotech.kanban.pojo.vo.TransferDataRequest;
import com.plotech.kanban.pojo.vo.TransferDataResponse;
import com.plotech.kanban.service.DataForwardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.HashMap;
import java.util.Random;

/**
 * 数据转发，用于将数据转发到指定的目标URL。
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class DataForwardController {

    private final DataForwardService dataForwardService;

    /**
     * 转发数据到目标URL。
     *
     * @param requestData 要转发的数据
     * @return 转发结果
     */
    @PostMapping("/forward")
    public Object transferData(@RequestBody TransferDataRequest requestData) {
        return dataForwardService.transferData(requestData);
    }

    @PostMapping("/user_chg_pwd")
    public Object userChgPwd(@RequestBody TransferDataRequest requestData) {
        return dataForwardService.userChgPwd(requestData);
    }
}