package com.plotech.itsrvsys.exception;

import lombok.Data;

@Data
public class CommonBaseException extends RuntimeException{
    private Integer code;
    private String errMsg;

    public CommonBaseException(Integer code, String errMsg){
        super(errMsg);
        this.code = code;
        this.errMsg = errMsg;
    }

    public CommonBaseException(CommonBaseErrorCode errorCode){
        this(errorCode.getCode(), errorCode.getErrMsg());
    }
}
