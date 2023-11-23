package com.lijinchao.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lijinchao.constant.MessageConstant;
import com.lijinchao.entity.Privilege;
import com.lijinchao.service.PrivilegeService;
import com.lijinchao.utils.BaseApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/privilege")
public class PrivilegeController {

    @Resource
    PrivilegeService privilegeService;

    /**
     * 新增权限
     * @param privilege
     * @return
     */
    @PostMapping("")
    public BaseApiResult addPrivilege(@RequestBody Privilege privilege) {
        try {
            privilegeService.savePrivilege(privilege);
        } catch (Exception e) {
            log.error(e.getMessage());
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.OPERATE_FAILED);
        }
        return BaseApiResult.success("新增权限成功！");
    }

    /**
     * 修改权限
     * @param privilege
     */
    @PutMapping("")
    public BaseApiResult updatePrivilege(@RequestBody Privilege privilege) {
        try {
            privilegeService.updatePrivilege(privilege);
        } catch (Exception e) {
            log.error(e.getMessage());
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.MODIFY_FAILED);
        }
        return BaseApiResult.success(MessageConstant.MODIFY_SUCCESS);
    }

    /**
     * 权限删除
     * @param ids
     * @return
     */
    @DeleteMapping("")
    public BaseApiResult deletePrivileges(@RequestParam List<Long> ids) {
        try {
            privilegeService.batchDelete(ids);
        } catch (Exception e) {
            log.error(e.getMessage());
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.DELETE_FAILED);
        }
        return BaseApiResult.success(MessageConstant.DELETE_SUCCESS);
    }

    /**
     * 分页查询权限
     * @param privilege
     * @return
     */
    @GetMapping("/query")
    public BaseApiResult queryPrivilege(@RequestBody Privilege privilege){
        try {
            Page<Privilege> page = privilegeService.queryPagePrivilege(privilege);
            return BaseApiResult.success(page);
        } catch (Exception e) {
            log.error(e.getMessage());
            return BaseApiResult.error(MessageConstant.PROCESS_ERROR_CODE,MessageConstant.DELETE_FAILED);
        }
    }


}
