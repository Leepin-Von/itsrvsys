package com.plotech.kanban.service;

import com.plotech.kanban.pojo.vo.TransferDataRequest;

import java.util.HashMap;

public interface DataForwardService {
    Object transferData(TransferDataRequest requestData);
    Object userChgPwd(TransferDataRequest requestData);
    HashMap<String, Object> transferDataForApprovalCenterWithPage(TransferDataRequest requestData);
}
