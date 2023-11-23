package com.lijinchao.service.impl;

import com.alibaba.fastjson.spi.Module;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijinchao.constant.MessageConstant;
import com.lijinchao.entity.*;
import com.lijinchao.entity.dto.MenuDTO;
import com.lijinchao.enums.GlobalEnum;
import com.lijinchao.mapper.PrivilegeMapper;
import com.lijinchao.permission.PermissionEnum;
import com.lijinchao.service.MenuService;
import com.lijinchao.mapper.MenuMapper;
import com.lijinchao.service.RolePrivilegeService;
import com.lijinchao.service.RoleService;
import com.lijinchao.service.UserService;
import com.lijinchao.utils.BaseApiResult;
import com.lijinchao.utils.BeanUtilCopy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author 时之始
* @description 针对表【menu(菜单表)】的数据库操作Service实现
* @createDate 2023-11-14 10:01:30
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{

    @Resource
    PrivilegeMapper privilegeMapper;

    @Resource
    MenuMapper menuMapper;

    @Resource
    RoleService roleService;

    @Resource
    RolePrivilegeService rolePrivilegeService;

    @Resource
    UserService userService;

    @Override
    @Transactional
    public BaseApiResult addMenu(Menu menu) {
        //1、新增菜单对应权限, 每个菜单都有对应的菜单权限
        Privilege privilege = new Privilege();
        privilege.setType(PermissionEnum.MENU.getCode());
        privilege.setPath("#");
        privilege.setStatusCd(GlobalEnum.EFFECT.getCode());
        privilege.setName(menu.getName());
        privilegeMapper.insert(privilege);
        //2、新增菜单
        menu.setPrivilegeId(privilege.getId());
        menu.setStatusCd(GlobalEnum.EFFECT.getCode());
        menuMapper.insert(menu);
        //3、给系统超级管理员加上该权限
        Role role = roleService.getSuperAdmin();
        RolePrivilege rolePrivilege = new RolePrivilege();
        rolePrivilege.setRoleId(role.getId());
        rolePrivilege.setPrivilegeId(privilege.getId());
        rolePrivilege.setStatusCd(GlobalEnum.EFFECT.getCode());
        rolePrivilegeService.save(rolePrivilege);

        return BaseApiResult.success("新增菜单成功！");
    }

    @Transactional
    @Override
    public BaseApiResult updateMenu(Menu menu) {
        if (ObjectUtils.isEmpty(menu)) {
            return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE,MessageConstant.DATA_IS_NULL);
        }
        Menu saved = menuMapper.selectOne(new LambdaQueryWrapper<Menu>().eq(Menu::getId, menu.getId())
                .ne(Menu::getStatusCd, GlobalEnum.INVALID));
        if (ObjectUtils.isEmpty(saved)) {
            return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE,"菜单不存在！");
        }
        //1、修改菜单信息
        menuMapper.updateById(menu);
        //2、如果变更菜单名称,则对应的变更权限名称
        if (!StringUtils.isEmpty(menu.getName()) && !menu.getName().equals(saved.getName())) {
            Privilege privilege = new Privilege();
            privilege.setId(saved.getPrivilegeId());
            privilege.setName(menu.getName());
            privilegeMapper.updateById(privilege);
        }
        return null;
    }

    @Override
    public void batchUpdateMenus(List<Menu> menus) {
        this.updateBatchById(menus);
    }

    @Override
    public void deleteMenus(List<Long> menuIds) {
        //1、查询这些菜单的详细信息
        if(CollectionUtils.isEmpty(menuIds)){
            return ;
        }
        List<Menu> menus = menuMapper.selectBatchIds(menuIds);
        if(CollectionUtils.isEmpty(menus)){
            return ;
        }
        //1.1 删除菜单
        menuMapper.deleteBatchIds(menus);
        //1.2 删除菜单对应的权限（防止角色配置查询权限时候干扰用户）
        Set<Long> privilegeIds = menus.stream().map(Menu::getPrivilegeId).collect(Collectors.toSet());
        privilegeMapper.deleteBatchIds(privilegeIds);
    }

    @Override
    public List<MenuDTO> queryAll() {

        //1、查询系统所有菜单
        List<Menu> menus = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .ne(Menu::getStatusCd, GlobalEnum.INVALID));
        if (CollectionUtils.isEmpty(menus)) {
            return Collections.emptyList();
        }
        // 将Menu拷贝为MenuDTO
        List<MenuDTO> menuDTOS = BeanUtilCopy.copyListProperties(menus, MenuDTO::new);
//        有第三方路径的时候使用
//        //2、补充菜单全路径
//        List<Module> modules = moduleService.querySysModules(null);
//        for (Menu menu : menuList) {
//            fullPath(menu, modules);
//        }
        //按照父子层级排序
        setTreeShape(menuDTOS);
        return menuDTOS;
    }

    @Override
    public List<MenuDTO> queryMenu(LambdaQueryWrapper<Menu> lambdaQueryWrapper) {
        List<Menu> menus = menuMapper.selectList(lambdaQueryWrapper);
        // 将Menu拷贝为MenuDTO
        List<MenuDTO> menuDTOS = BeanUtilCopy.copyListProperties(menus, MenuDTO::new);

        return menuDTOS;
    }

    @Override
    public List<MenuDTO> queryMenuByUser(Long userId) {
        //1、根据用户id查询用户权限
        Set<Long> priIds = new HashSet<>();

        List<Privilege> userPrivileges = userService.getUserPrivilegesByIds(Collections.singletonList(userId));
        if(!CollectionUtils.isEmpty(userPrivileges)){
            Set<Long> ids = userPrivileges.stream().map(Privilege::getId).collect(Collectors.toSet());
            priIds.addAll(ids);
        }

        if(CollectionUtils.isEmpty(priIds)){
            return Collections.emptyList();
        }
        List<Menu> menus = menuMapper.selectList(new LambdaQueryWrapper<Menu>().in(Menu::getPrivilegeId, priIds));
        if(CollectionUtils.isEmpty(priIds)){
            return Collections.emptyList();
        }
        // 将Menu拷贝为MenuDTO
        List<MenuDTO> menuDTOS = BeanUtilCopy.copyListProperties(menus, MenuDTO::new);
        //按照父子层级排序
        setTreeShape(menuDTOS);
        return menuDTOS;
    }

    /**
     * 生成树
     *
     * @param newMenuList
     */
    public void setTreeShape(List<MenuDTO> newMenuList) {
        if (CollectionUtils.isEmpty(newMenuList)) {
            return;
        }
        //map key 父id list子对象
        Map<Long, List<MenuDTO>> treeMap = new HashMap<>();
        for (MenuDTO menu : newMenuList) {
            Long parentId = menu.getParentId();
            if (null == parentId) {
                continue;
            }
            List<MenuDTO> children = Optional.ofNullable(treeMap.get(parentId)).orElse(new ArrayList<>());
            children.add(menu);
            treeMap.put(parentId, children);
        }
        List<MenuDTO> delItems = new ArrayList<>();
        for (MenuDTO menu : newMenuList) {
            List<MenuDTO> children = treeMap.get(menu.getId());
            if (CollectionUtils.isEmpty(children)) {
                continue;
            }
            //子级排序
            sortMenu(children);
            menu.setChild(children);
            delItems.addAll(children);
        }
        for (MenuDTO menu : delItems) {
            newMenuList.remove(menu);
        }

    }

    /**
     * 菜单排序
     *
     * @param
     */
    public void sortMenu(List<MenuDTO> menus) {
        if (!CollectionUtils.isEmpty(menus)) {
            //按照权重排序
            Comparator<MenuDTO> byWeight = Comparator.comparingLong(Menu::getOrderNum);
            //按照创建时间
            Comparator<MenuDTO> byId = Comparator.comparing(MenuDTO::getId).reversed();
            menus.sort(byWeight.thenComparing(byId));
        }
    }

}




