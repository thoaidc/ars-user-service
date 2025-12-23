package com.ars.userservice.service.impl;

import com.ars.userservice.entity.OutBox;
import com.ars.userservice.queue.publisher.KafkaProducer;
import com.ars.userservice.repository.OutBoxRepository;
import com.ars.userservice.service.OutBoxService;
import com.dct.model.constants.BaseOutBoxConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class OutBoxServiceImpl implements OutBoxService {
    private final KafkaProducer kafkaProducer;
    private final OutBoxRepository outBoxRepository;
    private static final Logger log = LoggerFactory.getLogger(OutBoxServiceImpl.class);

    public OutBoxServiceImpl(KafkaProducer kafkaProducer, OutBoxRepository outBoxRepository) {
        this.kafkaProducer = kafkaProducer;
        this.outBoxRepository = outBoxRepository;
    }

    @Override
    @Transactional
    public void processOutBoxEvent() {
        List<OutBox> outBoxes = outBoxRepository.findTopOutBoxesByTypeAndStatus(
            BaseOutBoxConstants.Type.USER_CREATED,
            BaseOutBoxConstants.Status.PENDING
        );

        outBoxes = outBoxes.stream().filter(Objects::nonNull).toList();
        outBoxes.forEach(this::sendOutBoxMessage);
        outBoxRepository.saveAll(outBoxes);
    }

    private void sendOutBoxMessage(OutBox outBox) {
        log.info("[SEND_EVENT_FROM_OUTBOX] - sagaId: {}, type: {}", outBox.getSagaId(), outBox.getType());

        try {
            kafkaProducer.sendMessageCreatedUser(outBox.getValue());
            outBox.setStatus(BaseOutBoxConstants.Status.COMPLETION);
        } catch (Exception e) {
            log.error("[SEND_OUTBOX_MESSAGE_ERROR] - sagaId: {}, type: {}", outBox.getSagaId(), outBox.getType(), e);
            outBox.setStatus(BaseOutBoxConstants.Status.FAILED);
            outBox.setError(e.getMessage());
        }
    }
}
