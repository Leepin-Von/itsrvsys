package com.plotech.itsrvsys.controller;

import com.plotech.itsrvsys.pojo.vo.R;
import com.plotech.itsrvsys.pojo.vo.TransferDataRequest;
import com.plotech.itsrvsys.service.DataForwardService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

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

    @PostMapping("/approval")
    public Object approvalCenter(@RequestBody TransferDataRequest requestData) {
        HashMap<String, Object> data = dataForwardService.transferDataForApprovalCenterWithPage(requestData);
        return R.ok(data.get("data"))
                .put("total", data.get("total"));
    }
}