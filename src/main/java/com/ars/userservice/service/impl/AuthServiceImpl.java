package com.ars.userservice.service.impl;

import com.ars.userservice.constants.ResultConstants;
import com.ars.userservice.dto.request.LoginRequestDTO;
import com.ars.userservice.dto.request.RegisterRequestDTO;
import com.ars.userservice.dto.response.AuthenticationResponseDTO;
import com.ars.userservice.entity.Roles;
import com.ars.userservice.entity.Users;
import com.ars.userservice.repository.RoleRepository;
import com.ars.userservice.repository.UserRepository;
import com.ars.userservice.security.UserDetailsCustom;
import com.ars.userservice.service.AuthService;

import com.dct.config.security.filter.BaseJwtProvider;
import com.dct.model.constants.BaseExceptionConstants;
import com.dct.model.constants.BaseHttpStatusConstants;
import com.dct.model.constants.BaseSecurityConstants;
import com.dct.model.constants.BaseUserConstants;
import com.dct.model.dto.auth.BaseTokenDTO;
import com.dct.model.dto.response.BaseResponseDTO;
import com.dct.model.exception.BaseAuthenticationException;
import com.dct.model.exception.BaseBadRequestException;
import com.dct.model.exception.BaseIllegalArgumentException;

import jakarta.transaction.Transactional;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private static final String ENTITY_NAME = "com.ars.userservice.service.impl.AuthServiceImpl";
    private final AuthenticationManager authenticationManager;
    private final BaseJwtProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           BaseJwtProvider tokenProvider,
                           PasswordEncoder passwordEncoder,
                           UserRepository userRepository,
                           RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public BaseResponseDTO register(RegisterRequestDTO requestDTO) {
        Users user = createUser(requestDTO);
        return BaseResponseDTO.builder()
                .code(BaseHttpStatusConstants.ACCEPTED)
                .message(ResultConstants.REGISTER_SUCCESS)
                .success(Boolean.TRUE)
                .result(user)
                .build();
    }

    @Override
    @Transactional
    public BaseResponseDTO registerShop(RegisterRequestDTO requestDTO) {
        Users user = createUser(requestDTO);
        Optional<Roles> role = roleRepository.findRoleByCode(BaseSecurityConstants.Role.DEFAULT);

        if (role.isPresent()) {
            user.setRoles(Set.of(role.get()));
            userRepository.save(user);
        } else {
            throw new BaseIllegalArgumentException(ENTITY_NAME, BaseExceptionConstants.ROLE_NOT_FOUND);
        }

        return BaseResponseDTO.builder()
                .code(BaseHttpStatusConstants.ACCEPTED)
                .message(ResultConstants.REGISTER_SUCCESS)
                .success(Boolean.TRUE)
                .result(user)
                .build();
    }

    @Override
    @Transactional
    public BaseResponseDTO authenticate(LoginRequestDTO loginRequest) {
        log.debug("Authenticating user: {}", loginRequest.getUsername());
        String username = loginRequest.getUsername().trim();
        String rawPassword = loginRequest.getPassword().trim();
        Authentication authentication = authenticate(username, rawPassword);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsCustom userDetails = (UserDetailsCustom) authentication.getPrincipal();
        AuthenticationResponseDTO results = new AuthenticationResponseDTO();
        Users user = userDetails.getUser();
        BeanUtils.copyProperties(user, results);
        results.setAuthorities(userDetails.getUserAuthorities());
        BaseTokenDTO authTokenDTO = BaseTokenDTO.builder()
                .authentication(authentication)
                .userId(user.getId())
                .username(user.getUsername())
                .rememberMe(loginRequest.getRememberMe())
                .build();

        String jwtAccessToken = tokenProvider.generateAccessToken(authTokenDTO);
        String jwtRefreshToken = tokenProvider.generateRefreshToken(authTokenDTO);
        results.setAccessToken(jwtAccessToken);
        results.setRefreshToken(jwtRefreshToken);

        return BaseResponseDTO.builder()
                .code(BaseHttpStatusConstants.ACCEPTED)
                .message(ResultConstants.LOGIN_SUCCESS)
                .success(Boolean.TRUE)
                .result(results)
                .build();
    }

    @Override
    public BaseResponseDTO logout() {
        return null;
    }

    private Users createUser(RegisterRequestDTO requestDTO) {
        if (userRepository.existsByUsername(requestDTO.getUsername())) {
            throw new BaseBadRequestException(ENTITY_NAME, BaseExceptionConstants.ACCOUNT_EXISTED);
        }

        return Users.builder()
                .username(StringUtils.trimToEmpty(requestDTO.getUsername()))
                .password(passwordEncoder.encode(StringUtils.trimToEmpty(requestDTO.getPassword())))
                .fullname(StringUtils.trimToEmpty(requestDTO.getFullname()))
                .email(StringUtils.trimToEmpty(requestDTO.getEmail()))
                .phone(StringUtils.trimToEmpty(requestDTO.getPhone()))
                .status(BaseUserConstants.Status.ACTIVE)
                .build();
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
}
