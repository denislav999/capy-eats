package capyeats.config;


import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests( matcher -> matcher
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/","/login","/signup","/signup-kitchen","/terms","/support","/resources"
                , "/privacy","/press", "/partner", "/about", "/cookies", "/careers", "/img/**").permitAll()
                .requestMatchers("/kitchen-overview", "/kitchen-menu", "/kitchen-orders",
                        "/kitchen-profile", "/kitchen-settings").hasRole("KITCHEN_OWNER")
                .requestMatchers("/home", "/profile/addresses", "/profile/addresses/**",
                        "/profile/payments", "/profile/orders", "/profile/settings").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .logoutSuccessUrl("/")
                );

        return httpSecurity.build();
    }
}
