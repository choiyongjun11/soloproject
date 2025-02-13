package com.springboot.auth.filter;

import com.springboot.AuthorityUtils;
import com.springboot.jwt.JwtTokenizer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthorityUtils authorityUtils;
    private final JwtTokenizer jwtTokenizer;

    public JwtAuthenticationFilter(AuthorityUtils authorityUtils, JwtTokenizer jwtTokenizer) {
        this.authorityUtils = authorityUtils;
        this.jwtTokenizer = jwtTokenizer;
    }

}
