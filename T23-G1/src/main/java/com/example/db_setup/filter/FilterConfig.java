package com.example.db_setup.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<HttpRequestLoggerFilter> loggingFilter() {
        FilterRegistrationBean<HttpRequestLoggerFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new HttpRequestLoggerFilter());
        // registrationBean.addUrlPatterns("/api/*"); per applicare a specifici endpoint
        registrationBean.setOrder(1);

        return registrationBean;
    }
}
