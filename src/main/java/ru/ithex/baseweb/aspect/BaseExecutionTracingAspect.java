package ru.ithex.baseweb.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@ConditionalOnProperty(
        value="app.execution.tracing",
        havingValue = "true")
public abstract class BaseExecutionTracingAspect {
    private final static Logger LOGGER = LoggerFactory.getLogger("");

    @Pointcut
    protected abstract void mainPackagePointcut();

    @Around("mainPackagePointcut()")
    private Object logScheduled(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        LOGGER.info(String.format("Execution tracing: -> %s.%s(..)", signature.getDeclaringTypeName(), signature.getName()));
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        LOGGER.info(String.format("Execution tracing: <- %s.%s(..)  [execution=%s ms]", signature.getDeclaringTypeName(), signature.getName(),endTime - startTime));
        return result;
    }
}
