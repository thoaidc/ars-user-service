package com.ars.userservice.security;

import com.ars.userservice.dto.mapping.OAuth2UserDTO;
import com.ars.userservice.dto.response.AuthenticationResponseDTO;
import com.ars.userservice.entity.Users;
import com.ars.userservice.service.AuthService;
import com.ars.userservice.service.UserService;

import com.dct.config.security.handler.BaseOAuth2AuthenticationSuccessHandler;
import com.dct.model.common.JsonUtils;
import com.dct.model.constants.BaseExceptionConstants;
import com.dct.model.dto.response.BaseResponseDTO;
import com.dct.model.exception.BaseAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

@Component
public class OAuth2LoginSuccessHandler extends BaseOAuth2AuthenticationSuccessHandler {
    private static final Logger log = LoggerFactory.getLogger(OAuth2LoginSuccessHandler.class);
    private static final String ENTITY_NAME = "com.ars.userservice.security.OAuth2LoginSuccessHandler";
    private final UserService userService;
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    public OAuth2LoginSuccessHandler(UserService userService,
                                     AuthService authService,
                                     ObjectMapper objectMapper) {
        this.userService = userService;
        this.authService = authService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
            OAuth2UserDTO userDTO = objectMapper.convertValue(oAuth2User.getAttributes(), OAuth2UserDTO.class);
            BaseResponseDTO responseDTO = userService.createOrUpdateUser(userDTO);
            Users user = (Users) responseDTO.getResult();
            BaseResponseDTO result = authService.authenticate(user);
            AuthenticationResponseDTO authenticationResponse = (AuthenticationResponseDTO) result.getResult();
            Cookie secureCookie = authenticationResponse.getCookie();
            response.addCookie(secureCookie);
            authenticationResponse.setCookie(null);
            result.setResult(authenticationResponse);
            response.getWriter().write(JsonUtils.toJsonString(result));
        } catch (Exception e) {
            log.error("[AUTHENTICATION_FAILED_VIA_OAUTH2] - error", e);
            throw new BaseAuthenticationException(ENTITY_NAME, BaseExceptionConstants.BAD_CREDENTIALS);
        }
    }
}
