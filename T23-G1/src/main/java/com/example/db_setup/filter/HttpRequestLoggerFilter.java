package com.example.db_setup.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class HttpRequestLoggerFilter extends OncePerRequestFilter {

    private final Logger customLogger = LoggerFactory.getLogger(HttpRequestLoggerFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        filterChain.doFilter(wrappedRequest, response);

        StringBuilder sb = new StringBuilder();
        sb.append("---------------------- Request info ----------------------\n");

        sb.append("[").append(request.getMethod()).append(" ").append(request.getRequestURI()).append("]").append("\n");
        sb.append("Query String: ").append(request.getQueryString()).append("\n");
        sb.append("Remote Addr: ").append(request.getRemoteAddr()).append("\n");
        sb.append("Headers: \n");

        var headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            sb.append("  ").append(headerName).append(": ")
                    .append(request.getHeader(headerName)).append("\n");
        }

        sb.append("Request Body: \n");
        String body = new String(wrappedRequest.getContentAsByteArray(), StandardCharsets.UTF_8);
        sb.append(body);

        sb.append("-----------------------------------------------------------\n");

        customLogger.debug("{}", sb);
    }
}
