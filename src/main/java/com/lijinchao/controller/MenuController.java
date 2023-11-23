package com.lijinchao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lijinchao.constant.MessageConstant;
import com.lijinchao.entity.Menu;
import com.lijinchao.entity.User;
import com.lijinchao.enums.GlobalEnum;
import com.lijinchao.permission.Permission;
import com.lijinchao.permission.PermissionRoleEnum;
import com.lijinchao.service.MenuService;
import com.lijinchao.utils.BaseApiResult;
import com.lijinchao.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    MenuService menuService;

    @Resource
    RedisUtil redisUtil;

    /**
     * 新增菜单
     * @param menu
     * @return
     */
    @Permission(roleValue = {PermissionRoleEnum.ADMIN,PermissionRoleEnum.SUPERADMIN})
    @PostMapping("")
    public BaseApiResult addMenu(@RequestBody Menu menu) {
        try {
            return menuService.addMenu(menu);
        } catch (Exception e) {
            log.error("新增菜单报错："+e.getMessage());
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.OPERATE_FAILED);
        }
    }

    /**
     * 更新菜单信息
     * @param menu
     * @return
     */
    @PutMapping("")
    public BaseApiResult updateMenu(@RequestBody Menu menu) {
        try {
            return menuService.updateMenu(menu);
        } catch (Exception e) {
            log.error("修改菜单报错："+e.getMessage());
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.OPERATE_FAILED);
        }
    }

    /**
     * 批量更新菜单，一般用于排序
     * @param menus
     * @return
     */
    @PutMapping("/batchUpdate")
    public BaseApiResult batchUpdateMenu(@RequestBody List<Menu> menus) {
        try {
            menuService.batchUpdateMenus(menus);
        } catch (Exception e) {
            log.error("批量修改菜单报错："+e.getMessage());
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.OPERATE_FAILED);
        }
        return BaseApiResult.success("修改成功！");
    }

    /**
     * 批量删除菜单
     * @param menuIds
     * @return
     */
    @DeleteMapping("/batchDelete")
    public BaseApiResult deleteMenus(@RequestBody List<Long> menuIds) {
        try {
            menuService.deleteMenus(menuIds);
        } catch (Exception e) {
            log.error("batchDelete："+e.getMessage());
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.OPERATE_FAILED);
        }
        return BaseApiResult.success("删除成功！");
    }

    /**
     * 查询所有菜单按照层级显示
     * @return
     */
    @Permission(roleValue = {PermissionRoleEnum.ADMIN,PermissionRoleEnum.SUPERADMIN})
    @GetMapping("/queryMenuToTree")
    public BaseApiResult queryAllMenus() {
        try {
            return BaseApiResult.success(menuService.queryAll());
        } catch (Exception e) {
            log.error("queryMenuToTree：", e);
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.OPERATE_FAILED);
        }
    }

    /**
     * @description 查询全部菜单 不分层级（首页定位，页面自动跳转）
     */
    @GetMapping("/queryAllMenu")
    public BaseApiResult queryMenu(){
        try{
            return BaseApiResult.success(menuService.queryMenu(new LambdaQueryWrapper<Menu>()
                    .ne(Menu::getStatusCd, GlobalEnum.INVALID)));
        } catch (Exception e) {
            log.error("queryAllMenu" + e.getMessage());
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.OPERATE_FAILED);
        }
    }

    /**
     * @description 查询用户有权限的菜单
     */
    @GetMapping("/queryMenuByUser")
    public BaseApiResult queryMenuByUser(HttpServletRequest request){
        try{
            String token = request.getHeader("authorization");
            User user = (User)redisUtil.get(token);
            if(ObjectUtils.isEmpty(user)){
                return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.OPERATE_FAILED);
            }
            return BaseApiResult.success(menuService.queryMenuByUser(user.getId()));
        } catch (Exception e) {
            log.error("queryMenuByUser" + e.getMessage());
           return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.OPERATE_FAILED);
        }
    }


}
