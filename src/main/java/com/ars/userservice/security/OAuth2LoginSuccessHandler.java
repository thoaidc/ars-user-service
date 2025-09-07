package com.ars.userservice.security;

import com.dct.config.security.handler.BaseOAuth2AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class OAuth2LoginSuccessHandler extends BaseOAuth2AuthenticationSuccessHandler {
    private static final Logger log = LoggerFactory.getLogger(OAuth2LoginSuccessHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("{}", authentication.getPrincipal());
    }
}
