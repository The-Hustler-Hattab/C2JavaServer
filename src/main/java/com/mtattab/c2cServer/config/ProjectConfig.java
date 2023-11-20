package com.mtattab.c2cServer.config;

import com.mtattab.c2cServer.util.GenericOperationsUtil;
import lombok.extern.slf4j.Slf4j;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.mtattab.c2cServer.util.Constants.OKTA_BEAN;


@Slf4j
@Configuration
public class ProjectConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    String oktaPublicKeyUrl;

    @Bean(name = OKTA_BEAN)
    public String publicKeysFromOkta(){
        String keys = GenericOperationsUtil.sendGetRequest(this.oktaPublicKeyUrl);
        log.info("Okta public key from in json: "+keys);

        return keys;
    }

    @Bean
    public GroupedOpenApi storeOpenApi() {
        String paths[] = {"/data-rest/**"};
        return GroupedOpenApi.builder().group("spring-data-rest").pathsToMatch(paths)
                .build();
    }
    @Bean
    public GroupedOpenApi storeOpenApi2() {
        String paths[] = {"/v1/**"};
        return GroupedOpenApi.builder().group("main-api").pathsToMatch(paths)
                .build();
    }


}
