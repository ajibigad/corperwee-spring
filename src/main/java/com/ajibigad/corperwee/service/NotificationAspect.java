package com.ajibigad.corperwee.service;

import com.ajibigad.corperwee.annotations.Notify;
import com.ajibigad.corperwee.model.Review;
import com.ajibigad.corperwee.model.apiModels.Notification;
import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Created by Julius on 20/04/2016.
 */
@Component
@Aspect
public class NotificationAspect {

    @Autowired
    NotificationService<Review> notificationService;

    public static final Logger LOG = Logger.getLogger(NotificationAspect.class);

    @Pointcut("within(com.ajibigad.corperwee.service..*)")
    public void inServiceLayer() {}

    @Pointcut("@annotation(notify)")
    public void notifyAnnotated(Notify notify) {}

    @Pointcut("execution(* com.ajibigad.corperwee.service.*.*(..))") //execution happens inside service subpackages
    public void businessService() {}

    @Pointcut("businessService() && inServiceLayer() && notifyAnnotated(notify)")
    public void notificationConcern(Notify notify) {}

    @AfterReturning(pointcut = "notificationConcern(notify)", returning="retVal")
    public void performNotification(Object retVal, Notify notify){
        //LOG.info("Entered notification aspect");
        if(notify.recipient().compareTo(Notify.RECIPIENT.USER) == 0){
            switch (notify.type()){
                case REVIEW :
                    Review review = (Review)retVal;
                    String recipient = SecurityContextHolder.getContext().getAuthentication().getName();
                    LOG.info("Sending review notification to "+ recipient);
                    notificationService.sendToUser(review.getPlace().getAddedBy().getUsername(), Notification.NotificationType.REVIEW, review, notify.message());
            }
        }
    }

}
