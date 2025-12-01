package org.delivery.voda.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.delivery.voda.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    //디비에서 유저 찾아서 반환
    return userRepository.findByEmail(email)
        .orElseThrow(()-> new UsernameNotFoundException("해당 이메일 유저 찾을 수 없음" + email));
  }
}
