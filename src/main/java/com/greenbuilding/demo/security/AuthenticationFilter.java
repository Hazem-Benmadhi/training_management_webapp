package com.greenbuilding.demo.security;

import com.greenbuilding.demo.dao.UserDAO;
import com.greenbuilding.demo.entity.AppUser;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@WebFilter("/secured/*")
public class AuthenticationFilter implements Filter {

    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAO(); // handles fetching users by login
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Check for basic auth header
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            // Decode and verify credentials
            String[] credentials = new String(
                    Base64.getDecoder().decode(authHeader.substring(6)))
                    .split(":", 2);

            if (credentials.length == 2) {
                Optional<AppUser> user = userDAO.findByLogin(credentials[0]);
                if (user.isPresent() && verifyPassword(credentials[1], user.get().getPassword())) {
                    // Authenticate the user
                    httpRequest.login(credentials[0], credentials[1]);
                    chain.doFilter(request, response);
                    return;
                }
            }
        }

        // Not authenticated - send challenge
        httpResponse.setHeader("WWW-Authenticate", "Basic realm=\"GreenBuilding\"");
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private boolean verifyPassword(String inputPassword, String storedPassword) {
        return inputPassword.equals(storedPassword);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {
    }
}