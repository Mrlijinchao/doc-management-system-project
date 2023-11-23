package com.lijinchao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 权限表
 * @TableName privilege
 */
@TableName(value ="privilege")
@Data
public class Privilege extends BaseEntity {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 权限类型
     */
    private String type;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 路径
     */
    private String path;

}
