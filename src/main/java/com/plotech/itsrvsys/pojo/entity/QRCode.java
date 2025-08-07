package com.plotech.itsrvsys.pojo.entity;

import lombok.Data;

@Data
public class QRCode {
    private String packageId;
    private String matCode;
    private String orderNo;
    private Double qnty;
    private String manufDate;
    private String expireDate;
    private String lotNum;
}
