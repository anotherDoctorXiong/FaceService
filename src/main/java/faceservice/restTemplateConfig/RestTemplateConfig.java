package faceservice.restTemplateConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate tokenRetrieveRestTemplate() {
        //HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate();
        ClientHttpRequestFactory requestFactory =
                new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
        restTemplate.setRequestFactory(requestFactory);
        //Response status code 4XX or 5XX to the client.
        restTemplate.setErrorHandler(new ThrowErrorHandler());
        restTemplate.setInterceptors(Collections.singletonList(new LoggingInterceptor()));
        return restTemplate;
    }
}
