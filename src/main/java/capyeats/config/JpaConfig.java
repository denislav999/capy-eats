package capyeats.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//need for created_on
@Configuration
@EnableJpaAuditing
public class JpaConfig {

}