package mycamel.spring.boot.camel.routes.config;

import java.net.URI;

import feign.Headers;
import feign.RequestLine;

public interface RefreshConfigClient {

	@RequestLine("GET")
	@Headers({ "Accept: application/json" })
	String refresh(URI requestUrl);

}
