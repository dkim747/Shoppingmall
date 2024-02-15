package com.danny.shoppingmall.security;

import com.danny.shoppingmall.user.dto.UserDTO;
import com.danny.shoppingmall.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("------------------------");
        log.info(username);

        UserDTO userDTO = userRepository.findByUserEmail(username).get().EntityToDTO();

        return new PrincipalDetails(userDTO);
    }
}
