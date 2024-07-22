package mycamel.spring.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.web.exchanges.HttpExchangeRepository;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
// @EnableConfigServer
public class HawtioConsoleApplication {

	public static void main(String[] args) {
		SpringApplication.run(HawtioConsoleApplication.class, args);
	}

	/**
	 * Enable HTTP tracing for Spring Boot
	 */
	@Bean
	public HttpExchangeRepository httpTraceRepository() {
		return new InMemoryHttpExchangeRepository();
	}

}
