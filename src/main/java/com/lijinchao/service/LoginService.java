package com.lijinchao.service;

import com.lijinchao.entity.User;
import com.lijinchao.utils.BaseApiResult;

public interface LoginService {
    /**
     * 登录
     * @param user
     * @return
     * @throws Exception
     */
    Object doLogin(User user) throws Exception;
}
