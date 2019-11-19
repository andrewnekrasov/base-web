package ru.ithex.baseweb.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class BaseControllerExceptionAspect {
    private final static Logger LOGGER = LoggerFactory.getLogger("");

    @Pointcut()
    public void controllersAdvicePackagePointcut(){}

    @Before("controllersAdvicePackagePointcut()")
    private void controllerAdviceAspect(JoinPoint joinPoint) {
        final StringBuilder stringBuilder = new StringBuilder("Request error:\n"); //String.format("%s:\n", joinPoint.getSignature().getDeclaringTypeName())
        Object[] args = joinPoint.getArgs();
        Arrays.stream(args).filter(o -> o instanceof HttpServletRequest)
                .map(o -> (HttpServletRequest) o)
                .forEach(request -> {
                    stringBuilder.append(String.format("%s %s\nHeaders:\n", request.getMethod(), request.getRequestURL()));
                    Collections.list(request.getHeaderNames()).forEach(name -> {
                        stringBuilder.append(String.format("    %s: %s\n", name, request.getHeader(name)));
                    });
                    stringBuilder.append("\nBody:");
                    try{
                        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                        stringBuilder.append(String.format("\n%s", requestBody));
                    }
                    catch (Exception e){
                        stringBuilder.append("\nCouldn't read request body");
                    }
                });

        Arrays.stream(args).filter(o -> o instanceof Exception)
                .map(o -> (Exception) o)
                .forEach(e -> LOGGER.error(stringBuilder.toString(),e));
    }
}
