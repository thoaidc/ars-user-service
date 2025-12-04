package com.ars.userservice.service.impl;

import com.ars.userservice.constants.ResultConstants;
import com.ars.userservice.constants.UserConstants;
import com.ars.userservice.dto.mapping.IAuthenticationDTO;
import com.ars.userservice.dto.request.user.AuthTokenDTO;
import com.ars.userservice.dto.request.user.LoginRequestDTO;
import com.ars.userservice.dto.request.user.RegisterRequestDTO;
import com.ars.userservice.dto.request.user.RegisterShopRequestDTO;
import com.ars.userservice.dto.response.AuthenticationResponseDTO;
import com.ars.userservice.dto.response.ShopLoginInfoDTO;
import com.ars.userservice.entity.OutBox;
import com.ars.userservice.entity.Roles;
import com.ars.userservice.entity.Users;
import com.ars.userservice.repository.AuthorityRepository;
import com.ars.userservice.repository.OutBoxRepository;
import com.ars.userservice.repository.RoleRepository;
import com.ars.userservice.repository.UserRepository;
import com.ars.userservice.security.UserDetailsCustom;
import com.ars.userservice.service.AuthService;

import com.dct.config.common.HttpClientUtils;
import com.dct.config.security.filter.BaseJwtProvider;
import com.dct.model.common.JsonUtils;
import com.dct.model.common.SecurityUtils;
import com.dct.model.config.properties.SecurityProps;
import com.dct.model.constants.ActivateStatus;
import com.dct.model.constants.BaseDatetimeConstants;
import com.dct.model.constants.BaseExceptionConstants;
import com.dct.model.constants.BaseHttpStatusConstants;
import com.dct.model.constants.BaseOutBoxConstants;
import com.dct.model.constants.BaseSecurityConstants;
import com.dct.model.constants.BaseUserConstants;
import com.dct.model.dto.auth.BaseTokenDTO;
import com.dct.model.dto.auth.BaseUserDTO;
import com.dct.model.dto.response.BaseResponseDTO;
import com.dct.model.event.UserCreatedEvent;
import com.dct.model.exception.BaseAuthenticationException;
import com.dct.model.exception.BaseBadRequestException;
import com.dct.model.exception.BaseIllegalArgumentException;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
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
    private final OutBoxRepository outBoxRepository;
    private final SecurityProps securityProps;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           BaseJwtProvider tokenProvider,
                           PasswordEncoder passwordEncoder,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           AuthorityRepository authorityRepository,
                           OutBoxRepository outBoxRepository,
                           SecurityProps securityProps,
                           RestTemplate restTemplate,
                           ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
        this.outBoxRepository = outBoxRepository;
        this.securityProps = securityProps;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public BaseResponseDTO checkAuthenticationStatus() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (org.springframework.util.StringUtils.hasText(username)) {
            Optional<IAuthenticationDTO> authentication = userRepository.findAuthenticationByUsernameOrEmail(username);

            if (authentication.isEmpty()) {
                throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.ACCOUNT_NOT_FOUND);
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
    public BaseResponseDTO register(RegisterRequestDTO requestDTO) {
        Users user = saveUser(requestDTO, Boolean.FALSE);
        return BaseResponseDTO.builder()
                .code(BaseHttpStatusConstants.CREATED)
                .message(ResultConstants.REGISTER_SUCCESS)
                .success(Boolean.TRUE)
                .result(user)
                .build();
    }

    @Override
    @Transactional
    public BaseResponseDTO registerShop(RegisterShopRequestDTO request) {
        Users user = saveUser(request, Boolean.TRUE);
        UserCreatedEvent userCreatedEvent = UserCreatedEvent.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .shopName(request.getShopName())
                .sagaId(UUID.randomUUID().toString())
                .build();
        OutBox outBox = new OutBox();
        outBox.setSagaId(userCreatedEvent.getSagaId());
        outBox.setStatus(BaseOutBoxConstants.Status.PENDING);
        outBox.setType(BaseOutBoxConstants.Type.USER_CREATED);
        outBox.setValue(JsonUtils.toJsonString(userCreatedEvent));
        outBoxRepository.save(outBox);
        return BaseResponseDTO.builder()
                .code(BaseHttpStatusConstants.ACCEPTED)
                .message(ResultConstants.REGISTER_SUCCESS)
                .success(Boolean.TRUE)
                .result(user)
                .build();
    }

    private Users saveUser(RegisterRequestDTO request, boolean isShop) {
        if (userRepository.existsByUsernameOrEmail(request.getUsername(), request.getEmail())) {
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.ACCOUNT_EXISTED);
        }

        Users user = Users.builder()
                .username(StringUtils.trimToNull(request.getUsername()))
                .password(passwordEncoder.encode(StringUtils.trimToNull(request.getPassword())))
                .fullname(StringUtils.trimToNull(request.getFullname()))
                .email(StringUtils.trimToNull(request.getEmail()))
                .phone(StringUtils.trimToNull(request.getPhone()))
                .status(BaseUserConstants.Status.ACTIVE)
                .type(UserConstants.Type.USER)
                .build();

        if (isShop) {
            Optional<Roles> role = roleRepository.findRoleByCode(UserConstants.Role.SHOP_OWNER);

            if (role.isEmpty()) {
                throw new BaseIllegalArgumentException(ENTITY_NAME, BaseExceptionConstants.ROLE_NOT_FOUND);
            }

            user.setStatus(BaseUserConstants.Status.PENDING);
            user.setType(UserConstants.Type.SHOP);
            user.setRoles(List.of(role.get()));
        }

        return userRepository.save(user);
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
        return authorize(userDetails.getUser(), userDetails.getUserAuthorities(), loginRequest.getRememberMe());
    }

    @Override
    public BaseResponseDTO authenticate(Users user) {
        Set<String> authorities = authorityRepository.findAllByUserId(user.getId());
        return authorize(user, authorities, Boolean.FALSE);
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

    private BaseResponseDTO authorize(Users user, Set<String> authorities, boolean isRememberMe) {
        AuthenticationResponseDTO results = new AuthenticationResponseDTO();
        BeanUtils.copyProperties(user, results);
        results.setAuthorities(authorities);
        results.setStatus(BaseUserConstants.Status.toString(user.getStatus()));
        AuthTokenDTO.TokenBuilder authTokenDTOBuilder = AuthTokenDTO.tokenBuilder()
                .userId(user.getId())
                .username(user.getUsername())
                .authorities(authorities)
                .rememberMe(isRememberMe);

        if (UserConstants.Type.SHOP.equals(user.getType())) {
            BaseResponseDTO responseDTO = HttpClientUtils.builder()
                    .restTemplate(restTemplate)
                    .url("http://PRODUCT-SERVICE/api/internal/shop/login-info/" + user.getId())
                    .method(HttpMethod.GET)
                    .execute(BaseResponseDTO.class);
            if (Objects.nonNull(responseDTO) && Objects.nonNull(responseDTO.getResult())) {
                ShopLoginInfoDTO shopLoginInfo = objectMapper.convertValue(responseDTO.getResult(), ShopLoginInfoDTO.class);
                authTokenDTOBuilder.shopId(shopLoginInfo.getShopId()).shopName(shopLoginInfo.getShopName());
            }
        }

        AuthTokenDTO tokenDTO = authTokenDTOBuilder.build();
        String jwtAccessToken = tokenProvider.generateAccessToken(tokenDTO);
        String jwtRefreshToken = tokenProvider.generateRefreshToken(tokenDTO);
        results.setAccessToken(jwtAccessToken);
        results.setCookie(this.createSecureCookie(jwtRefreshToken, isRememberMe));
        return BaseResponseDTO.builder()
                .code(BaseHttpStatusConstants.ACCEPTED)
                .message(ResultConstants.LOGIN_SUCCESS)
                .success(Boolean.TRUE)
                .result(results)
                .build();
    }

    private Cookie createSecureCookie(String refreshToken, boolean isRememberMe) {
        Cookie cookie = new Cookie(BaseSecurityConstants.COOKIES.HTTP_ONLY_TOKEN, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(ActivateStatus.ENABLED.equals(securityProps.getEnabledTls()));
        cookie.setPath(UserConstants.REFRESH_TOKEN_API);
        cookie.setAttribute(BaseSecurityConstants.COOKIES.SECURITY_ATTRIBUTE, BaseSecurityConstants.COOKIES.SECURITY_MODE);
        // cookie.domain("frontend.com");
        long refreshTokenValidity = securityProps.getJwt().getRefreshToken().getValidity();
        long refreshTokenValidityForRemember = securityProps.getJwt().getRefreshToken().getValidityForRemember();
        long expiredTimeMillis = isRememberMe ? refreshTokenValidityForRemember : refreshTokenValidity;
        cookie.setMaxAge((int) (expiredTimeMillis / BaseDatetimeConstants.ONE_SECOND_IN_MILLIS));
        return cookie;
    }

    public Cookie resetCookie() {
        Cookie cookie = new Cookie(BaseSecurityConstants.COOKIES.HTTP_ONLY_TOKEN, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(ActivateStatus.ENABLED.equals(securityProps.getEnabledTls()));
        cookie.setPath(UserConstants.REFRESH_TOKEN_API);
        cookie.setAttribute(BaseSecurityConstants.COOKIES.SECURITY_ATTRIBUTE, BaseSecurityConstants.COOKIES.SECURITY_MODE);
        // cookie.domain("frontend.com");
        cookie.setMaxAge(BaseSecurityConstants.COOKIES.COOKIE_EXPIRED);
        return cookie;
    }
}
