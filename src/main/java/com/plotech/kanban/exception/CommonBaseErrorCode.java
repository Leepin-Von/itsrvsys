package com.plotech.kanban.exception;


public enum CommonBaseErrorCode {

    USER_NOT_EXIST(10000, "用户不存在"),
    USER_EXIST(10000, "用户已存在"),
    USERNAME_ISNULL(10000, "用户名不能为空"),
    USER_PASSWORD_ERROR(10000, "用户名或密码错误"),
    USER_NOT_PERMISSION(10000, "用户无权限");

    private Integer code;
    private String errMsg;

    public Integer getCode() {
        return code;
    }

    public CommonBaseErrorCode setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public CommonBaseErrorCode setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }

    public CommonBaseErrorCode setErrMsgPlaceholder(String errMsg) {
        this.errMsg = String.format(this.getErrMsg(),errMsg);
        return this;
    }


    CommonBaseErrorCode(Integer code){
        this.code = code;
        this.errMsg = errMsg;
    }

    CommonBaseErrorCode(Integer code, String errMsg){
        this.code = code;
        this.errMsg = errMsg;
    }

}
