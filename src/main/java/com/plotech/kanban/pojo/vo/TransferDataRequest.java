package com.plotech.kanban.pojo.vo;

import lombok.Data;

import java.util.HashMap;

@Data
public class TransferDataRequest {
    private String docType;
    private HashMap<String, Object> parameters;
}
