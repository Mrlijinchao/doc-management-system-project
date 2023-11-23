package com.lijinchao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijinchao.entity.Role;
import com.lijinchao.entity.RolePrivilege;
import com.lijinchao.entity.User;
import com.lijinchao.entity.UserRole;
import com.lijinchao.entity.dto.RoleDTO;
import com.lijinchao.enums.GlobalEnum;
import com.lijinchao.permission.PermissionRoleEnum;
import com.lijinchao.service.RolePrivilegeService;
import com.lijinchao.service.RoleService;
import com.lijinchao.mapper.RoleMapper;
import com.lijinchao.service.UserRoleService;
import com.lijinchao.utils.BeanUtilCopy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author 时之始
* @description 针对表【role(角色表)】的数据库操作Service实现
* @createDate 2023-11-14 10:01:55
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

    @Resource
    RoleMapper roleMapper;
    @Resource
    RolePrivilegeService rolePrivilegeService;
    @Resource
    UserRoleService userRoleService;
    @Override
    public Role getSuperAdmin() {
        Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                .eq(Role::getCode, PermissionRoleEnum.SUPERADMIN.getCode()));
        return role;
    }

    @Transactional
    @Override
    public void addRole(RoleDTO roleDTO) {
        //新增角色
        saveRole(roleDTO);

        /**
         * 如果创建角色的时候添加了权限和人员的话，在这里可以直接完成关联
         */
        //新增角色权限
        addRolePrivilege(roleDTO);
        //新增人员角色
        addUserRole(roleDTO);
    }

    @Override
    public List<Role> queryRoles(RoleDTO roleDTO) {
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = packageQueryWrapper(roleDTO);
        roleLambdaQueryWrapper.orderByDesc(Role::getCreateDate);
        return this.list(roleLambdaQueryWrapper);
    }

    @Override
    public void deleteRole(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        // 在角色表中删除角色
        this.removeByIds(ids);

        // 在用户角色表中删除该角色的关系
        userRoleService.remove(new LambdaQueryWrapper<UserRole>()
                .in(UserRole::getRoleId,ids));
        // 在角色权限表中删除该角色的关系
        rolePrivilegeService.remove(new LambdaQueryWrapper<RolePrivilege>()
                .in(RolePrivilege::getRoleId,ids));

    }

    @Override
    public void updateRole(Role role) {
        if(ObjectUtils.isEmpty(role)){
            return;
        }
        this.updateById(role);
    }

    private LambdaQueryWrapper<Role> packageQueryWrapper(RoleDTO roleDTO){
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.ne(Role::getStatusCd,GlobalEnum.INVALID);
        if(!ObjectUtils.isEmpty(roleDTO.getId())){
            roleLambdaQueryWrapper.eq(Role::getCode,roleDTO.getId());
        }

        if(!ObjectUtils.isEmpty(roleDTO.getType())){
            roleLambdaQueryWrapper.eq(Role::getType,roleDTO.getType());
        }

        if(!ObjectUtils.isEmpty(roleDTO.getCode())){
            roleLambdaQueryWrapper.like(Role::getCode,roleDTO.getCode());
        }

        if(!ObjectUtils.isEmpty(roleDTO.getName())){
            roleLambdaQueryWrapper.like(Role::getName,roleDTO.getName());
        }
        return roleLambdaQueryWrapper;
    }

    /**
     * 新增角色
     * @param roleDTO
     */
    private void saveRole(RoleDTO roleDTO){
        //页面有传id说明不是新增的
        if(!ObjectUtils.isEmpty(roleDTO.getId())){
            return;
        }
        Role newRole = new Role();
        BeanUtilCopy.copyProperties(roleDTO,newRole);
        newRole.setStatusCd(String.valueOf(GlobalEnum.EFFECT.getCode()));
        this.save(newRole);
        roleDTO.setId(newRole.getId());
    }

    /**
     * 新增角色权限
     * @param roleDTO
     */
    private void addRolePrivilege(RoleDTO roleDTO){
        List<Long> privilegeIds = roleDTO.getPrivilegeIds();
        //1、有角色id并且有权限id才是新增
        if(ObjectUtils.isEmpty(roleDTO.getId()) || CollectionUtils.isEmpty(privilegeIds)){
            return;
        }
        //2、查询角色具有的权限id， 此前不存在再新增
        List<RolePrivilege> oleRolePrivileges = rolePrivilegeService.list(new LambdaQueryWrapper<RolePrivilege>()
                .eq(RolePrivilege::getRoleId, roleDTO.getId()).ne(RolePrivilege::getStatusCd, GlobalEnum.INVALID));
        Set<Long> oldPrivilegeIds = oleRolePrivileges.stream().map(RolePrivilege::getPrivilegeId).collect(Collectors.toSet());
        List<RolePrivilege> rolePrivileges = new ArrayList<>();
        for (Long privId : privilegeIds) {
            if (oldPrivilegeIds.contains(privId)) {
                continue;
            }
            RolePrivilege rolePrivilege = new RolePrivilege();
            rolePrivilege.setPrivilegeId(privId);
            rolePrivilege.setRoleId(roleDTO.getId());
            rolePrivilege.setStatusCd(String.valueOf(GlobalEnum.EFFECT.getCode()));
            rolePrivileges.add(rolePrivilege);
        }
        if(CollectionUtils.isEmpty(rolePrivileges)){
            return;
        }
        rolePrivilegeService.saveBatch(rolePrivileges);
    }

    /**
     * 新增人员角色
     * @param roleDTO
     */
    private void addUserRole(RoleDTO roleDTO){
        List<Long> userIds = roleDTO.getUserIds();
        //有角色id并且有人员id才是新增
        if(ObjectUtils.isEmpty(roleDTO.getId()) || CollectionUtils.isEmpty(userIds)){
            return;
        }
        List<UserRole> oleUserRoles = userRoleService.list(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getRoleId, roleDTO.getId()).ne(UserRole::getStatusCd, GlobalEnum.INVALID));
        Set<Long> oldUserIds = oleUserRoles.stream().map(UserRole::getUserId).collect(Collectors.toSet());
        List<UserRole> userRoles = new ArrayList<>();
        for (Long userId : userIds) {
            if (oldUserIds.contains(userId)) {
                continue;
            }
            UserRole userRole = new UserRole();
            userRole.setRoleId(roleDTO.getId());
            userRole.setStatusCd(String.valueOf(GlobalEnum.EFFECT.getCode()));
            userRole.setUserId(userId);
            userRoles.add(userRole);
        }
        userRoleService.saveBatch(userRoles);
    }


}




