package com.major.majorproject.Filter;

import com.major.majorproject.service.JWTService;
import com.major.majorproject.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    private final UserService userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");//getting authHeader in Authorization
        final String jwt;//jwt
        final String userEmail;// user email
        if (authHeader == null || !authHeader.startsWith("Bearer ")) { //If there is no token
            filterChain.doFilter(request, response); //stop the process
            return;
        }
        jwt = authHeader.substring(7);//Extracting the token part from authorization
        userEmail = jwtService.extractUserName(jwt); //Extract user name
        if (userEmail != null || SecurityContextHolder.getContext().getAuthentication() == null) { //If the user has not yet authenticated , then authenitcate it
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);
//            Boolean isTokenValid = tokenRepository.findByToken(jwt)
//                    .map(t -> !t.isExpired() && !t.isRevoked())
//                    .orElse(false);
            if (jwtService.isTokenValid(jwt, userDetails)) { //checking the validity of tht token
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();//To update the security context holder
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(userDetails);
                securityContext.setAuthentication(usernamePasswordAuthenticationToken);
                SecurityContextHolder.setContext(securityContext);
            }

        }
        filterChain.doFilter(request, response);
    }
}
