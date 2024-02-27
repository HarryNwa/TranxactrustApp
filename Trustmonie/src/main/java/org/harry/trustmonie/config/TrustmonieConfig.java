package org.harry.trustmonie.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class TrustmonieConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


//    @Bean
//    public WebClient webClient() {
//        // Configure and create your WebClient instance here
//        return WebClient.create();
//    }
}
