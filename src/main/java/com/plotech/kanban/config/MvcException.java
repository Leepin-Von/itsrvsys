package com.plotech.kanban.config;

import com.plotech.kanban.exception.CommonBaseException;
import com.plotech.kanban.pojo.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ResponseBody
@Slf4j
public class MvcException {

    //无参构造
    public MvcException() {
        log.info("启动mvc异常拦截器");
    }

    @ExceptionHandler(CommonBaseException.class)
    public R commonBaseException(CommonBaseException e) {
        return R.err(e);
    }


}