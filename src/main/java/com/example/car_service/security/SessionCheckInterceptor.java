package com.example.car_service.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

@Component
public class SessionCheckInterceptor implements HandlerInterceptor {

    public static final Set<String> NO_SESSION_REQUIRED_URLS = Set.of("/login", "/register", "/","guest-login");

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        if (NO_SESSION_REQUIRED_URLS.contains(request.getServletPath())) {
            return true;
        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect("/login");
            return false;
        }
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }

}
