package com.lijinchao.entity.dto;

import com.lijinchao.entity.Menu;
import com.lijinchao.entity.RolePrivilege;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuDTO extends Menu {
    /**
     * 完整路径
     */
    private String fullPath;

    /**
     * 菜单对应的角色权限
     */
    private List<RolePrivilege> rolePrivilegeList;

    /**
     * 子菜单
     */
    private List<MenuDTO> child = new ArrayList<MenuDTO>();


    public List<RolePrivilege> getSysRolePrivilegeList() {
        return rolePrivilegeList;
    }

    public void setSysRolePrivilegeList(List<RolePrivilege> sysRolePrivilegeList) {
        this.rolePrivilegeList = sysRolePrivilegeList;
    }

    public List<MenuDTO> getChild() {
        return child;
    }

    public void setChild(List<MenuDTO> child) {
        this.child = child;
    }
}
