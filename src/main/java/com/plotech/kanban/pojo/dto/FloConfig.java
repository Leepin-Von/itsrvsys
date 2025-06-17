package com.plotech.kanban.pojo.dto;

import lombok.Data;

@Data
public class FloConfig {
    private String serverName;
    private String dataBaseName;
    private String confirmProcedure;
    private String nonConfirmProcedure;
}
