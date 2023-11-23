package com.lijinchao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lijinchao.entity.Privilege;
import com.lijinchao.service.PrivilegeService;
import com.lijinchao.mapper.PrivilegeMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
* @author 时之始
* @description 针对表【privilege(权限表)】的数据库操作Service实现
* @createDate 2023-11-14 10:01:47
*/
@Service
public class PrivilegeServiceImpl extends ServiceImpl<PrivilegeMapper, Privilege>
    implements PrivilegeService{

    @Override
    public void savePrivilege(Privilege privilege) {
        if(ObjectUtils.isEmpty(privilege)){
            return;
        }
        this.save(privilege);
    }

    @Override
    public void updatePrivilege(Privilege privilege) {
        if(ObjectUtils.isEmpty(privilege)){
            return;
        }
        this.updateById(privilege);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        this.removeByIds(ids);
    }

    @Override
    public Page<Privilege> queryPagePrivilege(Privilege privilege) {
        if(ObjectUtils.isEmpty(privilege)){
            return new Page<>();
        }
        Page<Privilege> page = new Page<>(1,10);
        if(!ObjectUtils.isEmpty(privilege.getPageNum())){
            page.setCurrent(privilege.getPageNum());
        }
        if(!ObjectUtils.isEmpty(privilege.getPageSize())){
            page.setSize(privilege.getPageSize());
        }
        LambdaQueryWrapper<Privilege> privilegeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(!ObjectUtils.isEmpty(privilege.getName())){
            privilegeLambdaQueryWrapper.like(Privilege::getName,privilege.getName());
        }
        if(!ObjectUtils.isEmpty(privilege.getType())){
            privilegeLambdaQueryWrapper.like(Privilege::getType,privilege.getType());
        }
        if(!ObjectUtils.isEmpty(privilege.getCode())){
            privilegeLambdaQueryWrapper.like(Privilege::getCode,privilege.getCode());
        }
        Page<Privilege> rivilegePage = this.page(page, privilegeLambdaQueryWrapper);
        return rivilegePage;
    }
}




