package com.ajibigad.corperwee.service;

import com.ajibigad.corperwee.model.apiModels.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by Julius on 14/04/2016.
 */
@Service
public class NotificationService<T> {

    @Autowired
    private SimpMessagingTemplate messaging;

//    @Autowired
//    public NotificationService(SimpMessagingTemplate messaging) {
//        this.messaging = messaging;
//    }

    public void sendToUser(String username, Notification.NotificationType notificationType, T data, String message){
        messaging.convertAndSendToUser(username, "/queue/notifications", new Notification(message,
                Notification.NotificationType.REVIEW, data));
    }
}
