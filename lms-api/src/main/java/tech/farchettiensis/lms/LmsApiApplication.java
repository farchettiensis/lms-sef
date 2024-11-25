package tech.farchettiensis.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tech.farchettiensis.lms.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class LmsApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmsApiApplication.class, args);
    }

}
