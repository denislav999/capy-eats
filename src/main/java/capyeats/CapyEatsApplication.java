package capyeats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class CapyEatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CapyEatsApplication.class, args);
    }

}
