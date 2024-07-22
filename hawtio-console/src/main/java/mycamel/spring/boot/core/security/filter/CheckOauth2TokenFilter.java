package mycamel.spring.boot.core.security.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class CheckOauth2TokenFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		log.debug("Auth header:" + authToken);
		filterChain.doFilter(request, response);
	}

}
