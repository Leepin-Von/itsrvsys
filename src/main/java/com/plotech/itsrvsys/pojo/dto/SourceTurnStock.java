package com.plotech.itsrvsys.pojo.dto;

import lombok.Data;

@Data
public class SourceTurnStock {
    private Long serialNum;
    private String matCode;
    private String lotNum;
    private String stockId;
    private String storeHouse;
    private Double qnty;
}
