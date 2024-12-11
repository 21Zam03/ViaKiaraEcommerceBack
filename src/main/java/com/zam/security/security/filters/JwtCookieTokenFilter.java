package com.zam.security.security.filters;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.zam.security.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

public class JwtCookieTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtCookieTokenFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("auth_token".equals(cookie.getName())) {

                    String jwtToken = cookie.getValue();

                    DecodedJWT decodedJWT = jwtUtil.verifyToken(jwtToken);

                    String username = jwtUtil.extractUsername(decodedJWT);
                    String stringAuthorities = jwtUtil.getSpecificClaim(decodedJWT, "authorities").asString();

                    Collection<? extends GrantedAuthority> authorities =
                            AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);

                    Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    break;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

}
