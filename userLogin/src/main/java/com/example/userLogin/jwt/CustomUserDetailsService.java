package com.example.userLogin.jwt;

import com.example.userLogin.entity.UserEntity;
import com.example.userLogin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      UserEntity user = userRepository.findByUserEmail(username)
               .orElseThrow(() -> new UsernameNotFoundException("User Not Found: "+ username));

        return new CustomUserDetails(user);
    }
}
