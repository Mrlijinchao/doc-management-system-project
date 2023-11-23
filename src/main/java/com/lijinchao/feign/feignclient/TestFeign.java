package com.lijinchao.feign.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

@Repository
@FeignClient(name = "doc-management-system")
public interface TestFeign {
    @GetMapping("/name")
    String getName();
}
