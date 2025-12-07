package com.ars.userservice.dto.request.user;

import java.util.HashSet;
import java.util.Set;

public class SearchShopInfoRequestDTO {
    private Set<Integer> ownerIds = new HashSet<>();

    public Set<Integer> getOwnerIds() {
        return ownerIds;
    }

    public void setOwnerIds(Set<Integer> ownerIds) {
        this.ownerIds = ownerIds;
    }
}
