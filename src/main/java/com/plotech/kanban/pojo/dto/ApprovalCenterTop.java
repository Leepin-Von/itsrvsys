package com.plotech.kanban.pojo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApprovalCenterTop {
    private Integer isConfirm;
//    private Boolean isFold; // 展开单据流程
    private String confirmEmpId;
    private String confirmDate;
    private String comment;
}
