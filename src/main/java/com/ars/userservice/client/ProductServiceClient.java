package com.ars.userservice.client;

import com.dct.model.dto.response.BaseResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductServiceClient {
    @GetMapping("/api/internal/shop/login-info/{userId}")
    BaseResponseDTO getLoginInfo(@PathVariable("userId") Integer userId);
}
