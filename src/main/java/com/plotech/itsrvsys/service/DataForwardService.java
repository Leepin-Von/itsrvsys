package com.plotech.itsrvsys.service;

import com.plotech.itsrvsys.pojo.vo.TransferDataRequest;
import com.plotech.itsrvsys.pojo.vo.TransferDataWithTypeRequest;

import java.util.HashMap;

public interface DataForwardService {
    Object transferData(TransferDataRequest requestData);
    Object userChgPwd(TransferDataRequest requestData);
    <T> HashMap<String, Object> transferDataWithGeneric(TransferDataWithTypeRequest requestData);
}
