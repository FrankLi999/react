package mycamel.spring.boot.camel.routes.config;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.function.Predicate;

import feign.FeignException;

import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

public class ResponseExceptionPredicate implements Predicate<Throwable> {

	@Override
	public boolean test(Throwable t) {
		return (t instanceof RestClientResponseException || t instanceof SocketTimeoutException
				|| t instanceof FeignException || t instanceof IOException || t instanceof ResourceAccessException);
	}

}