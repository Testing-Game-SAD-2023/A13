package com.g2.security;

import com.g2.interfaces.ServiceManager;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private final ServiceManager serviceManager;

    public FilterConfig(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    @Bean
    public FilterRegistrationBean<Filter> authTokenFilterRegistration() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthTokenFilter(serviceManager));
        registrationBean.addUrlPatterns("/*"); // Applico il filtro a tutte le richeste
        registrationBean.setOrder(1); // Setto la priorità del filtro
        return registrationBean;
    }
}
