package com.example.gateway.config;

import com.example.common.interceptor.SeataHttpRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(SeataHttpRequestInterceptor seataInterceptor) {
        return RestClient.builder()
                .requestInterceptor(seataInterceptor)
                .build();
    }
}
