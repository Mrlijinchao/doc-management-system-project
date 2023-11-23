package com.lijinchao.service;

import com.lijinchao.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lijinchao.entity.dto.RoleDTO;

import java.util.List;

/**
* @author 时之始
* @description 针对表【role(角色表)】的数据库操作Service
* @createDate 2023-11-14 10:01:55
*/
public interface RoleService extends IService<Role> {
    /**
     * 获取超级管理员角色
     * @return
     */
    Role getSuperAdmin();

    /**
     * 添加角色
     * @param roleDTO
     */
    void addRole(RoleDTO roleDTO);

    /**
     * 查询角色列表
     * @param roleDTO
     * @return
     */
    List<Role> queryRoles(RoleDTO roleDTO);

    /**
     * 删除角色
     * @param ids
     */
    void deleteRole(List<Long> ids);

    /**
     * 修改角色
     * @param role
     */
    void updateRole(Role role);

}
