package com.josefy.nnpda.infrastructure.security;

import com.josefy.nnpda.infrastructure.service.IDeviceCredentialService;
import com.josefy.nnpda.infrastructure.utils.IHashProvider;
import com.josefy.nnpda.model.Device;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.Collections;


//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {
//    // todo: We expose the serial number in our API endpoint anyway lmao
//    private final IDeviceCredentialService deviceCredentialsService;
//    private final IHashProvider hashProvider;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        var wrappedRequest = new ContentCachingRequestWrapper(request);
//        String derivedId = wrappedRequest.getHeader("X-DERIVED-ID");
//        String hmac = wrappedRequest.getHeader("X-HMAC-SIG");
//        log.info("Request to: " + wrappedRequest.getRequestURI());
//        log.info("Servlet path: " + wrappedRequest.getServletPath());
//
//        if (!wrappedRequest.getRequestURI().contains("/devices/measurements")) {
//            filterChain.doFilter(wrappedRequest, response);
//            return;
//        }
//
//        if (derivedId == null|| hmac == null)
//        {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing authentication headers");
//            return;
//        }
//        var credential = deviceCredentialsService.findDeviceWithCredentials(derivedId);
//        if (credential.isEmpty() || credential.get().isRevoked()) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or revoked API key");
//            return;
//        }
//        String key = credential.get().getApiKey();
//        try (var inputStream = wrappedRequest.getInputStream()) {
//            if (!hashProvider.isHmacValid(wrappedRequest.getInputStream(), key, hmac)) {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid signature");
//                return;
//            }
//        }
//        Device device = credential.get().getDevice();
//        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(device,
//                null,
//                Collections.emptyList());
//        SecurityContextHolder.getContext().setAuthentication(auth);
//        filterChain.doFilter(wrappedRequest, response);
//    }
//}
