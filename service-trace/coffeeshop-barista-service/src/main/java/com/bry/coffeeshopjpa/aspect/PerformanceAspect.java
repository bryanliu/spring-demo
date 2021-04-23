package com.bry.coffeeshopjpa.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    @Around("repositoryOps()")
    public Object logPerformance(ProceedingJoinPoint pjp) throws Throwable {

        long startTime = System.currentTimeMillis();
        String name = pjp.getSignature().toString();
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        } finally {
            long endTime = System.currentTimeMillis();
            log.info("Execute {}, time {}", name, endTime - startTime);
        }

    }

    @Pointcut("execution(* com.bry.coffeeshopjpa.repository..*(..))")
    private void repositoryOps() {

    }
}
