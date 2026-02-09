package com.gelir.gider.service;

import com.gelir.gider.dto.LoginRequest;
import com.gelir.gider.dto.LoginResponse;
import com.gelir.gider.entity.Role;
import com.gelir.gider.entity.User;
import com.gelir.gider.repository.RoleRepository;
import com.gelir.gider.repository.UserRepository;
import com.gelir.gider.security.JwtTokenProvider;
import com.gelir.gider.security.RedisTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final AuthenticationManager authenticationManager;
        private final JwtTokenProvider jwtTokenProvider;
        private final RedisTokenService redisTokenService;
        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final PasswordEncoder passwordEncoder;

        @Transactional
        public LoginResponse login(LoginRequest request) {
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String token = jwtTokenProvider.generateToken(userDetails);

                User user = userRepository.findByUsernameAndDeletedFalse(request.getUsername())
                                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

                String roles = userDetails.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.joining(","));

                redisTokenService.storeToken(token, user.getId(), roles, Duration.ofHours(1));

                return LoginResponse.builder()
                                .token(token)
                                .username(user.getUsername())
                                .fullName(user.getFullName())
                                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                                .build();
        }

        @Transactional
        public void logout(String token) {
                redisTokenService.deleteToken(token);
        }

        @Transactional
        public void register(com.gelir.gider.dto.UserDTO userDTO) {
                if (userRepository.existsByUsername(userDTO.getUsername())) {
                        throw new RuntimeException("Bu kullanıcı adı zaten kullanılıyor");
                }

                if (userRepository.existsByEmail(userDTO.getEmail())) {
                        throw new RuntimeException("Bu e-posta adresi zaten kullanılıyor");
                }

                User user = User.builder()
                                .username(userDTO.getUsername())
                                .password(passwordEncoder.encode(userDTO.getPassword()))
                                .email(userDTO.getEmail())
                                .fullName(userDTO.getFullName())
                                .enabled(true)
                                .accountNonLocked(true)
                                .build();

                // Varsayılan olarak USER rolü ata
                Role userRole = roleRepository.findByName("ROLE_USER")
                                .orElseThrow(() -> new RuntimeException(
                                                "ROLE_USER bulunamadı. Lütfen veritabanını kontrol edin."));

                user.addRole(userRole);
                userRepository.save(user);
        }
}
