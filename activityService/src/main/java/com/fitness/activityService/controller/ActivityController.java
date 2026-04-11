package com.fitness.activityService.controller;

import com.fitness.activityService.dto.ActivityRequest;
import com.fitness.activityService.dto.ActivityResponse;
import com.fitness.activityService.service.ActivityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activities")
@AllArgsConstructor
@Slf4j
public class ActivityController {
    private static final Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @Autowired
    private ActivityService activityService;

    @PostMapping("/trackActivity")
    public ResponseEntity<ActivityResponse> trackActivity (@RequestBody ActivityRequest request){
        logger.info("request receive:  "+ request);
        ActivityResponse response = activityService.trackActivity(request);
        logger.info("response generated:: {}",response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getActivity/{id}")
    public ResponseEntity<ActivityResponse> trackActivity (@PathVariable String id){
        logger.info("request receive:  "+ id);
        ActivityResponse response = activityService.getActivity(id);
        logger.info("response generated:: {}",response);
        return ResponseEntity.ok(response);
    }
}
