package com.plotech.itsrvsys.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.plotech.itsrvsys.exception.CommonBaseErrorCode;
import com.plotech.itsrvsys.exception.CommonBaseException;
import com.plotech.itsrvsys.pojo.dto.PkgInfo;
import com.plotech.itsrvsys.pojo.entity.QRCode;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DataForwardServiceImpl implements DataForwardService {

    /**
     * RestTemplate实例，用于发送HTTP请求。
     */
    private final RestTemplate restTemplate;
    private final SaltUtil saltUtil;
    @Value("${com.plotech.current.env}")
    private String currentEnv;
    @Value("${com.plotech.base.dev.api}")
    private String baseDevApi;
    @Value("${com.plotech.base.prod.api}")
    private String baseProdApi;
    @Value("${com.plotech.wms.dev.api}")
    private String wmsDevApi;
    @Value("${com.plotech.wms.prod.api}")
    private String wmsProdApi;

    public DataForwardServiceImpl(RestTemplate restTemplate, SaltUtil saltUtil) {
        this.restTemplate = restTemplate;
        this.saltUtil = saltUtil;
    }

    private String getTargetUrl(String apiFor) {
        switch (apiFor) {
            case "base":
                return "dev".equals(currentEnv) ? baseDevApi : baseProdApi;
            case "wms":
                return "dev".equals(currentEnv) ? wmsDevApi : wmsProdApi;
            default:
                throw new CommonBaseException(CommonBaseErrorCode.API_FOR_NOT_FOUND);
        }
    }

    @Override
    public Object transferData(TransferDataRequest requestData, String apiFor) {
        // 目标URL
        String targetUrl = getTargetUrl(apiFor);
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
        String targetUrl = getTargetUrl("base");
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
    public <T> HashMap<String, Object> transferDataDynamic(TransferDataWithTypeRequest requestData, String apiFor) {
        String targetUrl = getTargetUrl(apiFor);
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
                    typeReference = new TypeReference<List<T>>(targetClass) {
                    };
                } catch (ClassNotFoundException e) {
                    throw new CommonBaseException(CommonBaseErrorCode.NO_TARGET_TYPE_CLASS);
                }
                // 使用传入的typeReference来解析数据
                List<T> fullList = JSONObject.parseObject(JSONObject.toJSONString(response.getData()), typeReference);
                int pageSize = requestData.getPageSize();
                int pageNo = requestData.getPageNo();

                // 如果没有指定分页参数，则返回所有数据
                if (pageSize == 0 && pageNo == 0) {
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("data", fullList);
                    return result;
                }

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

    @Override
    public ArrayList<PkgInfo> getPkgInfo(ArrayList<QRCode> qrCodes) {
        // 使用线程池并行处理多个 QRCode 请求
        ExecutorService executorService = Executors.newFixedThreadPool(Math.min(qrCodes.size(), 10));
        List<CompletableFuture<List<PkgInfo>>> futures = qrCodes.stream()
                .map(qrCode -> CompletableFuture.supplyAsync(() -> fetchPkgInfoForQRCode(qrCode), executorService))
                .collect(Collectors.toList());
        
        ArrayList<PkgInfo> pkgInfos = new ArrayList<>();
        try {
            for (CompletableFuture<List<PkgInfo>> future : futures) {
                pkgInfos.addAll(future.get(60, TimeUnit.SECONDS)); // 每个请求最多等待60秒
            }
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("获取PkgInfo时发生错误", e);
            throw new CommonBaseException(20000, "获取PkgInfo时发生错误: " + e.getMessage());
        } finally {
            executorService.shutdown();
        }
        
        return pkgInfos;
    }
    
    private List<PkgInfo> fetchPkgInfoForQRCode(QRCode qrCode) {
        TransferDataRequest request = new TransferDataRequest();
        request.setDocType("PkgInfo");
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("PkgId", qrCode.getPackageId());
        parameters.put("StockId","");
        parameters.put("StoreHouse","");
        parameters.put("MatCode","");
        parameters.put("LotNum","");
        parameters.put("PONum", "");
        request.setParameters(parameters);
        
        TransferDataResponse data = (TransferDataResponse) transferData(request, "wms");
        if("OK".equals(data.getState())) {
            String jsonString = JSONObject.toJSONString(data.getData());
            return JSONObject.parseObject(jsonString, new TypeReference<ArrayList<PkgInfo>>(){});
        } else {
            log.info("获取PkgInfo失败，错误信息：{}", data.getErrMsg());
            throw new CommonBaseException(20000, data.getErrMsg());
        }
    }

}
