package com.siemens.internal.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Aspect
@Configuration
@Slf4j
public class TempoAspect {

    @Around("com.siemens.internal.aspects.CommonJoinPointConfig.controllerMethods()")
    public Object logOnEnterAndExit(ProceedingJoinPoint joinPoint)  throws Throwable {
        log.info("#TempoService : Started {} at {} : ", joinPoint, new Date());
        Object object = joinPoint.proceed();
        log.info("#TempoService : Finished {} at {} : ", joinPoint, new Date());
        return object;
    }

    @AfterThrowing(pointcut = "execution(* com.siemens.internal.service.*.*(..))",
            throwing="ex")
    public void logOnException(ArithmeticException ex) {
        log.error("#TempoService : Error occured : ", ex);
    }

}
