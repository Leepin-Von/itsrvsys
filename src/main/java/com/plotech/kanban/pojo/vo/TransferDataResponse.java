package com.plotech.kanban.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDataResponse<T> {
    private String state;
    private String errMsg;
    private T data;
}
