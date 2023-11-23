package com.lijinchao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijinchao.entity.UserRole;
import com.lijinchao.service.UserRoleService;
import com.lijinchao.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 时之始
* @description 针对表【user_role(用户角色管理表)】的数据库操作Service实现
* @createDate 2023-11-14 10:02:18
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




