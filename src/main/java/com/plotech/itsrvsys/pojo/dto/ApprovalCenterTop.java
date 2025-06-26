package com.plotech.itsrvsys.pojo.dto;

import lombok.Data;

@Data
public class ApprovalCenterTop {
    private Integer isConfirm;
//    private Boolean isFold; // 展开单据流程
    private String confirmEmpId;
    private String confirmDate;
    private String comment;
}
