package com.lijinchao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lijinchao.entity.Privilege;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 时之始
* @description 针对表【privilege(权限表)】的数据库操作Service
* @createDate 2023-11-14 10:01:47
*/
public interface PrivilegeService extends IService<Privilege> {
    /**
     * 新增权限
     * @param privilege
     */
    void savePrivilege(Privilege privilege);

    /**
     * 修改权限
     * @param privilege
     */
    void updatePrivilege(Privilege privilege);

    /**
     * 删除权限
     * @param ids
     */
    void batchDelete(List<Long> ids);

    /**
     * 分页查询权限
     * @param privilege
     * @return
     */
    Page<Privilege> queryPagePrivilege(Privilege privilege);

}
