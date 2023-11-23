package com.lijinchao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

@MapperScan("com.lijinchao.mapper")
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.lijinchao.feign.feignclient"})
public class DocManagementSystemProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DocManagementSystemProjectApplication.class, args);
    }

}
