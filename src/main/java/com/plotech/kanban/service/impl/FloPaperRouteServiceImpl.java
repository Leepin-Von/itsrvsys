package com.plotech.kanban.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plotech.kanban.exception.CommonBaseErrorCode;
import com.plotech.kanban.exception.CommonBaseException;
import com.plotech.kanban.mapper.FloPaperRouteMapper;
import com.plotech.kanban.pojo.dto.FloPaperRouteDTO;
import com.plotech.kanban.pojo.entity.FloPaperRoute;
import com.plotech.kanban.service.FloPaperRouteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author OF00047
 * @description 针对表【FLO_PaperRoute】的数据库操作Service实现
 * @createDate 2025-05-09 11:11:56
 */
@DS("mssql")
@Service
@AllArgsConstructor
public class FloPaperRouteServiceImpl extends ServiceImpl<FloPaperRouteMapper, FloPaperRoute>
        implements FloPaperRouteService {

    private final FloPaperRouteMapper floPaperRouteMapper;

    @Override
    public HashMap<String, String> getPermitEmp(String paperNo) {
        ArrayList<FloPaperRouteDTO> paperRouteDTOs = floPaperRouteMapper.nonAllowPaper();
        if (CollectionUtils.isEmpty(paperRouteDTOs)) {
            throw new CommonBaseException(CommonBaseErrorCode.NO_DATA);
        }
        Map<String, String> map = new HashMap<>();
        for (FloPaperRouteDTO paperRouteDTO : paperRouteDTOs) {
            map.put(paperRouteDTO.getPaperNo(), paperRouteDTO.getAllEmpName());
        }
        String permitEmpNames = map.get(paperNo);
        if (permitEmpNames == null) {
            throw new CommonBaseException(CommonBaseErrorCode.NO_PERMIT_EMPLOYEE);
        } else {
            HashMap<String, String> permitEmpMap = new HashMap<>();
            if (permitEmpNames.contains("、")) {
                String[] permitEmpNameArr = permitEmpNames.split("、");
                for (String permitEmpName : permitEmpNameArr) {
                    String empId = floPaperRouteMapper.getEmpId(permitEmpName);
                    permitEmpMap.put(empId, permitEmpName);
                }
            } else {
                String empId = floPaperRouteMapper.getEmpId(permitEmpNames);
                permitEmpMap.put(empId, permitEmpNames);
            }
            return permitEmpMap;
        }
    }
}




