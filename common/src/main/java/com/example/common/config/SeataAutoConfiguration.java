package com.example.common.config;

import com.example.common.interceptor.SeataHttpRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeataAutoConfiguration {

    @Bean
    public SeataHttpRequestInterceptor seataHttpRequestInterceptor() {
        return new SeataHttpRequestInterceptor();
    }
}
