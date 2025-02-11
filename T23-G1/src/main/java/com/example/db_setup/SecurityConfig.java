// Le Configurazioni di sicurezza sono definite in SecurityConfig.java:
// vengono definite le regole di sicurezza per l'applicazione, 
// ad esempio quali URL richiedono l'autenticazione e quali no.

package com.example.db_setup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.example.db_setup.Service.OAuthUserGoogleService;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private OAuthUserGoogleService oAuthUserGoogleService;

    @Autowired
    private GoogleSuccessHandler googleAuthenticationSuccessHandler;

    // Questo metodo configura le regole di sicurezza per l'applicazione
    // in particolare definisce quali URL richiedono l'autenticazione e quali no
    // gli URL del login e logout, settano il success handler per l'autenticazione con Google

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .antMatcher("/**").authorizeRequests()
            .antMatchers("/llogin", "/t23/**", "/login","/new_notification", "**").permitAll()
            .anyRequest().authenticated()
            .and()
            .oauth2Login().loginPage("/login").userInfoEndpoint()
            .userService(oAuthUserGoogleService).and()
            .successHandler(googleAuthenticationSuccessHandler)
            //.and()
            //.logout()
            //.logoutUrl("/dologout") // Logout URL
            //.clearAuthentication(true)
            //.logoutSuccessUrl("/login")
            .permitAll();

            http.csrf().disable();
            http.logout().disable();
            }
}