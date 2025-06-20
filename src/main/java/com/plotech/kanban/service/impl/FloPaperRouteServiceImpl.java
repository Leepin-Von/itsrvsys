package com.plotech.kanban.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plotech.kanban.exception.CommonBaseErrorCode;
import com.plotech.kanban.exception.CommonBaseException;
import com.plotech.kanban.mapper.FloPaperRouteMapper;
import com.plotech.kanban.pojo.dto.ApprovalCenterTop;
import com.plotech.kanban.pojo.dto.FloConfig;
import com.plotech.kanban.pojo.dto.FloPaperRouteDTO;
import com.plotech.kanban.pojo.dto.FloStdRoute;
import com.plotech.kanban.pojo.entity.FloPaperRoute;
import com.plotech.kanban.pojo.entity.FloSearchNonConfirm;
import com.plotech.kanban.service.FloPaperRouteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author OF00047
 * @description 针对表【FLO_PaperRoute】的数据库操作Service实现
 * @createDate 2025-05-09 11:11:56
 */
@DS("mssql")
@Service
@AllArgsConstructor
public class FloPaperRouteServiceImpl extends ServiceImpl<FloPaperRouteMapper, FloPaperRoute> implements FloPaperRouteService {

    private final FloPaperRouteMapper floPaperRouteMapper;

    @Override
    public ArrayList<String> getPermitEmp(String paperNo) {
        ArrayList<FloPaperRouteDTO> paperRouteDTOs = floPaperRouteMapper.callForNonAllowPaper();
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
            ArrayList<String> permitEmps = new ArrayList<>();
            if (permitEmpNames.contains("、")) {
                String[] permitEmpNameArr = permitEmpNames.split("、");
                for (String permitEmpName : permitEmpNameArr) {
                    String empId = floPaperRouteMapper.getEmpId(permitEmpName);
                    permitEmps.add(empId);
                }
            } else {
                String empId = floPaperRouteMapper.getEmpId(permitEmpNames);
                permitEmps.add(empId);
            }
            return permitEmps;
        }
    }

    @Override
    public void runFlow(ApprovalCenterTop top, String paperNo) {
        // 判断工号是否有此人
        String empId = floPaperRouteMapper.isEmpIdExist(top.getConfirmEmpId());
        if (empId.isEmpty()) {
            throw new CommonBaseException(CommonBaseErrorCode.USER_NOT_EXIST);
        }
        ArrayList<FloSearchNonConfirm> floSearchNonConfirms = floPaperRouteMapper.callFloSearchNonConfirm(empId);
        FloSearchNonConfirm floSearchNonConfirm = null;
        for (FloSearchNonConfirm item : floSearchNonConfirms) {
            if (Objects.equals(item.getPaperNo(), paperNo)) {
                floSearchNonConfirm = item;
                break;
            }
        }
        ArrayList<FloPaperRoute> floPaperRoutes = floPaperRouteMapper.queryByPaperNo(paperNo);
        if (CollectionUtils.isEmpty(floPaperRoutes)) {
            throw new CommonBaseException(CommonBaseErrorCode.NO_DATA);
        }
        for (FloPaperRoute floPaperRoute : floPaperRoutes) {
            String steptCode = floPaperRoute.getSteptCode();
            if ("開始".equals(steptCode) || "結束".equals(steptCode)) continue;
            assert floSearchNonConfirm != null;
            if (!Objects.equals(floSearchNonConfirm.getSerialNo(), floPaperRoute.getSerialNo())) continue;
            int isConfirm = top.getIsConfirm();
            if ((floPaperRoute.getIsEmail2Agent() == 1 || floPaperRoute.getFlowId().equals("STD17")) && isConfirm == 1) {
                updateFlo(floPaperRoute, top);
            }
            FloConfig floConfig = floPaperRouteMapper.queryByFlowId(floPaperRoute.getFlowId());
            FloStdRoute floStdRoute = floPaperRouteMapper.querySpNameS(paperNo, floPaperRoute.getSerialNo());
            String spNameConfirm = floStdRoute.getConfirmProcedure();
            String spNameNonConfirm = floStdRoute.getNonConfirmProcedure();
            if (isConfirm == 1) {
                // 核可
                if (spNameConfirm.isEmpty()) runCommonFlow(floPaperRoute, floConfig, paperNo, spNameConfirm, isConfirm);
                updateFlo(floPaperRoute, top);
            } else if (isConfirm == 2) {
                // 退审
                if (spNameNonConfirm.isEmpty())
                    runCommonFlow(floPaperRoute, floConfig, paperNo, spNameNonConfirm, isConfirm);
                updateFlo(floPaperRoute, top);
                doReturn(floPaperRoute.getPaperNo(), floPaperRoute.getSerialNo() + 1, 0, floSearchNonConfirm.getConfirmNo());
                try {
                    floPaperRouteMapper.callFloRejectMail(paperNo, floPaperRoute.getSerialNo(), 0, floSearchNonConfirm.getConfirmNo(), top.getConfirmEmpId(), top.getComment());
                } catch (Exception e) {
                    throw new CommonBaseException(40000, "发送退审邮件失败");
                }
            }
        }
    }

    /**
     * 储存，触发FLO_PaperRoute表的触发器，更新来源单据主表上的status
     *
     * @param floPaperRoute 储存的FLO_PaperRoute
     * @param top           签核中心顶部表单数据
     */
    void updateFlo(FloPaperRoute floPaperRoute, ApprovalCenterTop top) {
        FloPaperRoute result = floPaperRouteMapper.queryByLogicPK(floPaperRoute.getSystemId(), floPaperRoute.getPaperNo(), floPaperRoute.getFlowId(), floPaperRoute.getSerialNo());
        result.setIsConfirm(top.getIsConfirm());
        result.setConfirmEmpId(top.getConfirmEmpId());
        result.setConfirmDate(top.getConfirmDate());
        result.setComment(top.getComment());
        UpdateWrapper<FloPaperRoute> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("SystemId", result.getSystemId()).eq("PaperNo", result.getPaperNo()).eq("FlowId", result.getFlowId()).eq("SerialNo", result.getSerialNo()).set("IsConfirm", result.getIsConfirm()).set("ConfirmEmpId", result.getConfirmEmpId()).set("ConfirmDate", result.getConfirmDate()).set("comment", result.getComment());
        boolean updated = update(updateWrapper);
        if (!updated) {
            throw new CommonBaseException(CommonBaseErrorCode.UPDATE_FAIL);
        }
    }

    /**
     * 核可和退审的流程中公共的部分
     *
     * @param floPaperRoute FLO_PaperRoute对象
     * @param floConfig     FLO_Config表实体类对象
     * @param paperNo       单据编号
     * @param procedureName 存储过程名称（核可或退审的存储过程名称）
     */
    void runCommonFlow(FloPaperRoute floPaperRoute, FloConfig floConfig, String paperNo, String procedureName, int isConfirm) {
        int isOver = floPaperRouteMapper.selectOne(new QueryWrapper<FloPaperRoute>().eq("PaperNo", paperNo).eq("SerialNo", floPaperRoute.getSerialNo())).getIsOver();
        if (isOver == 0 && !procedureName.isEmpty()) {
            String fullProcedureName = floConfig.getServerName() + "." + floConfig.getDataBaseName() + ".dbo." + procedureName;
            try {
                floPaperRouteMapper.callProcedure(fullProcedureName, paperNo);
            } catch (Exception e) {
                throw new CommonBaseException(40000, e.getMessage());
            }
        }
        // 是否为最后一关
        int serialNo = floPaperRouteMapper.getSerialNo(paperNo);
        FloConfig floConfigLast = floPaperRouteMapper.querySpNameC(floPaperRoute.getFlowId());
        String spNameConfirm = floConfigLast.getConfirmProcedure();
        String spNameNonConfirm = floConfigLast.getNonConfirmProcedure();
        String procedureNameLast = isConfirm == 1 ? spNameConfirm : isConfirm == 2 ? spNameNonConfirm : "";
        if (Objects.equals(serialNo, floPaperRoute.getSerialNo()) && !procedureNameLast.isEmpty()) {
            String spName = floConfigLast.getServerName() + "." + floConfigLast.getDataBaseName() + ".dbo." + procedureNameLast;
            try {
                floPaperRouteMapper.callProcedure(spName, paperNo);
            } catch (Exception e) {
                throw new CommonBaseException(40000, e.getMessage());
            }
        }
    }

    /**
     * 退审操作
     *
     * @param paperNo   单据编号
     * @param item      项目
     * @param groupType 签核（0）还是会签（1）
     * @param confirmNo 编号（同FLO_PaperRoute的SerialNo）
     */
    void doReturn(String paperNo, int item, int groupType, int confirmNo) {
        try {
            floPaperRouteMapper.doReturn(paperNo, item, groupType, confirmNo);
        } catch (Exception e) {
            throw new CommonBaseException(40000, "退审失败：" + e.getMessage());
        }
    }
}
