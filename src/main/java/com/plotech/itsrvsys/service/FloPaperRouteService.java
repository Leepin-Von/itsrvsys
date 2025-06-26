package com.plotech.itsrvsys.service;

import com.plotech.itsrvsys.pojo.dto.ApprovalCenterTop;
import com.plotech.itsrvsys.pojo.entity.FloPaperRoute;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;

/**
* @author OF00047
* @description 针对表【FLO_PaperRoute】的数据库操作Service
* @createDate 2025-05-09 11:11:56
*/
public interface FloPaperRouteService extends IService<FloPaperRoute> {
    ArrayList<String> getPermitEmp(String paperNo);
    void runFlow(ApprovalCenterTop top, String paperNo);
}
