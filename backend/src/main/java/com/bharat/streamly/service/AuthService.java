package com.bharat.streamly.service;


import com.bharat.streamly.document.UserDocument;
import com.bharat.streamly.dto.TokenResponse;
import com.bharat.streamly.entity.RefreshToken;
import com.bharat.streamly.entity.User;
import com.bharat.streamly.exception.EmailAlreadyExistsException;
import com.bharat.streamly.exception.InvalidCredentialsException;
import com.bharat.streamly.exception.InvalidRefreshTokenException;
import com.bharat.streamly.exception.UsernameAlreadyExistsException;
import com.bharat.streamly.repository.RefreshTokenRepository;
import com.bharat.streamly.repository.UserRepository;
import com.bharat.streamly.repository.UserSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserSearchRepository userSearchRepository;
    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    public User signup(String email, String username, String password) {
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        try {
            User saved = userRepository.save(user);
            userSearchRepository.save(new UserDocument(saved.getId(), saved.getUsername()));
            return saved;
        } catch (DataIntegrityViolationException e) {
            if (userRepository.existsByUsername(username)) {
                throw new UsernameAlreadyExistsException("This username is already taken");
            }
            throw new EmailAlreadyExistsException("This email is already registered");
        }
    }

    public TokenResponse login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        return new TokenResponse(jwtService.generateToken(user.getId()), createRefreshToken(user.getId()).getToken());
    }

    private RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiresAt(LocalDateTime.now().plusSeconds(refreshExpiration / 1000));
        return refreshTokenRepository.save(refreshToken);
    }

    public TokenResponse refresh(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));

        if (refreshToken.isRevoked()) {
            revokeAllTokens(refreshToken.getUserId());
            throw new InvalidRefreshTokenException(
                    "This refresh token was already used. All sessions have been logged out. Please login again.");
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidRefreshTokenException("Refresh token has expired. Please login again.");
        }
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
        return new TokenResponse(
                jwtService.generateToken(refreshToken.getUserId()),
                createRefreshToken(refreshToken.getUserId()).getToken());
    }

    private void revokeAllTokens(Long userId) {
        List<RefreshToken> tokens = refreshTokenRepository.findAllByUserId(userId);
        tokens.forEach(t -> t.setRevoked(true));
        refreshTokenRepository.saveAll(tokens);
    }

}
