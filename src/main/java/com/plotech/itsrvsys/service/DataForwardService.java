package com.plotech.itsrvsys.service;

import com.plotech.itsrvsys.pojo.vo.TransferDataRequest;

import java.util.HashMap;

public interface DataForwardService {
    Object transferData(TransferDataRequest requestData);
    Object userChgPwd(TransferDataRequest requestData);
    HashMap<String, Object> transferDataForApprovalCenterWithPage(TransferDataRequest requestData);
}
