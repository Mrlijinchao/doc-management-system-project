package com.lijinchao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lijinchao.entity.Privilege;
import com.lijinchao.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lijinchao.entity.dto.UserDTO;
import com.lijinchao.permission.PermissionEnum;
import com.lijinchao.utils.BaseApiResult;

import java.util.List;

/**
* @author 时之始
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-11-13 22:28:38
*/
public interface UserService extends IService<User> {
    /**
     * 新增用户
     * @param users
     * @return
     */
    BaseApiResult batchSaveUsers(List<User> users) throws Exception;

    /**
     * 检查用户是否拥有权限
     * @param user
     * @param permissions
     * @return
     */
    Boolean checkPermissionForUser(User user, List<String> permissions);

    /**
     * 删除用户（可批量）
     * @param userIds
     * @return
     */
    BaseApiResult delUsersById(List<Long> userIds);

    /**
     * 注销用户账号
     * @param token
     * @return
     */
    BaseApiResult closeAccount(String token);

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    BaseApiResult modifyUserInfo(User user,String token);

    /**
     * 修改用户密码
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @return
     */
    BaseApiResult modifyPassword(Long userId,String oldPassword,String newPassword);

    /**
     * 多条件分页查询
     * @param userDTO
     * @return
     */
    Page<User> multiConditionalPageQuery(UserDTO userDTO);

    /**
     * 根据用户Id查询用户所有权限
     * @param userIds
     * @return
     */
    List<Privilege> getUserPrivilegesByIds(List<Long> userIds);

}
