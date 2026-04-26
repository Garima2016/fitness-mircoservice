package com.fitness.activityService.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserValidationService {
    private final WebClient userServiceWebClient;

    // ── Call Item Service with Circuit Breaker + Retry ────
    @CircuitBreaker(name = "userService", fallbackMethod = "userServiceFallback")
    @Retry(name = "userService")
    @RateLimiter(name = "userService")
    public boolean validateUser(String userId){
        try {
            return userServiceWebClient.get().uri("/api/users/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        }catch(WebClientResponseException e){
            e.printStackTrace();
        }
        return false;
    }


    // ── Fallback — called when circuit is OPEN ────────────
    // must have same return type + extra Throwable param
    public boolean userServiceFallback(String itemId, Throwable ex) {
        log.error("Item service is down. itemId={}, error={}",
                itemId, ex.getMessage());
        // return a safe default so order flow doesn't completely break
        //UserResponse fallback = new UserResponse();

        return false;
    }

}
