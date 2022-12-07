package com.numismatic_app.server.security;

import com.numismatic_app.server.dto.JWTResponse;
import com.numismatic_app.server.dto.UserDto;
import com.numismatic_app.server.entity.UserEntity;
import com.numismatic_app.server.exception.UserNotFoundException;
import com.numismatic_app.server.repository.UserRepo;
import com.sun.istack.NotNull;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthProviderImpl  {


    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JWTProvider jwtProvider;

    private final int accessTokenExpMinute=100;
    private final int refreshTokenExpMinute =52560000;


    public JWTResponse login(@NotNull UserDto authentication) throws  AuthException {

        final UserEntity user = userRepo.findByUsername(authentication.getUsername());


        if (user==null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }


        if (!passwordEncoder.matches(authentication.getPassword(),user.getPassword())){
            log.info("User "+user.getUsername()+" not connected to server (mistake password)");
            throw new BadCredentialsException("неверный пароль");


        }

        final String accessToken = jwtProvider.generateAccessToken(user, accessTokenExpMinute);
        final String refreshToken = jwtProvider.generateRefreshToken(user, refreshTokenExpMinute);
        refreshStorage.put(user.getUsername(), refreshToken);
        log.info("Generate for user  "+user.getUsername()+" refresh token: "+refreshToken);
        return new JWTResponse(accessToken, refreshToken,accessTokenExpMinute,refreshTokenExpMinute);


    }

    public JWTResponse getAccessToken(@NotNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(username);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final UserEntity user = userRepo.findByUsername(username);
                if (user==null) {
                     new UserNotFoundException("Пользователь не найден");
                }
                final String accessToken = jwtProvider.generateAccessToken(user,accessTokenExpMinute);
                return new JWTResponse(accessToken, null,accessTokenExpMinute,null);
            }
        }
        return new JWTResponse(null, null,null,null);
    }

    public JWTResponse refresh(@NotNull String refreshToken) throws AuthException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(username);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final UserEntity user = userRepo.findByUsername(username);
                if (user==null) {
                     new UsernameNotFoundException("Пользователь не найден");
                }
                final String accessToken = jwtProvider.generateAccessToken(user,accessTokenExpMinute);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user, refreshTokenExpMinute);
                refreshStorage.put(user.getUsername(), newRefreshToken);
                return new JWTResponse(accessToken, newRefreshToken,accessTokenExpMinute,refreshTokenExpMinute);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }

    public JWTAuthentication getAuthInfo() {
        return (JWTAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}
