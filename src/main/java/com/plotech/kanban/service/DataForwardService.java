package com.plotech.kanban.service;

import com.plotech.kanban.pojo.vo.TransferDataRequest;

public interface DataForwardService {
    Object transferData(TransferDataRequest requestData);
    Object userChgPwd(TransferDataRequest requestData);
}
