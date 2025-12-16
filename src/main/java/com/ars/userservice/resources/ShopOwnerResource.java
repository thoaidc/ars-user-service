package com.ars.userservice.resources;

import com.ars.userservice.dto.request.user.SearchShopInfoRequestDTO;
import com.ars.userservice.service.UserService;
import com.dct.model.dto.response.BaseResponseDTO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/users")
public class ShopOwnerResource {
    private final UserService userService;

    public ShopOwnerResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/owners-info")
    public BaseResponseDTO getShopsOwnerInfos(@ModelAttribute SearchShopInfoRequestDTO requestDTO) {
        return userService.getShopOwnerInfos(requestDTO.getOwnerIds());
    }

    @GetMapping("/owners-info/{ownerId}")
    public BaseResponseDTO getShopsOwnerInfo(@PathVariable Integer ownerId) {
        return userService.getShopOwnerInfos(ownerId);
    }
}
