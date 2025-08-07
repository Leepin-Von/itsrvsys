package com.plotech.itsrvsys.pojo.dto;

import lombok.Data;

@Data
public class RptSupport {
    private String paperNo;
    private String unitId;
    private String unitName;
    private String whoInPut;
    private String whoInPutName;
    private Integer item;
    private String empId;
    private String empName;
    private String supportDateB;
    private String supportDateE;
    private Double supportHr;
    private Double supportExtHr;
    private Double supportHrSum;
    private String supportUnitId;
    private Integer restId;
    private Double restHr;
    private Integer restId2;
    private Double restHr2;
    private Double restHrSum;
    private String supportUnitName;
    private String note;
    private String fullName;
}
