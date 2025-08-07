package com.plotech.itsrvsys.exception;


public enum CommonBaseErrorCode {

    // 10000 - 用户相关
    USER_NOT_EXIST(10000, "用户不存在"),
    USER_EXIST(10000, "用户已存在"),
    USERNAME_ISNULL(10000, "用户名不能为空"),
    USER_PASSWORD_ERROR(10000, "用户名或密码错误"),
    USER_NOT_PERMISSION(10000, "用户无权限"),
    // 20000 - 和大哥的接口相关
    API_FOR_NOT_FOUND(20000, "未找到对应的接口配置"),
    // 30000 - MinIO 相关
    BUCKET_NOT_EXIST(30000, "该文件/文件夹（存储桶）不存在"),
    BUCKET_CREATE_FAIL(30000, "创建文件/文件夹（存储桶）失败"),
    BUCKET_DELETE_FAIL(30000, "删除文件/文件夹（存储桶）失败"),
    FILE_IS_BLANK(30000, "该文件名获取为空"),
    FILE_UPLOAD_FAIL(30000, "上传文件失败"),
    FILE_DOWNLOAD_FAIL(30000, "下载文件失败"),
    IMAGE_PREVIEW_FAIL(30000, "图片预览失败"),
    // 40000 - 数据库相关
    SAVE_FAIL(40000, "插入数据失败，请联系系统管理员"),
    UPDATE_FAIL(40000, "更新数据失败，请联系系统管理员"),
    NO_PERMIT_EMPLOYEE(40000, "该单据无可签核的人员"),
    NO_DATA(40000, "查无数据"),
    // 50000 - 本项目内的错误
    NO_TARGET_TYPE_CLASS(50000, "目标类型类未找到"),
    NO_TARGET_TYPE_PARAM(50000, "无目标类型参数，数据接收失败"),
    PAGE_PARAM_ERROR(50000, "分页参数错误：fromIndex超出范围");

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
