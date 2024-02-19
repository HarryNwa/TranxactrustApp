package org.harry.trustmonie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"org.harry.trustmonie.user", "org.harry.trustmonie.data.model", "org.harry.email.user"})
public class TrustmonieApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrustmonieApplication.class, args);
    }

}
