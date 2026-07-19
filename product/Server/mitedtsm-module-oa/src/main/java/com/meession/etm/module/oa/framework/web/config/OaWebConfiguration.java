package com.meession.etm.module.oa.framework.web.config;

import com.meession.etm.framework.swagger.config.MitedtsmSwaggerAutoConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class OaWebConfiguration {

    @Bean
    public GroupedOpenApi oaGroupedOpenApi() {
        return MitedtsmSwaggerAutoConfiguration.buildGroupedOpenApi("oa");
    }

}