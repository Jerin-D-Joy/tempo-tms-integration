package com.siemens.internal.config;

import com.atlassian.plugin.spring.SpringHostComponentProviderConfig;
import com.atlassian.plugin.spring.SpringHostComponentProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PluginConfig {

    @Bean
    public SpringHostComponentProviderFactoryBean springHostComponentProviderFactoryBean() {
        SpringHostComponentProviderConfig springHostComponentProviderConfig = new SpringHostComponentProviderConfig();
        SpringHostComponentProviderFactoryBean factoryBean = new SpringHostComponentProviderFactoryBean();
        factoryBean.setSpringHostComponentProviderConfig(springHostComponentProviderConfig);
        return factoryBean;
    }
}
