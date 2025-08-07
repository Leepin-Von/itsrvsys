package com.plotech.itsrvsys.pojo.dto;

import lombok.Data;

@Data
public class PkgInfo {
    private Long serialNum;
    private String pkgId;
    private String stockId;
    private String storeHouse;
    private String matCode;
    private String manufDate;
    private String expireDate;
    private String lotNum;
    private String poNum;
    private Double qnty;
}
