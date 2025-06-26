package com.plotech.itsrvsys.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FaAddPaper {
    private String paperNo;
    private String unitName;
    private String title;
    private LocalDateTime paperDate;
    private String faName;
    private String itemSpec;
    private String custUnitName;
    private Integer qnty;
    private Integer unitPrice;
    private Double subTotal;
    private LocalDateTime buildDate;
    private String faId;
    private Integer item;
    private String cerNo;
    private String notes;
    private Integer finished;
    private Integer isStockIn;
    private String sourceNo;
    private Integer sourceItem;
    private String isMain;
    private String isLaw;
    private String empName;
}
