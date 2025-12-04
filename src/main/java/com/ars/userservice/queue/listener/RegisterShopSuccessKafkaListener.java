package com.ars.userservice.queue.listener;

import com.ars.userservice.service.UserService;
import com.dct.model.common.JsonUtils;
import com.dct.model.constants.BaseKafkaConstants;
import com.dct.model.event.UserShopCompletionEvent;

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
public class RegisterShopSuccessKafkaListener {
    private static final Logger log = LoggerFactory.getLogger(RegisterShopSuccessKafkaListener.class);
    private final UserService userService;

    public RegisterShopSuccessKafkaListener(UserService userService) {
        this.userService = userService;
    }

    @KafkaListener(
        topics = BaseKafkaConstants.Topic.USER_REGISTER_SHOP_COMPLETION,
        groupId = BaseKafkaConstants.GroupId.USER_REGISTER_SHOP_COMPLETION,
        concurrency = BaseKafkaConstants.Consumers.USER_REGISTER_SHOP_COMPLETION
    )
    public void receiveMessage(
        @Payload String payload,
        @Header(name = KafkaHeaders.RECEIVED_KEY, required = false) String ignoredKey,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int ignoredPartition,
        Acknowledgment ack
    ) {
        log.info("[HANDLE_USER_REGISTER_SHOP_COMPLETION_EVENT] - message payload: {}", payload);

        if (Objects.isNull(payload)) {
            log.error("[HANDLE_USER_REGISTER_SHOP_COMPLETION_EVENT_FAILED] - message payload is null");
            ack.acknowledge();
            return;
        }

        try {
            UserShopCompletionEvent userShopCompletionEvent = JsonUtils.parseJson(payload, UserShopCompletionEvent.class);

            if (Objects.isNull(userShopCompletionEvent) || Objects.isNull(userShopCompletionEvent.getUserId())) {
                log.error("[HANDLE_USER_REGISTER_SHOP_COMPLETION_EVENT_FAILED] - event content or userId is null");
                ack.acknowledge();
                return;
            }

            userService.updateRegisterUserWithShopCompletion(userShopCompletionEvent);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("[HANDLE_USER_REGISTER_SHOP_COMPLETION_EXCEPTION] - error: {}. Retry later", e.getMessage(), e);
        }
    }
}
