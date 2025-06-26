package com.plotech.itsrvsys.pojo.vo;

import com.plotech.itsrvsys.exception.CommonBaseErrorCode;
import com.plotech.itsrvsys.exception.CommonBaseException;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 公共返回结构
 */

@Data
// 公共返回结构
public class R<T> implements Serializable {

    private Integer code; // 返回代码

    private String message; // 消息

    private T data; // 返回数据

    private Map<String, Object> map = new HashMap<>(); // 动态返回数据

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

    public static <T> R<T> ok() {
        return new R<>(200, "success");
    }

    public static <T> R<T> ok(T data) {
        return new R<>(200, "success", data);
    }

    public static <T> R<T> err(Integer code, String message) {
        return new R<>(code, message);
    }

    //错误异常代码
    public static <T> R<T> err(CommonBaseException e) {
        return new R<>(e.getCode(), e.getMessage());
    }

    public static <T> R<T> err(CommonBaseErrorCode code) {
        return new R<>(code.getCode(), code.getErrMsg());
    }

    public R<T> put(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}