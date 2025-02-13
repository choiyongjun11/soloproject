package com.springboot.config;

import com.springboot.auth.filter.JwtAuthenticationFilter;
import com.springboot.auth.filter.JwtVerificationFilter;
import com.springboot.auth.handler.MemberAccessDeniedHandler;
import com.springboot.auth.handler.MemberAuthenticationEntryPoint;
import com.springboot.auth.handler.MemberAuthenticationFailureHandler;
import com.springboot.auth.handler.MemberAuthenticationSuccessHandler;
import com.springboot.auth.jwt.JwtTokenizer;
import com.springboot.auth.utils.AuthorityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/*
25-02-12 04:19 PM 김용준 고객님 - Q1) 이 메서드에서는 주로 무슨 기능을 담당하나요?
25-02-12 04:19 PM 최용준 개발자 - 답변 - API의 전반적인 보안을 담당하고 있습니다. 그리고 계정 로그인 시 ADMIN, USER 권한 별로 사용할 수 있는 역할도 지정합니다. 자세한 내용은 아래를 참고해 주십시오.

역할(Role) 기반 권한 적용
서버 측 리소스에 적절한 접근 권한 설정을 하지 않는다면 JWT를 사용하여 클라이언트의 자격 증명이 확인된다 하더라도 그 의미가 퇴색된다.
JWT를 이용한 자격 증명이라는 의미에는 특정 리소스에 접근할 수 있는 적절한 권한을 가졌는지를 판다해야 한다.
따라서, 접근 권한에 따라 특정 리소스에 접근 할 수 있도록 기능을 구현해줘야 한다.


역할(Role)에 대해서 예시로 설명 드리겠습니다.
 회원 등록의 경우 접근 권한 여부와 상관 없이 누구나 가능해야 하므로 다음과 같이 회원등록에 사용되는 접근을 허용해야 합니다.
  .antMatchers(HttpMethod.POST, /members").permitAll() ; 1-1) 참고


 */



@Configuration
public class SecurityConfiguration {
    private final JwtTokenizer jwtTokenizer;
    private final AuthorityUtils authorityUtils;

    public SecurityConfiguration(JwtTokenizer jwtTokenizer, AuthorityUtils authorityUtils) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .cors(Customizer.withDefaults())
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint())
                .accessDeniedHandler(new MemberAccessDeniedHandler())
                .and()
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers(HttpMethod.POST, "/*/members").permitAll()  //권한에 따른 http 메서드 사용 1-1)
                        .antMatchers(HttpMethod.PATCH, "/*/members/**").hasRole("USER") //일반 사용자만 회원 정보를 수정할 수 있다.
                        .antMatchers(HttpMethod.GET, "/*/members").hasRole("ADMIN") // 모든 회원 정보의 목록은 ADMIN 권한을 가진 사용만 접근이 가능하다.
                        .antMatchers(HttpMethod.GET, "/*/members/**").hasAnyRole("USER", "ADMIN") // 특정 회원에 대한 정보 조회는
                        .antMatchers(HttpMethod.DELETE, "/*/members/**").hasRole("USER")
                        .anyRequest().permitAll() //서버 측으로 들어온는 모든 request에 대해서 접근을 허용한다.


                         //1. 게시글 (board) - USER만 가능
                        //.antMatchers(HttpMethod.POST, "/boards").hasRole("USER")

                        //2. 댓글 (comment) - ADMIN만 가능
                        //.antMatchers(HttpMethod.POST, "/comments").hasRole("ADMIN")

                        //3. 조회수 (look) - USER, ADMIN 가능
                        //.antMatchers(HttpMethod.GET, "/boards/**").hasAnyRole("USER", "ADMIN")

                        //4. 좋아요 (like) - USER만 가능
                        //.antMatchers(HttpMethod.POST, "/boards/**/like").hasRole("USER")
                        //.antMatchers(HttpMethod.DELETE, "/boards/**/like").hasRole("USER")

                        //5. 업로드 (upload) - USER만 가능
                        //.antMatchers(HttpMethod.POST, "/boards/**/upload").hasRole("USER")

                        //6. 알림 (notify) - USER, ADMIN 가능
                        //.antMatchers(HttpMethod.POST, "/notify").hasAnyRole("USER", "ADMIN")


                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

     //JwtAuthenticationFilter를 SecurityConfiguration에 추가
    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {

         @Override
         public void configure(HttpSecurity builder) throws Exception {
             AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

             JwtAuthenticationFilter jwtAuthenticationFilter =
                     new JwtAuthenticationFilter(authenticationManager, jwtTokenizer);
             jwtAuthenticationFilter.setFilterProcessesUrl("/v1/auth/login");

             jwtAuthenticationFilter.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler());
             jwtAuthenticationFilter.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler());

             JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils);
                builder
                        .addFilter(jwtAuthenticationFilter)
                        .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);
         }
     }


}
