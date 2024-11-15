package com.example.practice_241115.config;

import jakarta.servlet.annotation.WebListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@WebListener
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain (HttpSecurity httpSecurity) throws Exception{
        httpSecurity

        // 권한 페이지 접속권한
                .authorizeHttpRequests(
                        authorization -> authorization
                                .requestMatchers("/user/login/**").permitAll()  //로그인 페이지는 누구나 접속가능
                                .requestMatchers("/board/register").authenticated() // 로그인 한 사람만 글 등록가능
                                .requestMatchers("/item/register").hasRole("ADMIN") // 로그인 한 사람만 상품등록 가능
                                .requestMatchers("/user/list").hasRole("ADMIN")     // 로그인 한 사람만 유저정보 확인 가능
                                .anyRequest().permitAll()       // 그 외 모든 권한 열림
//                                .anyRequest().authenticated() // 그 외 모든 권한 로그인한 사람으로 제한


                )

        // 위변조 방지 웹에서 form 태그 변경 등의 변조를 방지
                .csrf(csrf -> csrf.disable())
        // 로그인 기능
                .formLogin(formLogin->formLogin.loginPage("/user/login")    // 기본 로그인 페이지 지정
                        .defaultSuccessUrl("/user/login")                   // 로그인이 성공했다면
                        .usernameParameter("email")                         // 로그인 <input> name ="email">
                )
        // 로그아웃
                .logout(
                        logout->logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))    // 로그아웃 a태그라 생각
                                .invalidateHttpSession(true)                                           // 세션초기화
                                .logoutSuccessUrl("/")                                                 // localhost:8090 으로 간다.
                                                                                                        //dsn 주소일 경우 www.naver.com으로 까지간다.
                                                                                                        // 컨트롤러에서 만든다
                );
        // 예외처리 // 로그인이 되지 않은 사용자, 권한이 없는 사용자 접속시 취할 행동들
        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new  BCryptPasswordEncoder();
    }
}
