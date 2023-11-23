package com.lijinchao.controller;

import com.lijinchao.entity.User;
import com.lijinchao.feign.feignclient.DemoClient;
import com.lijinchao.feign.feignclient.TestFeign;
import com.lijinchao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    TestFeign testFeign;

    @Autowired
    DemoClient demoClient;

    @Resource
    UserService userService;

    @GetMapping("/otherName")
    public String getOtherName(){
        return testFeign.getName();
    }

    @GetMapping("/common")
    public String getCommonName(){
        return demoClient.getName();
    }

    @Transactional
    @GetMapping("/user")
    public Object addUser(String name){
        User user = new User();
        user.setBirthtime(new Date());
        user.setCode("1000");
        user.setName(name);
        user.setStatusCd("1001");
        userService.save(user);
        List<User> list = userService.list();
        return list;
    }

}
