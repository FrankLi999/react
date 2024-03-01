package com.example.springbootconfigjdbc.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
@Component
@Order(9999)
@Slf4j
public class FrontendForwardingFilter implements Filter {
    private static final String API_NAMESPACE_REGEX = "^/(api/.*|api)";
    private static final String FILENAME_REGEX = "^/.*\\.[^.]+$";
    private ServletContext servletContext;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.servletContext = filterConfig.getServletContext();
    }
    @Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
        if (log.isDebugEnabled()) {
		    log.debug("Logging Request  {} : {} : {}", req.getMethod(), req.getRequestURI(),  req.getPathInfo());
        }
        boolean isApiNamespace = req.getPathInfo().matches(API_NAMESPACE_REGEX);
        boolean isFilename = req.getRequestURI().matches(FILENAME_REGEX);
        if (!isApiNamespace && !isFilename && res.getStatus() == 404) {
            this.servletContext.getRequestDispatcher("/").forward(request, response);
        } else {
		   chain.doFilter(request, response);
        }
		log.info("Logging Response :{}", res.getContentType());
	}
}
