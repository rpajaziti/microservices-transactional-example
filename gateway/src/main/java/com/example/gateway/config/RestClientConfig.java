package com.example.gateway.config;

import com.example.common.interceptor.SeataHttpRequestInterceptor;
import com.example.gateway.client.OrderServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(SeataHttpRequestInterceptor seataInterceptor,
                                 @Value("${order-service.base-url}") String orderServiceBaseUrl) {
        return RestClient.builder()
                .baseUrl(orderServiceBaseUrl)
                .requestInterceptor(seataInterceptor)
                .build();
    }

    @Bean
    public OrderServiceClient orderServiceClient(RestClient restClient) {
        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(OrderServiceClient.class);
    }
}
