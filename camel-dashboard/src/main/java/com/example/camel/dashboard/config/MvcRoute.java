
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;




import java.net.URI;
//@Configuration
//@EnableWebMvc
public class RouterConfiguration { //implements WebMvcConfigurer {
    // @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        // @formatter:off
        // S2i Integrator React static resources
        System.out.println(">>>>>>>>>>>>addResourceHandlers >>>>>>>>>>>>");
        registry
                .addResourceHandler("/integrator/static/**")
                .addResourceLocations("classpath:/integrator-static/static/");
        registry
                .addResourceHandler("/integrator/img/**")
                .addResourceLocations("classpath:/integrator-static/img/");
        registry
                .addResourceHandler("/integrator/**")
                .addResourceLocations("classpath:/integrator-static/");

        // @formatter:on
    }
}
