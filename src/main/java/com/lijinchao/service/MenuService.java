package com.lijinchao.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lijinchao.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lijinchao.entity.dto.MenuDTO;
import com.lijinchao.utils.BaseApiResult;

import java.util.List;

/**
* @author 时之始
* @description 针对表【menu(菜单表)】的数据库操作Service
* @createDate 2023-11-14 10:01:30
*/
public interface MenuService extends IService<Menu> {

    /**
     * 新增菜单
     * @param menu
     * @return
     */
    BaseApiResult addMenu(Menu menu);

    /**
     * 更新菜单
     * @param menu
     * @return
     */
    BaseApiResult updateMenu(Menu menu);

    /**
     * 批量更新菜单
     * @param menus
     * @return
     */
    void batchUpdateMenus(List<Menu> menus);

    /**
     * 批量删除菜单
     * @param menuIds
     * @return
     */
    void deleteMenus(List<Long> menuIds);

    /**
     * 查询所有菜单，按层级显示
     * @return
     */
    List<MenuDTO> queryAll();

    /**
     *  查询所有菜单
     * @return
     */
    List<MenuDTO> queryMenu(LambdaQueryWrapper<Menu> lambdaQueryWrapper);

    /**
     * 按权限查询用户菜单
     * @param userId
     * @return
     */
    List<MenuDTO> queryMenuByUser(Long userId);

}
