package com.example.common.aop;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
public class WebLogAspect {
    private static final Logger log = LoggerFactory.getLogger(WebLogAspect.class);

    @Around("@annotation(com.example.common.annotation.WebLog)") // 切点：带有@WebLog注解的方法
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new IllegalStateException("RequestAttributes is null");
        }
        HttpServletRequest request = attributes.getRequest();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        String params = JSON.toJSONString(request.getParameterMap()); // 查询参数

        // 获取方法参数
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        String methodParams = Arrays.stream(paramNames)
                .map(paramName -> paramName + ": " + safeToString(joinPoint.getArgs()[Arrays.asList(paramNames).indexOf(paramName)]))
                .collect(Collectors.joining(", ")); // 方法参数

        // 执行方法并获取结果
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();

        // 打印日志
        log.error("=== 接口请求开始 ===");
        log.info("URI: {}", uri);
        log.info("Method: {}", method);
        log.info("查询参数: {}", params);
        log.info("方法参数名: {}", Arrays.toString(paramNames));
        log.info("方法参数值: {}", methodParams);
        log.info("返回结果: {}", safeToString(result));
        log.info("耗时: {}ms", endTime - startTime);
        log.error("=== 接口请求结束 ===");

        return result;
    }

    private String safeToString(Object obj) {
        try {
            return JSON.toJSONString(obj);
        } catch (Exception e) {
            log.warn("Failed to serialize object: {}", obj.getClass().getSimpleName(), e);
            return obj == null ? "null" : obj.toString();
        }
    }
}