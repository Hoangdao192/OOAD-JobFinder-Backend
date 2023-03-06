package com.uet.jobfinder.security;

import com.uet.jobfinder.entity.User;
import com.uet.jobfinder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username not exists."));
        return user;
    }

    public User loadUserById(String id) {
        User user = userRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new IllegalArgumentException("User with ID not found."));
        return user;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
