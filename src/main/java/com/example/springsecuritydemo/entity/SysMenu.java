package com.example.springsecuritydemo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ycy
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysMenu implements Serializable {
    private static final long serialVersionUID = -9025906373702004043L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotNull(message = "上级菜单不能为空")
    private Long parentId;

    @NotBlank(message = "菜单名称不能为空")
    private String name;

    private String path;

    private String perms;

    private String component;

    @NotNull(message = "菜单类型不能为空")
    private Integer type;

    private String icon;

    private Integer orderNum;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Integer status;

    @TableLogic
    private Integer isDelete;

    @Version
    private Integer version;

    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();
}
