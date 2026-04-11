package com.fitness.activityService.service;

import com.fitness.activityService.ActivityRespository;
import com.fitness.activityService.controller.ActivityController;
import com.fitness.activityService.dto.ActivityRequest;
import com.fitness.activityService.dto.ActivityResponse;
import com.fitness.activityService.model.Activity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private static final Logger logger = LoggerFactory.getLogger(ActivityService.class);

    private ActivityRespository activityRespository;
    private final UserValidationService userValidationService;
    private final KafkaTemplate<String,Activity> kafkaTemplate;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    @Value("{kafka.topic.name}")
    private String topicName;
    public ActivityResponse trackActivity(ActivityRequest request) {

        boolean isValidUser = userValidationService.validateUser(request.getUserId());

        if(!isValidUser){
            throw new RuntimeException("User Not exist "+ request.getUserId());
        }

        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType()).duration(request.getDuration()).caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();
        Activity saveActivity = activityRespository.save(activity);
        try {
            kafkaTemplate.send(topicName, saveActivity.getUserId(), saveActivity);
        }catch(Exception e){
            e.printStackTrace();
        }
        logger.info("saveActivity::" + saveActivity);
        return mapToResponse(saveActivity);
}

    private ActivityResponse mapToResponse(Activity saveActivity) {
        ActivityResponse response = new ActivityResponse();

        response.setId(saveActivity.getId());
        response.setUserId(saveActivity.getUserId());
        response.setType(saveActivity.getType());
        response.setDuration(saveActivity.getDuration());
        response.setCaloriesBurned(saveActivity.getCaloriesBurned());
        response.setStartTime(saveActivity.getStartTime());
        response.setAdditionalMetrics(saveActivity.getAdditionalMetrics());
        response.setCreatedAt(saveActivity.getCreatedAt());
        response.setUpdatedAt(saveActivity.getUpdatedAt());

        return response;
    }

    public ActivityResponse getActivity(String id) {

        Activity activity = activityRespository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
        logger.info("getActivity::" + activity);
        return mapToResponse(activity);
    }
}
