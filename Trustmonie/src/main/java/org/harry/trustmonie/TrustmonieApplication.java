package org.harry.trustmonie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@ComponentScan({
        "org.harry.trustmonie.user",
        "org.harry.trustmonie.data.model",
        "org.harry.email.user",
        "org.harry.email.service",
        "org.harry.email.config",
        "org.harry.trustmonie.service",
        "org.harry.trustmonie.data",
        "org.harry.trustmonie.config",

})
public class TrustmonieApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrustmonieApplication.class, args);
    }

}

