package com.tousie.securities.port;

import com.tousie.securities.common.Randoms;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class RequestIdGenerator {

    private static final String REQUEST_ID_RANGE = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM0123456789";

    public String generateId() {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        return Randoms.ofRange(threadLocalRandom, REQUEST_ID_RANGE, 15);
    }
}
