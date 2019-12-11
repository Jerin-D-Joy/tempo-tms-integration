package com.siemens.internal.aspects;

import org.aspectj.lang.annotation.Pointcut;

public class CommonJoinPointConfig {

    @Pointcut("execution (* com.siemens.internal.controllers.*.*(..))")
    public void controllerMethods() {}

}
