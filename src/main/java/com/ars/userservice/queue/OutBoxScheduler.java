package com.ars.userservice.queue;

import com.ars.userservice.service.OutBoxService;
import com.dct.model.constants.BaseOutBoxConstants;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OutBoxScheduler {
    private final OutBoxService outBoxService;

    public OutBoxScheduler(OutBoxService outBoxService) {
        this.outBoxService = outBoxService;
    }

    @Transactional
    @Scheduled(fixedDelay = BaseOutBoxConstants.DELAY_TIME)
    public void sendOutBoxEvent() {
        outBoxService.processOutBoxEvent();
    }
}
