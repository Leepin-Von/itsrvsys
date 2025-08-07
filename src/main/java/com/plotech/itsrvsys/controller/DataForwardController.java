package com.plotech.itsrvsys.controller;

import com.plotech.itsrvsys.pojo.dto.PkgInfo;
import com.plotech.itsrvsys.pojo.entity.QRCode;
import com.plotech.itsrvsys.pojo.vo.R;
import com.plotech.itsrvsys.pojo.vo.TransferDataRequest;
import com.plotech.itsrvsys.pojo.vo.TransferDataWithTypeRequest;
import com.plotech.itsrvsys.service.DataForwardService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

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
        return dataForwardService.transferData(requestData, "base");
    }

    @PostMapping("/wmsForward")
    public Object transferWmsData(@RequestBody TransferDataRequest requestData) {
        return dataForwardService.transferData(requestData, "wms");
    }

    @PostMapping("/user_chg_pwd")
    public Object userChgPwd(@RequestBody TransferDataRequest requestData) {
        return dataForwardService.userChgPwd(requestData);
    }

    @PostMapping("/approval")
    public R forApprovalCenter(@RequestBody TransferDataWithTypeRequest requestData) {
        requestData.setTargetType("com.plotech.itsrvsys.pojo.entity." + requestData.getTargetType());
        HashMap<String, Object> data = dataForwardService.transferDataDynamic(requestData, "base");
        return R.ok(data.get("data"))
                .put("total", data.get("total"))
                .put("pageNo", data.get("pageNo"))
                .put("pageSize", data.get("pageSize"));
    }

    @PostMapping("/turnStock")
    public R forTurnStock(@RequestBody TransferDataWithTypeRequest requestData) {
        requestData.setTargetType("com.plotech.itsrvsys.pojo.dto." + requestData.getTargetType());
        HashMap<String, Object> data = dataForwardService.transferDataDynamic(requestData, "wms");
        return R.ok(data.get("data"))
                .put("total", data.get("total"))
                .put("pageNo", data.get("pageNo"))
                .put("pageSize", data.get("pageSize"));
    }

    @PostMapping("/pkgInfo")
    public R getPkgInfo(@RequestBody ArrayList<QRCode> qrCodes) {
        ArrayList<PkgInfo> pkgInfo = dataForwardService.getPkgInfo(qrCodes);
        return R.ok(pkgInfo);
    }

    @PostMapping("/demense")
    public R forDemense(@RequestBody TransferDataWithTypeRequest requestData) {
        requestData.setTargetType("com.plotech.itsrvsys.pojo.entity." + requestData.getTargetType());
        HashMap<String, Object> data = dataForwardService.transferDataDynamic(requestData, "wms");
        return R.ok(data.get("data"))
                .put("total", data.get("total"))
                .put("pageNo", data.get("pageNo"))
                .put("pageSize", data.get("pageSize"));
    }
}