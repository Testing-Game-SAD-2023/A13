package com.example.db_setup.Language;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageConfig implements WebMvcConfigurer{
  
  @Bean("messageSource")
  public MessageSource messageSource() {
      ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
      messageSource.setBasenames("language/messages");
      messageSource.setDefaultEncoding("UTF-8");
      return messageSource;
  }

}