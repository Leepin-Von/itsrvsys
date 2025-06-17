package com.plotech.kanban.pojo.entity;

import lombok.Data;

@Data
public class FloSearchNonConfirm {
    private String systemId;
    private String systemName;
    private String paperName;
    private String paperNo;
    private Integer serialNo;
    private Integer AnnounceNo;
    private String type;
    private String paperUnitName;
    private String groupType;
    private Integer confirmNo;
}
