package com.ecommerce.store.service;

import com.ecommerce.store.entity.Feedback;
import com.ecommerce.store.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public Feedback addFeedback(Feedback feedback) {
        if (feedback.getFeedback().length() > 10000)
            feedback.setFeedback(feedback.getFeedback().substring(0, 9999));
        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getFeedbacks() {
        return feedbackRepository.findAll();
    }
}
