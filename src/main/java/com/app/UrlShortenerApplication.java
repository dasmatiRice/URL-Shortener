package com.app;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class UrlShortenerApplication {
    public static void main(String[] args) {
      SpringApplication.run(UrlShortenerApplication.class, args);
    }

}