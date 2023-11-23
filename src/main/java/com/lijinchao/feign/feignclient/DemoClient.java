package com.lijinchao.feign.feignclient;



import com.lijinchao.openFeignFacade.systemFacade.DemoFacade;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "doc-management-system")
public interface DemoClient extends DemoFacade {
}
