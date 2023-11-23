package com.lijinchao.controller;

import com.lijinchao.constant.MessageConstant;
import com.lijinchao.entity.Role;
import com.lijinchao.entity.RolePrivilege;
import com.lijinchao.entity.UserRole;
import com.lijinchao.entity.dto.RoleDTO;
import com.lijinchao.service.RoleService;
import com.lijinchao.utils.BaseApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    RoleService roleService;

    /**
     * 新增角色
     * @param roleDTO
     * @return
     */
    @PostMapping("")
    public BaseApiResult addRole(@RequestBody RoleDTO roleDTO) {
        try {
            roleService.addRole(roleDTO);
        } catch (Exception e) {
            log.error("saveRole fail", e);
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.OPERATE_FAILED);
        }
        return BaseApiResult.success("新增角色成功");
    }

    /**
     * 查询角色
     * @param roleDTO
     * @return
     */
    @GetMapping("")
    public BaseApiResult queryRoles(RoleDTO roleDTO) {
        try {
            List<Role> roles = roleService.queryRoles(roleDTO);
            return BaseApiResult.success(roles);
        } catch (Exception e) {
            log.error("queryRoles fail", e);
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.OPERATE_FAILED);
        }
    }

    /**
     * 删除
     * @param
     * @return
     */
    @DeleteMapping("")
    public BaseApiResult deleteRole(@RequestParam List<Long> ids) {
        try {
            roleService.deleteRole(ids);
        } catch (Exception e) {
            log.error("deleteRole fail", e);
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.OPERATE_FAILED);
        }
        return BaseApiResult.success("删除成功！");
    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @PutMapping("")
    public BaseApiResult updateRole(@RequestBody Role role) {
        try {
            roleService.updateRole(role);
        } catch (Exception e) {
            log.error("updateRole fail", e);
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.OPERATE_FAILED);
        }
        return BaseApiResult.success("修改成功！");
    }


//    /**
//     * 根据角色查权限
//     * @param rolePrivilege
//     * @param page
//     * @param pageSize
//     * @return
//     */
//    @GetMapping("/queryRolePrivilege")
//    public BaseApiResult queryRolePrivilege(RolePrivilege rolePrivilege, @RequestParam int page, @RequestParam int pageSize) {
//        try {
//            rolePrivilegeService.queryRolePrivileges(rolePrivilege, page, pageSize);
//        } catch (Exception e) {
//            log.error("queryRolePrivilege fail", e);
//            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.OPERATE_FAILED);
//        }
//        return ;
//    }

//    /**
//     * 查询用户角色
//     * @param userRole
//     * @param page
//     * @param pageSize
//     * @return
//     */
//    @GetMapping("/queryUserRole")
//    public BaseApiResult queryUserRole(UserRole userRole, @RequestParam int page, @RequestParam int pageSize) {
//        try {
//            PageInfo pageInfo = userRoleService.queryUserRole(userRole, page, pageSize);
//        } catch (Exception e) {
//            log.error("queryUserRole fail", e);
//            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.OPERATE_FAILED);
//        }
//        return ;
//    }

//    /**
//     * 删除角色用户
//     * @param
//     * @return
//     */
//    @DeleteMapping("/action/deleteUser")
//    public BaseApiResult deleteRoleUser(@RequestParam Long roleId, @RequestParam Long userId) {
//        try {
//            userRoleService.deleteByUserIdAndRoleId(userId, roleId);
//        } catch (Exception e) {
//            log.error("deleteRole fail", e);
//            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.OPERATE_FAILED);
//        }
//        return BaseApiResult.success("删除成功！");
//    }

//    /**
//     * 删除角色用户
//     * @param
//     * @return
//     */
//    @DeleteMapping("/action/deletePrivilege")
//    public BaseApiResult deleteRolePrivilege(@RequestParam Long roleId, @RequestParam Long privilegeId) {
//        try {
//            rolePrivilegeService.deleteByRoleIdAndPrivilegeId(new HashSet<>(Collections.singletonList(roleId)), privilegeId);
//        } catch (Exception e) {
//            log.error("deleteRole fail", e);
//            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.OPERATE_FAILED);
//        }
//        return BaseApiResult.success("删除成功！");
//    }


}
