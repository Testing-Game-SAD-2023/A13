package com.groom.manvsclass.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

// Usa un nome di pacchetto specifico per i repository JPA
@EnableJpaRepositories(basePackages = "com.groom.manvsclass.model.repository.jpa")
// Usa un nome di pacchetto specifico per le entità JPA
@EntityScan(basePackages = "com.groom.manvsclass.model.entity")
@Configuration
public class JpaConfig {

    // Recupera i valori dal tuo application.properties
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    // --- 1. DATASOURCE (Connessione al DB) ---
    @Bean
    public DataSource dataSource() {
        // Stiamo usando la classe standard di Spring Boot per creare il DataSource
        org.springframework.boot.jdbc.DataSourceBuilder<?> dataSourceBuilder =
                org.springframework.boot.jdbc.DataSourceBuilder.create();
        dataSourceBuilder.url(url);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        return dataSourceBuilder.build();
    }

    // --- 2. ENTITY MANAGER FACTORY (Il bean mancante) ---
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource()); // Usa il DataSource che abbiamo appena definito
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        // Specifica i pacchetti delle entità come abbiamo fatto con @EntityScan
        factory.setPackagesToScan("com.groom.manvsclass.model.entity");

        // Impostazioni aggiuntive per Hibernate (es. ddl-auto)
        java.util.Properties jpaProperties = new java.util.Properties();
        // Nota: se definisci ddl-auto qui, puoi omettere spring.jpa.hibernate.ddl-auto nell'application.properties
        jpaProperties.put("hibernate.hbm2ddl.auto", "none");
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");

        factory.setJpaProperties(jpaProperties);
        return factory;
    }

    // --- 3. TRANSACTION MANAGER (Gestisce le transazioni) ---
    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        // Collega al bean EntityManagerFactory
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}