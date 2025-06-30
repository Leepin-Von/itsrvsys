package com.plotech.itsrvsys.pojo.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TransferDataWithTypeRequest extends TransferDataRequest {
    private String targetType;
    private int pageSize = 1;
    private int pageNo = 1;
}