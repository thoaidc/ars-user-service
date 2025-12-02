package com.ars.userservice.queue.listener;

import com.ars.userservice.service.UserService;
import com.dct.model.common.JsonUtils;
import com.dct.model.constants.BaseKafkaConstants;
import com.dct.model.event.UserShopFailureEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RegisterShopFailureKafkaListener {
    private static final Logger log = LoggerFactory.getLogger(RegisterShopFailureKafkaListener.class);
    private final UserService userService;

    public RegisterShopFailureKafkaListener(UserService userService) {
        this.userService = userService;
    }

    @KafkaListener(
        topics = BaseKafkaConstants.Topic.CREATE_USER_SHOP_FAILURE,
        groupId = BaseKafkaConstants.GroupId.CREATE_USER_SHOP_FAILURE,
        concurrency = BaseKafkaConstants.Consumers.CREATE_USER_SHOP_FAILURE
    )
    public void receiveMessage(
        @Payload String payload,
        @Header(name = KafkaHeaders.RECEIVED_KEY, required = false) String ignoredKey,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int ignoredPartition,
        Acknowledgment ack
    ) {
        log.info("[HANDLE_CREATE_USER_SHOP_FAILURE_EVENT] - message payload: {}", payload);

        if (Objects.isNull(payload)) {
            log.error("[HANDLE_ROLLBACK_CREATE_USER_WITH_SHOP_FAILED] - message payload is null");
            ack.acknowledge();
            return;
        }

        try {
            UserShopFailureEvent userShopFailureEvent = JsonUtils.parseJson(payload, UserShopFailureEvent.class);

            if (Objects.isNull(userShopFailureEvent) || Objects.isNull(userShopFailureEvent.getUserId())) {
                log.error("[HANDLE_ROLLBACK_CREATE_USER_WITH_SHOP_FAILED] - event content or userId is null");
                ack.acknowledge();
                return;
            }

            userService.rollbackRegisterUserWithShopFailure(userShopFailureEvent);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("[HANDLE_ROLLBACK_CREATE_USER_WITH_SHOP_EXCEPTION] - error. {}. Retry later", e.getMessage(), e);
        }
    }
}
