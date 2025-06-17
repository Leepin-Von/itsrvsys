package com.plotech.kanban.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName FLO_PaperRoute
 */
@TableName(value ="FLO_PaperRoute")
@Data
public class FloPaperRoute implements Serializable {

    private String systemId;

    private String paperNo;

    private String flowId;

    private Integer serialNo;

    private String paperName;

    private String steptType;

    private String steptCode;

    private String steptNote;

    private Integer isConfirm;

    private String comment;

    private String confirmDate;

    private String confirmEmpId;

    private String announce;

    private String paperUnitId;

    private String paperWhoInput;

    private LocalDateTime paperInputDate;

    private Integer isEmail2Agent;

    private Integer isEmail2EndUser;

    private Integer steptKind;

    private Integer signType;

    @TableField("Eo_SerialNo")
    private Integer eoSerialno;

    private Integer isCheck;

    private Integer isEmail;

    private Integer isOver;

    @TableField(exist = false)
    private String allEmpName;

    @TableField(exist = false)
    private String empId;

    @TableField(exist = false)
    private String empName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}