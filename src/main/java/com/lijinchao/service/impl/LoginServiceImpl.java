package com.lijinchao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lijinchao.entity.User;
import com.lijinchao.enums.GlobalEnum;
import com.lijinchao.service.LoginService;
import com.lijinchao.service.UserService;
import com.lijinchao.utils.BaseApiResult;
import com.lijinchao.utils.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    UserService userService;

    @Resource
    RedisUtil redisUtil;
    @Override
    public Object doLogin(User user) throws Exception {

        if(StringUtils.isEmpty(user.getCode())){
            throw new Exception("用户名不能为空");
        }

        User one = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getCode, user.getCode()));
        if(ObjectUtils.isEmpty(one)){
            throw new Exception("账号不存在，请联系系统管理员注册之后才能登录");
        }

        if(one.getStatusCd().equals(GlobalEnum.INVALID.getCode())){
            throw new Exception("账号已注销，请联系系统管理员恢复账号才能登录");
        }

        if(!user.getPassword().equals(one.getPassword())){
            throw new Exception("密码错误！");
        }

        String token = "Bearer " + UUID.randomUUID().toString().replaceAll("-", "");
        redisUtil.set(token,one,3, TimeUnit.HOURS);

        return BaseApiResult.returnToken(user,token);
    }
}
