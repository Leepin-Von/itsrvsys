package com.plotech.kanban.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.plotech.kanban.pojo.dto.FloConfig;
import com.plotech.kanban.pojo.dto.FloPaperRouteDTO;
import com.plotech.kanban.pojo.dto.FloStdRoute;
import com.plotech.kanban.pojo.entity.FloPaperRoute;
import com.plotech.kanban.pojo.entity.FloSearchNonConfirm;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

/**
 * @author OF00047
 * @description 针对表【FLO_PaperRoute】的数据库操作Mapper
 * @createDate 2025-05-09 11:11:56
 * @Entity com.plotech.kanban.pojo.entity.FloPaperRoute
 */
@Mapper
public interface FloPaperRouteMapper extends BaseMapper<FloPaperRoute> {
    ArrayList<FloPaperRouteDTO> callForNonAllowPaper();

    String getEmpId(String empName);

    String isEmpIdExist(String empId);

    ArrayList<FloPaperRoute> queryByPaperNo(String paperNo);

    FloPaperRoute queryByLogicPK(String systemId, String paperNo, String flowId, Integer serialNo);

    FloConfig queryByFlowId(String flowId);

    FloStdRoute querySpNameS(String paperNo, Integer serialNo);

    FloConfig querySpNameC(String flowId);

    void callProcedure(String spName, String paperNo);

    Integer getSerialNo(String paperNo);

    void doReturn(String paperNo, int item, int groupType, int confirmNo);

    void callFloRejectMail(String paperNo, int serialNo, int groupType, int confirmNo, String confirmEmpId, String notes);

    ArrayList<FloSearchNonConfirm> callFloSearchNonConfirm(String empId);
}




