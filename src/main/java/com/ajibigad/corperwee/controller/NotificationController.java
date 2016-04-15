package com.ajibigad.corperwee.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

/**
 * Created by Julius on 13/04/2016.
 */
@Controller
public class NotificationController {
    private static final Logger logger =
            LoggerFactory.getLogger(NotificationController.class);

    @MessageMapping("/notification")
    public Shout handleShout(Shout incoming) {
        logger.info("Received message: " + incoming.getMessage());
        incoming.setMessage("Sunday Morning");
        return incoming;
    }

    public static class Shout {
        private String message;
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
