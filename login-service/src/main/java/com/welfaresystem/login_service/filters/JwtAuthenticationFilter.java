package com.welfaresystem.login_service.filters;

import com.welfaresystem.login_service.jwt.JwtUtil;
import com.welfaresystem.login_service.services.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;



public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserDetailsService userDetailsService;
    private final String jwtSecret;

    public JwtAuthenticationFilter(UserDetailsService userDetailsService, String jwtSecret) {
        this.userDetailsService = userDetailsService;
        this.jwtSecret = jwtSecret;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(jwtSecret.getBytes())
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

               /* String userIdentifier = claims.getSubject();
                UserDetails userDetails = userDetailsService.loadUserByUsernameOrEmail(userIdentifier);
*/
                return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            } catch (SignatureException e) {
                throw new RuntimeException("Invalid JWT signature.");
            }
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}