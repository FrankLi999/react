package mycamel.spring.boot.camel.support;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CamelRequestResolverConfiguration implements WebMvcConfigurer {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CamelRequestArgumentResolverService camelRequestArgumentResolverService;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new CamelRequestArgumentResolver(objectMapper, camelRequestArgumentResolverService));
	}

}
