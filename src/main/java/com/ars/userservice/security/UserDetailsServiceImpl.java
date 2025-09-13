package com.ars.userservice.security;

import com.ars.userservice.dto.mapping.IAuthenticationDTO;
import com.ars.userservice.entity.Users;
import com.ars.userservice.repository.AuthorityRepository;
import com.ars.userservice.repository.UserRepository;
import com.dct.model.constants.BaseExceptionConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public UserDetailsServiceImpl(UserRepository userRepository,
                                  AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        log.debug("Bean 'UserDetailsServiceImpl' is configured for load user credentials info");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Load user by username: {}", username);
        Optional<IAuthenticationDTO> authentication = userRepository.findAuthenticationByUsernameOrEmail(username);

        if (authentication.isEmpty()) {
            throw new UsernameNotFoundException(BaseExceptionConstants.ACCOUNT_NOT_FOUND);
        }

        Users user = new Users();
        BeanUtils.copyProperties(authentication.get(), user);
        Set<String> userPermissions = authorityRepository.findAllByUserId(user.getId());
        Collection<SimpleGrantedAuthority> userAuthorities = userPermissions
                .stream()
                .filter(StringUtils::hasText)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        return UserDetailsCustom.customBuilder()
                .user(user)
                .status(user.getStatus())
                .authorities(userAuthorities).build();
    }
}
