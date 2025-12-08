package capyeats.config;


import capyeats.security.UserSessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    private final UserSessionInterceptor interceptor;

    @Autowired
    public WebConfiguration(UserSessionInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.interceptor).addPathPatterns("/**").excludePathPatterns("/css/**", "/img/**");
    }
}
