package com.plotech.itsrvsys.pojo.dto;

import lombok.Data;

@Data
public class FloSearchNonConfirm {
    private String systemId;
    private String systemName;
    private String paperName;
    private String paperNo;
    private Integer serialNo;
    private Integer announceNo;
    private String type;
    private String paperUnitName;
    private String groupType;
    private Integer confirmNo;
}
