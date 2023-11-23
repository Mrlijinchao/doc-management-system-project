package com.lijinchao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User extends BaseEntity {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 人员名称
     */
    private String name;

    /**
     * 人员账号
     */
    private String code;

    /**
     * 密码
     */
    private String password;

    /**
     * 身份证号
     */
    private String identityCode;

    /**
     * 性别
     */
    private String sex;

    /**
     * 手机
     */
    private Long phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 籍贯
     */
    private String nativePlace;

    /**
     * 专业
     */
    private String subject;

    /**
     * 学历
     */
    private String education;

    /**
     * 学校
     */
    private String school;

    /**
     * 毕业年份
     */
    private String graduationYear;

    /**
     * 状态日期
     */
    private Date statusDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 其他系统用户id
     */
    private Long otherId;

    /**
     * 描述
     */
    private String description;

    /**
     * 生日
     */
    private Date birthtime;

    /**
     * 上次登录
     */
    private Date lastlogin;

}
