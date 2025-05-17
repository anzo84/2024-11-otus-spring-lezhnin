package ru.otus.hw.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.hw.mapper.UserMapper;
import ru.otus.hw.persistence.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository repository;

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
            .map(userMapper::mapWithPassword)
            .map(user -> new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRoles()))
            .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}