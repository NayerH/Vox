package com.example.demo.config;


import com.example.demo.token.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        logger.info("authHeader:  "+ authHeader);
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            logger.info("got in first if:  "+ authHeader);
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        logger.info("userEmail:  "+ userEmail);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("in");
            UserDetails userDetails= this.userDetailsService.loadUserByUsername(userEmail);
            logger.info("userDetails:  "+userDetails);
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired()&& !t.isRevoked())
                    .orElse(false);
            logger.info("token found here: " +tokenRepository.findByToken(jwt));
            logger.info("first condition: " + jwtService.isTokenValid(jwt,userDetails));
            logger.info("SECOND condition: " + isTokenValid);

            if(jwtService.isTokenValid(jwt,userDetails)&& isTokenValid){
                logger.info("got in second if valid Token here :  ");
                UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                logger.info("new authentication tokennnn :  "+ authToken);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);


    }
}
