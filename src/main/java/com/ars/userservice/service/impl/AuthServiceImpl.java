package com.ars.userservice.service.impl;

import com.ars.userservice.constants.ResultConstants;
import com.ars.userservice.dto.mapping.IAuthenticationDTO;
import com.ars.userservice.dto.request.user.LoginRequestDTO;
import com.ars.userservice.dto.request.user.RegisterRequestDTO;
import com.ars.userservice.dto.response.AuthenticationResponseDTO;
import com.ars.userservice.entity.Roles;
import com.ars.userservice.entity.Users;
import com.ars.userservice.repository.AuthorityRepository;
import com.ars.userservice.repository.RoleRepository;
import com.ars.userservice.repository.UserRepository;
import com.ars.userservice.security.UserDetailsCustom;
import com.ars.userservice.service.AuthService;

import com.dct.config.security.filter.BaseJwtProvider;
import com.dct.model.common.SecurityUtils;
import com.dct.model.config.properties.SecurityProps;
import com.dct.model.constants.ActivateStatus;
import com.dct.model.constants.BaseExceptionConstants;
import com.dct.model.constants.BaseHttpStatusConstants;
import com.dct.model.constants.BaseSecurityConstants;
import com.dct.model.constants.BaseUserConstants;
import com.dct.model.dto.auth.BaseTokenDTO;
import com.dct.model.dto.auth.BaseUserDTO;
import com.dct.model.dto.response.BaseResponseDTO;
import com.dct.model.exception.BaseAuthenticationException;
import com.dct.model.exception.BaseBadRequestException;
import com.dct.model.exception.BaseIllegalArgumentException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private static final String ENTITY_NAME = "com.ars.userservice.service.impl.AuthServiceImpl";
    private final AuthenticationManager authenticationManager;
    private final BaseJwtProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final SecurityProps securityProps;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           BaseJwtProvider tokenProvider,
                           PasswordEncoder passwordEncoder,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           AuthorityRepository authorityRepository,
                           SecurityProps securityProps) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.securityProps = securityProps;
    }

    @Override
    public BaseResponseDTO checkAuthenticationStatus() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (org.springframework.util.StringUtils.hasText(username)) {
            Optional<IAuthenticationDTO> authentication = userRepository.findAuthenticationByUsernameOrEmail(username);

            if (authentication.isEmpty()) {
                throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.ACCOUNT_NOT_EXISTED);
            }

            AuthenticationResponseDTO authenticationDTO = new AuthenticationResponseDTO();
            BeanUtils.copyProperties(authentication.get(), authenticationDTO);
            Set<String> authorities = authorityRepository.findAllByUserId(authenticationDTO.getId());
            authenticationDTO.setAuthorities(authorities);
            return BaseResponseDTO.builder().ok(authenticationDTO);
        }

        throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.BAD_CREDENTIALS);
    }

    @Override
    @Transactional
    public BaseResponseDTO register(RegisterRequestDTO requestDTO, boolean isShop) {
        if (userRepository.existsByUsernameOrEmail(requestDTO.getUsername(), requestDTO.getEmail())) {
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.ACCOUNT_EXISTED);
        }

        Users user = Users.builder()
                .username(StringUtils.trimToEmpty(requestDTO.getUsername()))
                .password(passwordEncoder.encode(StringUtils.trimToEmpty(requestDTO.getPassword())))
                .fullname(StringUtils.trimToEmpty(requestDTO.getFullname()))
                .email(StringUtils.trimToEmpty(requestDTO.getEmail()))
                .phone(StringUtils.trimToEmpty(requestDTO.getPhone()))
                .status(BaseUserConstants.Status.ACTIVE)
                .build();

        if (isShop) {
            Optional<Roles> role = roleRepository.findRoleByCode(BaseSecurityConstants.Role.DEFAULT);

            if (role.isEmpty()) {
                throw new BaseIllegalArgumentException(ENTITY_NAME, BaseExceptionConstants.ROLE_NOT_FOUND);
            }

            user.setRoles(Set.of(role.get()));
            userRepository.save(user);
        }

        return BaseResponseDTO.builder()
                .code(BaseHttpStatusConstants.ACCEPTED)
                .message(ResultConstants.REGISTER_SUCCESS)
                .success(Boolean.TRUE)
                .result(userRepository.save(user))
                .build();
    }

    @Override
    @Transactional
    public BaseResponseDTO authenticate(LoginRequestDTO loginRequest) {
        log.debug("Authenticating user: {}", loginRequest.getUsername());
        String username = loginRequest.getUsername().trim();
        String rawPassword = loginRequest.getPassword().trim();
        Authentication authentication = this.authenticate(username, rawPassword);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsCustom userDetails = (UserDetailsCustom) authentication.getPrincipal();
        AuthenticationResponseDTO results = new AuthenticationResponseDTO();
        Users user = userDetails.getUser();
        BeanUtils.copyProperties(user, results);
        results.setAuthorities(userDetails.getUserAuthorities());
        results.setStatus(BaseUserConstants.Status.toString(user.getStatus()));
        BaseTokenDTO tokenDTO = BaseTokenDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .authorities(userDetails.getUserAuthorities())
                .rememberMe(loginRequest.getRememberMe())
                .build();

        String jwtAccessToken = tokenProvider.generateAccessToken(tokenDTO);
        String jwtRefreshToken = tokenProvider.generateRefreshToken(tokenDTO);
        results.setAccessToken(jwtAccessToken);
        results.setCookie(this.createSecureCookie(jwtRefreshToken, loginRequest.getRememberMe()));

        return BaseResponseDTO.builder()
                .code(BaseHttpStatusConstants.ACCEPTED)
                .message(ResultConstants.LOGIN_SUCCESS)
                .success(Boolean.TRUE)
                .result(results)
                .build();
    }

    @Override
    public BaseResponseDTO refreshToken(HttpServletRequest request) {
        String refreshToken = SecurityUtils.retrieveToken(request);
        Authentication authentication = tokenProvider.validateRefreshToken(refreshToken);
        BaseUserDTO userDTO = (BaseUserDTO) authentication.getPrincipal();
        Set<String> authorities = userDTO.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(org.springframework.util.StringUtils::hasText)
                .collect(Collectors.toSet());
        BaseTokenDTO tokenDTO = BaseTokenDTO.builder()
                .authorities(authorities)
                .userId(userDTO.getId())
                .username(userDTO.getUsername())
                .build();
        String accessToken = tokenProvider.generateAccessToken(tokenDTO);
        return BaseResponseDTO.builder().ok(accessToken);
    }

    @Override
    public BaseResponseDTO logout() {
        return null;
    }

    private Authentication authenticate(String username, String rawPassword) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, rawPassword);

        try {
            return authenticationManager.authenticate(token);
        } catch (UsernameNotFoundException e) {
            log.error("[AUTHENTICATE_ERROR] [{}] - Account '{}' does not exists", e.getClass().getSimpleName(), username);
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.ACCOUNT_NOT_FOUND);
        } catch (DisabledException e) {
            log.error("[AUTHENTICATE_ERROR] [{}] - Account disabled: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.ACCOUNT_DISABLED);
        } catch (LockedException e) {
            log.error("[AUTHENTICATE_ERROR] [{}] - Account locked: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.ACCOUNT_LOCKED);
        } catch (AccountExpiredException e) {
            log.error("[AUTHENTICATE_ERROR] [{}] - Account expired: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.ACCOUNT_EXPIRED);
        } catch (CredentialsExpiredException e) {
            log.error("[AUTHENTICATE_ERROR] [{}] - Credentials expired: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.CREDENTIALS_EXPIRED);
        } catch (BadCredentialsException e) {
            log.error("[AUTHENTICATE_ERROR] [{}] - Bad credentials: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.BAD_CREDENTIALS);
        } catch (AuthenticationException e) {
            log.error("[AUTHENTICATE_ERROR] [{}] Authentication failed: {}", e.getClass().getSimpleName(), e.getMessage());
            throw new BaseAuthenticationException(ENTITY_NAME, BaseExceptionConstants.UNAUTHORIZED);
        }
    }

    private Cookie createSecureCookie(String refreshToken, boolean isRememberMe) {
        Cookie cookie = new Cookie(BaseSecurityConstants.COOKIES.HTTP_ONLY_TOKEN, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(ActivateStatus.ENABLED.equals(securityProps.getEnabledTls()));
        cookie.setPath("/api/p/v1/users/refresh-token");
        cookie.setAttribute("SameSite", "Strict");
        // cookie.domain("frontend.com");
        long refreshTokenValidity = securityProps.getJwt().getRefreshToken().getValidity();
        long refreshTokenValidityForRemember = securityProps.getJwt().getRefreshToken().getValidityForRemember();
        long expiredTimeMillis = isRememberMe ? refreshTokenValidityForRemember : refreshTokenValidity;
        cookie.setMaxAge((int) (expiredTimeMillis / 1000L));
        return cookie;
    }
}
