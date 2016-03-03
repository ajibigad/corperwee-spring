package com.ajibigad.corperwee.config;

/**
* Created by Julius on 18/02/2016.
*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(
                        "select username,password,enabled " +
                                "from user " +
                                "where username = ?")
                .authoritiesByUsernameQuery(
                        "select username, 'ROLE_USER' from user where username=?");
                //.passwordEncoder(new StandardPasswordEncoder("corperwee"));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/corperwee/api/user").permitAll()
                    .antMatchers(HttpMethod.GET, "/corperwee/api/category").permitAll()
                    .antMatchers("/corperwee/api/logout").permitAll()
                    .anyRequest().authenticated()
                .and()
                .httpBasic().realmName("Corperwee")
                .and()
                .logout()
                    .logoutSuccessUrl("/corperwee/api/logout")
                .logoutUrl("/signout")
                .and()
                .csrf()
                    .disable()
                ;
    }
}