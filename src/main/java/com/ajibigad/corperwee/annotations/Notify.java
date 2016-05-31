package com.ajibigad.corperwee.annotations;

import com.ajibigad.corperwee.model.apiModels.Notification;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Julius on 19/04/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Notify {

    static enum RECIPIENT{USER, ALL}

    Notification.NotificationType type();

    String message() default "This is a Notification";

    Notify.RECIPIENT recipient() default RECIPIENT.ALL;
}
