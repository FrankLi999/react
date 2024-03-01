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
    // private static final String API_NAMESPACE_REGEX = "^/(spring/admin/api/.*|spring/admin/api)";
    private static final String DASHBOARD_REGEX = "^/(spring/admin/dashboard/.*|spring/admin/dashboard)";
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
        String servletPath = req.getServletPath();
        // boolean isApiNamespace = servletPath.matches(API_NAMESPACE_REGEX);
        boolean isDashboard = servletPath.matches(DASHBOARD_REGEX);
        boolean isFilename = servletPath.matches(FILENAME_REGEX);
        if (log.isDebugEnabled()) {
            log.debug("Logging Request  {} : {} : isApiNamespace: {}: isDashboard: {}: statusCode: {}", req.getMethod(),  servletPath, isDashboard, isFilename, res.getStatus());
        }


        // if (!isApiNamespace && !isFilename && res.getStatus() == 404) {
        if (isDashboard && !isFilename) {
             this.servletContext.getRequestDispatcher("/spring/admin/dashboard/index.html").forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
        log.info("Logging Response :{}", res.getContentType());
    }
}
