package com.ajibigad.corperwee;

import com.ajibigad.corperwee.service.NotificationAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;

/**
 * Created by Julius on 18/02/2016.
 */
@Configuration
@EnableAspectJAutoProxy
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.ajibigad")
@ImportResource("classpath:corperwee.xml")
public class CorperWeeApplication {

    public static void main(String [] args){
        SpringApplication.run(CorperWeeApplication.class, args);
    }

//    @Bean
//    public NotificationAspect notificationAspect() {
//        return new NotificationAspect();
//    }
}
