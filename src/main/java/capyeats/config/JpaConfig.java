package capyeats.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//need for created_on in BaseEntity
@Configuration
@EnableJpaAuditing
public class JpaConfig {

}