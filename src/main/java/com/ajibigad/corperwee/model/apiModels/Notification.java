package com.ajibigad.corperwee.model.apiModels;

/**
 * Created by Julius on 14/04/2016.
 */
public class Notification<T> {


    public enum NotificationType { REVIEW, PLACE};

    private String message;
    private T data;
    private NotificationType notificationType;

    public Notification (String message, NotificationType notificationType, T data){
        this.message = message;
        this.data = data;
        this.notificationType = notificationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

}
