package mycamel.spring.boot.dashboard.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(9999)
@Slf4j
public class SpaWebFilter extends OncePerRequestFilter {
    // private static final String API_NAMESPACE_REGEX = "^/(spring/admin/api/.*|spring/admin/api)";
    // private static final String DASHBOARD_REGEX = "^/(my-camel/admin/dashboard/.*|my-camel/admin/dashboard)";
    private static final String DASHBOARD_REGEX = "^/(dashboard/.*|dashboard)";
    private static final String FILENAME_REGEX = "^/.*\\.[^.]+$";
    //    private ServletContext servletContext;
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        this.servletContext = filterConfig.getServletContext();
//        super.init(filterConfig);
//    }
    @Override
    public void doFilterInternal(final HttpServletRequest  req, final HttpServletResponse res, final FilterChain chain)
            throws IOException, ServletException {
        String servletPath = req.getServletPath();
        // String servletPath = req.getRequestURI();
        // boolean isApiNamespace = servletPath.matches(API_NAMESPACE_REGEX);
        boolean isDashboard = servletPath.matches(DASHBOARD_REGEX);
        boolean isFilename = servletPath.matches(FILENAME_REGEX);
        if (log.isDebugEnabled()) {
            log.debug("Logging Request  {} : {} : isApiNamespace: {}: isDashboard: {}: statusCode: {}", req.getMethod(),  servletPath, isDashboard, isFilename, res.getStatus());
        }


        // if (!isApiNamespace && !isFilename && res.getStatus() == 404) {
//        Authentication user = SecurityContextHolder.getContext().getAuthentication();
//        if (user != null && !path.startsWith("/api") && !path.contains(".") && path.matches("/(.*)")) {
//            request.getRequestDispatcher("/").forward(request, response);
//            return;
//        }
        if (isDashboard && !isFilename) {
            req.getRequestDispatcher("/my-camel/admin/dashboard/index.html").forward(req, res);
        } else {
            chain.doFilter(req, res);
        }
        log.info("Logging Response :{}", res.getContentType());
    }
}
