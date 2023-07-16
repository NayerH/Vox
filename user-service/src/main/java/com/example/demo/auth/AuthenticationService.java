package com.example.demo.auth;

import com.example.demo.config.JwtService;
import com.example.demo.redis.RedisService;
import com.example.demo.token.Token;
import com.example.demo.token.TokenRepository;
import com.example.demo.token.TokenType;
import com.example.demo.user.Role;
import com.example.demo.user.Users;
import com.example.demo.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UsersRepository repository;
    private final TokenRepository tokenRepository;
    private final   RedisService redisService;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var userEmail = request.getEmail();
        if (repository.existsByEmail(userEmail)) {
            throw new RuntimeException("Email already exists");
        }

        var user = Users.builder().username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .first_name(request.getFirstname())
                .last_name(request.getLastname())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .role(request.getRole()).build();
       var savedUser=repository.save(user);

        var jwtToken= jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }



    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        var user= repository.findUsersByEmail(request.getEmail())
        .orElseThrow();
        var jwtToken= jwtService.generateToken(user);
       // var tokenToExclude = getTokenFromHeader(request.getHeader("Authorization")); // extract token from header
        revokeAllUserTokens(user);
       // revokeAllUserTokens(user);

      Token token=  saveUserToken(user,jwtToken);
      //  revokeAllUserTokens(user);

        redisService.execute(token);

        return AuthenticationResponse.builder().token(jwtToken).build();

    }

    public AuthenticationResponse updateUser(Users newUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
        System.out.println("new user details here:"+newUser);
        var oldUser= repository.findUsersByEmail(userEmail)
                .orElseThrow(()-> new UsernameNotFoundException("user not found"));;
       var user= repository.findUsersByEmail(userEmail)
                .map(users -> {
                    if(newUser.getFirst_name() != null) {
                        users.setFirst_name(newUser.getFirst_name());
                    }
                    if(newUser.getEmail() != null) {
                        users.setEmail(newUser.getEmail());
                    }
                    if(newUser.getLast_name() != null) {
                        users.setLast_name(newUser.getLast_name());
                    }
                    if(newUser.getUser_name() != null) {
                        users.setUsername(newUser.getUser_name());
                    }
                    if(newUser.getPassword() != null) {
                        users.setPassword(passwordEncoder.encode(newUser.getPassword()));
                    }
                    return repository.save(users);
                }).orElseThrow(()-> new UsernameNotFoundException("user not found"));

        var jwtToken= jwtService.generateToken(user);
        // var tokenToExclude = getTokenFromHeader(request.getHeader("Authorization")); // extract token from header
        revokeAllUserTokens(user);
        revokeAllUserTokens(oldUser);
        // revokeAllUserTokens(user);

        saveUserToken(user,jwtToken);
        //  revokeAllUserTokens(user);
        System.out.println("user Email:  " +userEmail);
        return AuthenticationResponse.builder().token(jwtToken).build();





    }

//    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
//        var user = repository.findUsersByEmail(request.getEmail()).orElseThrow();
//        var jwtToken = jwtService.generateToken(user);
//
//        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
//
//        if (!validUserTokens.isEmpty()) {
//            for (Token token : validUserTokens) {
//                if (!jwtToken.equals(token.getToken())) {
//                    token.setRevoked(true);
//                    token.setExpired(true);
//                }
//            }
//            tokenRepository.saveAll(validUserTokens);
//        }
//
//        saveUserToken(user, jwtToken);
//
//        return AuthenticationResponse.builder().token(jwtToken).build();
//    }

    private Token saveUserToken(Users user, String jwtToken) {
        var token= Token.builder().user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
       return tokenRepository.save(token);
    }
    private void revokeAllUserTokens(Users user) {


        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());

        if (!validUserTokens.isEmpty()) {

            validUserTokens.forEach(t -> {

                t.setRevoked(true);
                t.setExpired(true);

            });
            tokenRepository.saveAll(validUserTokens);
        }
    }
}
