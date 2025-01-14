package com.plotech.kanban.pojo.vo;

import com.plotech.kanban.exception.CommonBaseErrorCode;
import com.plotech.kanban.exception.CommonBaseException;
import lombok.Data;

/**
 * 公共返回结构
 */

@Data
//"公共返回结构"
public class R<T> {

    // "返回代码"
    private Integer code;

    // "消息"
    private String message;

    // "返回数据"
    private T data;

    public R() {
    }

    public R(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public R(Integer code, String message, T data) {
        this(code, message);
        this.data = data;
    }

    public static <T> R ok() {
        return new R(200, "success");
    }

    public static <T> R ok(T data) {
        return new R(200, "success", data);
    }

    public static R err(Integer code, String message) {
        return new R(code, message);
    }

    //错误异常代码
    public static R err(CommonBaseException e) {
        return new R(e.getCode(), e.getMessage());
    }

    public static R err(CommonBaseErrorCode code) {
        return new R(code.getCode(), code.getErrMsg());
    }

}