package com.plotech.itsrvsys.service;

import com.plotech.itsrvsys.pojo.dto.PkgInfo;
import com.plotech.itsrvsys.pojo.entity.QRCode;
import com.plotech.itsrvsys.pojo.vo.TransferDataRequest;
import com.plotech.itsrvsys.pojo.vo.TransferDataWithTypeRequest;

import java.util.ArrayList;
import java.util.HashMap;

public interface DataForwardService {
    Object transferData(TransferDataRequest requestData, String apiFor);
    Object userChgPwd(TransferDataRequest requestData);
    <T> HashMap<String, Object> transferDataDynamic(TransferDataWithTypeRequest requestData, String apiFor);
    ArrayList<PkgInfo> getPkgInfo(ArrayList<QRCode> qrCodes);
}
