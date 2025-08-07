package com.plotech.itsrvsys.pojo.entity;

import lombok.Data;

@Data
public class DemenseLotQnty {
    private Long serialNum;
    private String matCode;
    private String storeHouse;
    private String lotNum;
    private Double stockQnty;
}
