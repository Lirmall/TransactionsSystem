package ru.klokov.tsaccounts.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class UserServiceAspect {

    @Pointcut("execution(public * ru.klokov.tsaccounts.services.UserService.*(..))")
    public void callAtPublicMethods() {
    }

    @Pointcut("execution(private * ru.klokov.tsaccounts.services.UserService.*(..))")
    public void callAtPrivateMethods() {
    }

    @Before("callAtPublicMethods()")
    public void logMethodCall(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Calling method: {}", methodName);
    }

    @AfterThrowing(pointcut = "callAtPublicMethods()", throwing = "ex")
    public void logPublicMethodException(JoinPoint joinPoint, Exception ex) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        String exceptionName = ex.getClass().getSimpleName();
        log.error("Exception \"{}\" in class \"{}\" in public method \"{}\": \"{}\"", exceptionName, className, methodName, ex.getMessage());
    }

    @AfterThrowing(pointcut = "callAtPrivateMethods()", throwing = "ex")
    public void logPrivateMethodException(JoinPoint joinPoint, Exception ex) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        String exceptionName = ex.getClass().getSimpleName();
        log.error("Exception \"{}\" in class \"{}\" in private method \"{}\": \"{}\"", exceptionName, className, methodName, ex.getMessage());
    }
}
