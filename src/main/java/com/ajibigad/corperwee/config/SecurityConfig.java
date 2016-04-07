package com.ajibigad.corperwee.config;

/**
* Created by Julius on 18/02/2016.
*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    public final static String API_ENDPOINT = "/corperwee/api";

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
                        "select username, 'ROLE_USER' from user where username=?")
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(API_ENDPOINT + "/user/resetPassword", API_ENDPOINT + "/user/changePassword").anonymous()
                    .antMatchers(HttpMethod.POST, API_ENDPOINT + "/user ").permitAll()
                    .antMatchers(HttpMethod.GET, API_ENDPOINT + "/category ").permitAll()
                    .antMatchers(API_ENDPOINT + "/ logout ", API_ENDPOINT + "/user/profilePicture/*", API_ENDPOINT + "/state/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic().realmName("Corperwee")
                .and()
                .logout()
                    .logoutUrl("/signout")
                    .logoutSuccessUrl(API_ENDPOINT + "/logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                .and()
                .csrf()
                    .disable()
                ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }
}
