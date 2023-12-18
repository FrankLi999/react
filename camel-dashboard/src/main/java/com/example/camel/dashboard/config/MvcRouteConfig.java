
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

//@Controller
//@RequestMapping("/dashboard")
@Configuration
public class DashboardConfig implements WebMvcConfigurer {
//    @GetMapping(name = "ui/home/landing", produces = MediaType.TEXT_HTML_VALUE)
//    public RedirectView forwardHawtioRequestToIndexHtml(RedirectAttributes attributes) {
////        attributes.addFlashAttribute("flashAttribute", "redirectWithRedirectView");
////        attributes.addAttribute("attribute", "redirectWithRedirectView");
//        return new RedirectView("/dashboard/ui/index.html");
//    }
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        // @formatter:off
        // S2i Integrator React static resources
        registry
                .addResourceHandler("/dashboard/static/**")
                .addResourceLocations("classpath:/integrator-static/static/");
        registry
                .addResourceHandler("/dashboard/img/**")
                .addResourceLocations("classpath:/integrator-static/img/");
        registry
                .addResourceHandler("/admin/console/properties/*")
                .addResourceLocations("classpath:/integrator-static/index.html");
        registry
                .addResourceHandler("/dashboard/*")
                .addResourceLocations("classpath:/integrator-static/");
        // @formatter:on
    }
}
