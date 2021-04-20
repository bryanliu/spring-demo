package com.example.eurekaserver.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class CricuitBreakerAspect {

    private int THRESHOLD = 3;
    private Map<String, AtomicInteger> count = new ConcurrentHashMap<>();
    private Map<String, AtomicInteger> breakCount = new ConcurrentHashMap<>();

    @Around("execution(* com.example.eurekaserver.integration..*(..))")
    public Object processCricuitBreak(ProceedingJoinPoint pjp) throws Throwable {

        String signature = pjp.getSignature().toLongString();
        // 如果失败次数大于阈值，就中断阈值次数，然后再试，如果还是不行，就再中断。
        if (count.containsKey(signature)) {
            if (count.get(signature).get() > THRESHOLD
                    && breakCount.get(signature).get() < THRESHOLD) {
                breakCount.get(signature).incrementAndGet();
                return null;
            }
        } else {
            count.put(signature, new AtomicInteger(0));
            breakCount.put(signature, new AtomicInteger(0));
        }

        Object retVal;
        try {
            retVal = pjp.proceed();
            log.info("invoke {} successfully", signature);
            breakCount.get(signature).set(0);
            count.get(signature).set(0);
            return retVal;
        } catch (Exception e) {
            breakCount.get(signature).set(0);

            log.error("Encounter {} errors when invoke RPC call",
                    count.get(signature).incrementAndGet(), e);
            throw e;
        }

    }
}
