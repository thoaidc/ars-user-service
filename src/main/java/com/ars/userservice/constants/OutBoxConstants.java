package com.ars.userservice.constants;

public interface OutBoxConstants {
    interface Type {
        String REGISTER_USER_WITH_SHOP = "REGISTER_USER_WITH_SHOP";
    }

    interface Status {
        String PENDING = "PENDING";
        String COMPLETION = "COMPLETION";
    }

    int DELAY_TIME = 5000; // 5 seconds
}
