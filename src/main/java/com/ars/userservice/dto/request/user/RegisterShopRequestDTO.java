package com.ars.userservice.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterShopRequestDTO extends RegisterRequestDTO {
    @NotBlank(message = "Shop name must not be null")
    @Size(min = 2, message = "Shop name min 2 characters")
    @Size(max = 250, message = "Shop name max 250 character")
    private String shopName;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
