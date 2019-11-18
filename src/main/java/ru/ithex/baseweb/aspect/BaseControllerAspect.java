package ru.ithex.baseweb.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import ru.ithex.baseweb.model.Validation;

import java.util.Arrays;

public abstract class BaseControllerAspect {
    private final static Logger LOGGER = LoggerFactory.getLogger("");

    private final ObjectMapper objectMapper;

    public BaseControllerAspect(
            ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Pointcut()
    public void controllersPackagePointcut(){}

    @Before("controllersPackagePointcut()")
    private void controllerAspect(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        StringBuilder stringBuilder = new StringBuilder(String.format("Request %s.%s(..):\n", signature.getDeclaringTypeName(), signature.getName()));
        String[] paramNames = signature.getParameterNames();
        for (int paramNamesCounter = 0; paramNamesCounter < paramNames.length; paramNamesCounter++){
            stringBuilder.append(String.format("    -> %s=%s\n", paramNames[paramNamesCounter], readArgValue(joinPoint.getArgs()[paramNamesCounter])));
        }
        LOGGER.info(stringBuilder.toString());
        Arrays.stream(joinPoint.getArgs()).filter(o -> o instanceof Validation).map(o -> (Validation) o).forEach(Validation::validate);
    }
    private String readArgValue(Object arg){
        String result = null;
        try{
            result = objectMapper.writeValueAsString(arg);
        }catch (JsonProcessingException e){
            result = "Couldn't read value";
        }
        return result;
    }

    @AfterReturning(
            pointcut = "controllersPackagePointcut() && !@annotation(ru.ithex.baseweb.aspect.NoLoggingResponse)",
            returning = "result"
    )
    private void logReturnObject(Object result){
        if (!(result instanceof byte[]))
            if (result instanceof ResponseEntity) {
                LOGGER.info("Response\nHeaders:{}\n", readArgValue(((ResponseEntity) result).getHeaders()));
                if (((ResponseEntity) result).getBody() instanceof  byte[]){
                    LOGGER.info("Response\nBody: byte stream\n");
                } else{
                    LOGGER.info("Response\nBody:{}\n", readArgValue(((ResponseEntity) result).getBody()));
                }
            } else
                LOGGER.info("Response\n{}", readArgValue(result));
        else{
            LOGGER.info("Response\nbyte stream");
        }
    }
}
