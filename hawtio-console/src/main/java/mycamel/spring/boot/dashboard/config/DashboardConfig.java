package mycamel.spring.boot.dashboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class DashboardConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        // @formatter:off
        // S2i Integrator React static resources
        registry
                .addResourceHandler("/my-camel/admin/dashboard/static/**")
                .addResourceLocations("classpath:/dashboard-ui/static/");
        registry
                .addResourceHandler("/my-camel/admin/dashboard/img/**")
                .addResourceLocations("classpath:/dashboard-ui/img/");
        registry
                .addResourceHandler("/my-camel/admin/dashboard/locales/**")
                .addResourceLocations("classpath:/dashboard-ui/locales/");
//        registry
//                .addResourceHandler("/admin/console/properties/*")
//                .addResourceLocations("classpath:/s2i-integrator-static/index.html");
        registry
                .addResourceHandler("/my-camel/admin/dashboard")
                .addResourceLocations("classpath:/dashboard-ui/index.html");
        registry
                .addResourceHandler("/my-camel/admin/dashboard/*")
                .addResourceLocations("classpath:/dashboard-ui/");
        //
        registry
                .addResourceHandler("/dashboard/static/**")
                .addResourceLocations("classpath:/dashboard-ui/static/");
        registry
                .addResourceHandler("/dashboard/img/**")
                .addResourceLocations("classpath:/dashboard-ui/img/");
        registry
                .addResourceHandler("/dashboard/locales/**")
                .addResourceLocations("classpath:/dashboard-ui/locales/");
//        registry
//                .addResourceHandler("/admin/console/properties/*")
//                .addResourceLocations("classpath:/s2i-integrator-static/index.html");
        registry
                .addResourceHandler("/dashboard")
                .addResourceLocations("classpath:/dashboard-ui/index.html");
        registry
                .addResourceHandler("/dashboard/*")
                .addResourceLocations("classpath:/dashboard-ui/");
        // @formatter:on
    }
}
