package com.lijinchao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 菜单表
 * @TableName menu
 */
@TableName(value ="menu")
@Data
public class Menu extends BaseEntity {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 权限id
     */
    private Long privilegeId;

    /**
     * 上级菜单
     */
    private Long parentId;

    /**
     * 菜单类型
     */
    private String type;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单编码
     */
    private String code;

    /**
     * 菜单路径
     */
    private String path;

    /**
     * 菜单分组
     */
    private String groupId;

    /**
     * 菜单排序
     */
    private Long orderNum;

    /**
     * 菜单图标
     */
    private String icon;

}
