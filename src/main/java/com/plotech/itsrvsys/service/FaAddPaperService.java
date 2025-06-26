package com.plotech.itsrvsys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.plotech.itsrvsys.pojo.entity.FaAddPaper;

public interface FaAddPaperService extends IService<FaAddPaper> {
    String execFaAddPaper(String paperNo, Integer pageNo, Integer pageSize);
}
