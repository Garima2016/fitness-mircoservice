package com.fitness.userservice.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {


/*
    //return type of method and absalute path of method
    @Before("execution(* com.fitness.userservice.service.UserService.getUserProfile(..))")
    public void log(){
        System.out.println("aspect is called before");
    }
    @After("execution(* com.fitness.userservice.service.UserService.getUserProfile(..))")
    public void logAfter(){
        System.out.println("aspect is called after");
    }
    @AfterReturning
    @AfterThrowing
*/

    //@Pointcut("within(com.fitness.userservice.service.UserService)")  //within userService class
    //@Pointcut("@within(org.springframework.stereotype.Service)")  //within the class mark as service annotation
    //@Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")  //within the  mark post annotation
    //@Pointcut("@target(org.springframework.stereotype.Service)") //within the clss mrk with service annotation its service obj where its object use it applied
    @Pointcut("execution(* com.fitness.userservice.service.UserService.getUserProfile(..))")
    private void anyOldTransfer(){}

    //@Around("execution(* com.fitness.userservice.service.UserService.getUserProfile(..))")
    @Around("anyOldTransfer()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Aspect around is called before");
        Object result =  joinPoint.proceed();
        System.out.println("Aspect around is called After");
        return result;
    }




}
