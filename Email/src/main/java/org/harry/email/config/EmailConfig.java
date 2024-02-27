package org.harry.email.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.TemplateEngine;

@Configuration
@Getter
public class EmailConfig {

//    @Value("${spring.mail.verify.host}")
//    private String host;
//    @Value("${spring.mail.username}")
//    private String fromEmail;



    @Value("http://localhost:8070")
    private String host;
    @Value("nwaogwugwuharrison@gmail.com")
    private String fromEmail;

    @Bean
    public JavaMailSender send(){
        return new JavaMailSenderImpl();
    }

}
