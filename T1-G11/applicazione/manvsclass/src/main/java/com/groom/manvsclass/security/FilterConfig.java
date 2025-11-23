package com.groom.manvsclass.security;

import com.groom.manvsclass.api.ApiGatewayClient;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.Filter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<Filter> authTokenFilterRegistration(ApiGatewayClient apiGatewayClient) {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthTokenFilter(apiGatewayClient));
        registrationBean.addUrlPatterns("/*"); // Applico il filtro a tutte le richeste
        registrationBean.setOrder(1); // Setto la priorità del filtro
        return registrationBean;
    }
}
