package com.blog.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TemplateConfiguration {

    @Bean
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
    }

    public static void main(String args[]) {
        System.out.println("abc");
    }

    public static void main(char[] args) {
        System.out.println("abc");
    }
}
