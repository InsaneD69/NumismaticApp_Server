package com.NumismaticApp.Server.NumismaticApp.Security;

import com.NumismaticApp.Server.NumismaticApp.Entity.UserEntity;
import com.NumismaticApp.Server.NumismaticApp.Exception.UserNotFoundException;
import com.NumismaticApp.Server.NumismaticApp.Exception.WrongPasswordException;
import com.NumismaticApp.Server.NumismaticApp.repository.Model.User;
import com.NumismaticApp.Server.NumismaticApp.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.ClientAuthConfig;
import javax.security.auth.message.config.ServerAuthConfig;
import java.util.ArrayList;
import java.util.List;

@Component
public class AuthProviderImpl implements AuthenticationProvider {

    @Autowired
    private UserRepo userRepo;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UserEntity user = userRepo.findByUsername(authentication.getName());

        if (user == null) {
            throw new UsernameNotFoundException("Пользователь с таким логином не существует");
        }

        if (!user.getPassword().equals(user.getPassword())){

            throw new BadCredentialsException("неверный пароль");

        }
        List<GrantedAuthority> authorities = new ArrayList<>();

        return new UsernamePasswordAuthenticationToken(user,null,authorities);



    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}