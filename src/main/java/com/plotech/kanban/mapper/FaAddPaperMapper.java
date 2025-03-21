package com.plotech.kanban.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.plotech.kanban.pojo.entity.FaAddPaper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FaAddPaperMapper extends BaseMapper<FaAddPaper> {
    List<FaAddPaper> execFaAddPaper(String paperNo);
}
