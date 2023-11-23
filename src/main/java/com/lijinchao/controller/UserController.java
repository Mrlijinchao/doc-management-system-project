package com.lijinchao.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lijinchao.constant.MessageConstant;
import com.lijinchao.entity.User;
import com.lijinchao.entity.dto.UserDTO;
import com.lijinchao.enums.GlobalEnum;
import com.lijinchao.permission.Permission;
import com.lijinchao.permission.PermissionRoleEnum;
import com.lijinchao.service.UserService;
import com.lijinchao.utils.BaseApiResult;
import com.lijinchao.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 用户模块
 * 关于用户的一些操作
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @Resource
    RedisUtil redisUtil;

    /**
     * 获取当前用户信息
     * @param request
     * @return
     */
    @GetMapping("/queryCurrentUser")
    public BaseApiResult queryCurrentUser(HttpServletRequest request){
        String token = request.getHeader("authorization");
        return BaseApiResult.success(redisUtil.get(token));
    }

    /**
     * 新增用户
     * @param users
     * @return
     */
    @PostMapping("")
    public BaseApiResult addUser(@RequestBody List<User> users) {
        try {
            return userService.batchSaveUsers(users);
        } catch (Exception e) {
            log.error("addUser fail", e);
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,e.getMessage());
        }
    }

    /**
     * 管理员删除用户
     * @return
     */
    @Transactional
    @Permission(roleValue = {PermissionRoleEnum.ADMIN,PermissionRoleEnum.SUPERADMIN})
    @DeleteMapping("")
    public BaseApiResult delUser(@RequestBody List<Long> userIds,HttpServletRequest request){
        try {
            // 清空redis里面的用户信息
            String token = request.getHeader("authorization");
            redisUtil.del(token);
            return userService.delUsersById(userIds);
        }catch (Exception e){
            e.printStackTrace();
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.OPERATE_FAILED);
        }
    }

    /**
     * 用户注销自己的账号
     * @param request
     * @return
     */
    @DeleteMapping("/closeAccount")
    public BaseApiResult closeAccount(HttpServletRequest request){
        String token = request.getHeader("authorization");
        return userService.closeAccount(token);
    }

    /**
     * 恢复用户账号
     * @param userId
     * @return
     */
    @Transactional
    @Permission(roleValue = {PermissionRoleEnum.ADMIN,PermissionRoleEnum.SUPERADMIN})
    @PostMapping("/recoverAccount")
    public BaseApiResult recoverAccount(Long userId){
        if(ObjectUtils.isEmpty(userId)){
            return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE,MessageConstant.DATA_IS_NULL);
        }
        boolean b = userService.update(new LambdaUpdateWrapper<User>()
                .set(User::getStatusCd, GlobalEnum.EFFECT.getCode()));
        if(b){
            return BaseApiResult.success("用户账号已恢复");
        }
        return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.OPERATE_FAILED);
    }

    /**
     * 修改用户信息，只有管理员和本人才有权限修改
     * @param user
     * @param request
     * @return
     */
    @PutMapping("/modify")
    public BaseApiResult modifyUser(@RequestBody User user,HttpServletRequest request){
        String token = request.getHeader("authorization");
        return userService.modifyUserInfo(user,token);
    }

    /**
     * 修改用户密码
     * @param userPasswordInfo
     * @return
     */
    @PutMapping("/modifyPassword")
    public BaseApiResult modifyPassword(@RequestBody Map<String,String> userPasswordInfo){
        Long userId = Long.parseLong(userPasswordInfo.get("userId"));
        String oldPassword = userPasswordInfo.get("oldPassword");
        String newPassword = userPasswordInfo.get("newPassword");
        if(ObjectUtils.isEmpty(userId) || ObjectUtils.isEmpty(oldPassword)
                || ObjectUtils.isEmpty(newPassword)){
            return BaseApiResult.error(MessageConstant.PARAMS_ERROR_CODE,MessageConstant.DATA_IS_NULL);
        }
        return userService.modifyPassword(userId,oldPassword,newPassword);
    }


    /**
     * 多条件分页查询用户信息
     * @param userDTO
     * @return
     */
    @GetMapping("/query")
    public BaseApiResult pageQuery(@RequestBody UserDTO userDTO){
        try {
            Page<User> userPage = userService.multiConditionalPageQuery(userDTO);
            return BaseApiResult.success(userPage);
        } catch (Exception e){
            e.printStackTrace();
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.OPERATE_FAILED);
        }
    }





}
