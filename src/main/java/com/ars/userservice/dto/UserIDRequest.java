package com.ars.userservice.dto;

import java.util.ArrayList;
import java.util.List;

public class UserIDRequest {
    private List<Integer> ids = new ArrayList<>();

    public UserIDRequest() {}

    public UserIDRequest(List<Integer> ids) {
        this.ids = ids;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
