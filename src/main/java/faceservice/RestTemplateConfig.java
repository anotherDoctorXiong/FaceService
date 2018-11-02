package faceservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate tokenRetrieveRestTemplate() {
        RestTemplate restTemplate=new RestTemplate();
        //Response status code 4XX or 5XX to the client.
        restTemplate.setErrorHandler(new ThrowErrorHandler());
        return restTemplate;
    }
}
