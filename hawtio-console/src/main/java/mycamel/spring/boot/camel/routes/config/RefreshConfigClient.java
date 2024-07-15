package mycamel.spring.boot.camel.routes.config;

import feign.Headers;
import feign.RequestLine;

import java.net.URI;
import java.util.Set;

public interface RefreshConfigClient {
    @RequestLine("GET")
    @Headers({"Accept: application/json"})
    String refresh(URI requestUrl);
}
