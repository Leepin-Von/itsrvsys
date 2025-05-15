package com.plotech.kanban.mapper;

import com.plotech.kanban.pojo.dto.FloPaperRouteDTO;
import com.plotech.kanban.pojo.entity.FloPaperRoute;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
    ArrayList<FloPaperRouteDTO> nonAllowPaper();

    String getEmpId(String empName);
}




