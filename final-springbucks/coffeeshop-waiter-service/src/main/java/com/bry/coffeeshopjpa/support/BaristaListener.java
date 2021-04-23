package com.bry.coffeeshopjpa.support;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BaristaListener {

    @StreamListener(Barista.FINISHED_ORDRS)
    public void finishedOrder(Integer id) {
        log.info("Get finished orders {}", id);
    }
}
