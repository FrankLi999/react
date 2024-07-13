package io.hawt.example.spring.boot.camel.support;

import org.springframework.core.NamedInheritableThreadLocal;

public class CamelRequestContextHolder {

	private static final ThreadLocal<ApiRequestClientRequestContext> camelRequestContextHolder = new NamedInheritableThreadLocal<>(
			"Camel request Context");

	public static void setApiRequestClientRequestContext(
			ApiRequestClientRequestContext apiRequestClientRequestContext) {
		if (apiRequestClientRequestContext != null) {
			camelRequestContextHolder.set(apiRequestClientRequestContext);
		}
		else {
			camelRequestContextHolder.remove();
		}
	}

	public static ApiRequestClientRequestContext getApiRequestClientRequestContext() {
		return camelRequestContextHolder.get();
	}

	public static void resetApiClientRequestContext() {
		camelRequestContextHolder.remove();
	}

}
