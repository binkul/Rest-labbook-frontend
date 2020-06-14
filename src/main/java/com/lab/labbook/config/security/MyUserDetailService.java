package com.lab.labbook.config.security;

import com.lab.labbook.client.UserClient;
import com.lab.labbook.domain.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class MyUserDetailService implements UserDetailsService {

    private final UserClient userClient = new UserClient();

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserDto userDto = userClient.getByLogin(login);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + userDto.getRole()));
        return new org.springframework.security.core.userdetails.User(
                userDto.getLogin(),
                userDto.getPassword(),
                true,
                true,
                true,
                !userDto.isBlocked(),
                grantedAuthorities);
    }
}
