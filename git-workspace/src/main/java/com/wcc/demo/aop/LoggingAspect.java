package com.wcc.demo.aop;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    private static final Integer resultSafeLength = 200;
    private static final Integer resultArraySafeLength = 5;
    private final ObjectMapper objectMapper;
    
    public LoggingAspect(ObjectMapper objectMapper) {
    	this.objectMapper = objectMapper;
    }
    
    @Pointcut("execution(* com.wcc.demo..service..*(..)) || "
    		+ "execution(* com.wcc.demo..controller..*(..)) || "
    		+ "execution(* com.wcc.demo..dao..*(..)) || "
    		+ "execution(* com.wcc.demo..processor..*(..)) || "
    		+ "execution(* com.wcc.demo..mapper..*(..))")
    public void applicationLayerMethods() {}

    @Before("applicationLayerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        logger.info("[INBOUND] {}.{}() with args = {}", 
                    signature.getDeclaringTypeName(), 
                    signature.getName(), 
                    previewArray(joinPoint.getArgs(), resultArraySafeLength, resultSafeLength));
    }
    
    private String previewArray(Object[] array, int limit, int strLimit) {
        if (array == null) return "null";

        int len = Math.min(array.length, limit);
        Object[] preview = Arrays.copyOfRange(array, 0, len);
        String result = Arrays.toString(preview);

        if (array.length > limit) {
            result = result.substring(0, result.length() - 1) + ", ...]";
        }

        if(result.length() > strLimit) {
        	result = result.substring(0, strLimit) + ", ...]";
        }
        logger.info("result= " + result);

        return result;
    }
    
    @Around("applicationLayerMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        try {
            Object result = joinPoint.proceed();
            long timeTaken = System.currentTimeMillis() - start;
            logger.info("[METHOD] {}.{}() executed in {} ms", signature.getDeclaringTypeName(), signature.getName(), 
                        timeTaken);
            return result;
        } catch (Throwable ex) {
            logger.error("[METHOD] {}.{}() failed: {}", signature.getDeclaringTypeName(), signature.getName(), 
                         ex.getMessage(), ex);
            throw ex;
        }
    }
    
    @AfterReturning(pointcut = "applicationLayerMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

		try {
			objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
			String jsonResult = objectMapper.writeValueAsString(result);
			
	        logger.info("[OUTBOUND] {} Method {} executed with result: {}", signature.getDeclaringTypeName(), joinPoint.getSignature(), jsonResult);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("json object cannot be displayed");
			
	        logger.info("[OUTBOUND] {}.{}() returned = {}", 
                    signature.getDeclaringTypeName(), 
                    signature.getName(), 
                    previewArray(result, resultSafeLength));
		}
    }

    private String previewArray(Object result, int limit) {
        if (result == null) return "null";

        if(result.toString().length() > limit) {
        	return result.toString().substring(0, limit) + ", ...]";
        }

        return result.toString();
    }
    
    @AfterThrowing(pointcut = "applicationLayerMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        logger.error("[ERROR] {}.{}() threw exception: {}", 
                     signature.getDeclaringTypeName(), 
                     signature.getName(), 
                     ex.getMessage(), ex);
    }
}
