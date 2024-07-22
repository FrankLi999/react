package mycamel.spring.boot.oauth2client.web;

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
                .addResourceHandler("/spring/admin/dashboard/static/**")
                .addResourceLocations("classpath:/s2i-integrator-static/static/");
        registry
                .addResourceHandler("/spring/admin/dashboard/img/**")
                .addResourceLocations("classpath:/s2i-integrator-static/img/");
//        registry
//                .addResourceHandler("/admin/console/properties/*")
//                .addResourceLocations("classpath:/s2i-integrator-static/index.html");
        registry
                .addResourceHandler("/spring/admin/dashboard")
                .addResourceLocations("classpath:/s2i-integrator-static/index.html");
        registry
                .addResourceHandler("spring/admin/dashboard/*")
                .addResourceLocations("classpath:/s2i-integrator-static/");
        // @formatter:on
	}

}
