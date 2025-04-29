package com.greenbuilding.demo.security;

import jakarta.security.enterprise.CallerPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Set;

public class AppSecurityContext implements SecurityContext {
    private final Principal callerPrincipal;
    private final Set<String> roles;
    private final HttpServletRequest request;
    private final String authenticationScheme;

    public AppSecurityContext(CallerPrincipal callerPrincipal,
                              Set<String> roles,
                              HttpServletRequest request,
                              String authenticationScheme) {
        this.callerPrincipal = callerPrincipal;
        this.roles = roles;
        this.request = request;
        this.authenticationScheme = authenticationScheme;
    }

    @Override
    public Principal getUserPrincipal() {
        return callerPrincipal;
    }

    @Override
    public boolean isUserInRole(String role) {
        return roles.contains(role);
    }

    @Override
    public boolean isSecure() {
        return request.isSecure();
    }

    @Override
    public String getAuthenticationScheme() {
        return authenticationScheme;
    }
}
