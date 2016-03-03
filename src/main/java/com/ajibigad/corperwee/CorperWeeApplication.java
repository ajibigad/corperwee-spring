package com.ajibigad.corperwee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Created by Julius on 18/02/2016.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.ajibigad")
@ImportResource("classpath:corperwee.xml")
public class CorperWeeApplication {

    public static void main(String [] args){
        SpringApplication.run(CorperWeeApplication.class, args);
    }
}