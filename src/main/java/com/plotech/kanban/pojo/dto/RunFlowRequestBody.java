package com.plotech.kanban.pojo.dto;

import lombok.Data;

@Data
public class RunFlowRequestBody {
    private String paperNo;
    private ApprovalCenterTop top;
}
