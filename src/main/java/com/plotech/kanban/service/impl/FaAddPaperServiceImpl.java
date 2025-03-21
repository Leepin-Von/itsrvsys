package com.plotech.kanban.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plotech.kanban.pojo.entity.FaAddPaper;
import com.plotech.kanban.mapper.FaAddPaperMapper;
import com.plotech.kanban.service.FaAddPaperService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DS("mssql")
@Slf4j
@Service
@AllArgsConstructor
public class FaAddPaperServiceImpl extends ServiceImpl<FaAddPaperMapper, FaAddPaper> implements FaAddPaperService {

    private final FaAddPaperMapper faAddPaperMapper;

    @Override
    public String execFaAddPaper(String paperNo, Integer pageNo, Integer pageSize) {
        List<FaAddPaper> results = faAddPaperMapper.execFaAddPaper(paperNo);
        if (CollectionUtil.isEmpty(results)) {
            return null;
        }
        int total = results.size();
        pageNo = pageNo == 0 ? 1 : pageNo;
        log.info("当前页码：{}，每页条数：{}", pageNo, pageSize);
        if (pageNo > 1) {
            results = results.subList((pageNo - 1) * pageSize, Math.min(pageNo * pageSize, results.size()));
        } else if (pageNo == 1) {
            results = results.subList(0, Math.min(pageSize, results.size()));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("data", results);
        map.put("total", total);
        map.put("count", results.size());
        String json = JSONObject.toJSONString(map, SerializerFeature.WRITE_MAP_NULL_FEATURES);
        log.info("积木报表API数据集获取的JSON: {}", json);
        return json;
    }
}
