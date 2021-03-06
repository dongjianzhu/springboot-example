package com.example.security.config;

import com.example.security.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletResponse;

/**
 * @author: dongjianzhu
 * @create: 2021-09-24 07:49
 **/
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceImpl userDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and().formLogin().loginPage("/login.html").permitAll().loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json");
                    response.getWriter().println("{\"exceptionId\":\"null\",\"messageCode\":\"200\"," +
                            "\"message\": \"Login successfully.\",\"serverTime\": " + System.currentTimeMillis() +"}");
                }).failureHandler((request, response, exception) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json");
                    response.getWriter().println("{\"exceptionId\":\"null\",\"messageCode\":\"401\"," +
                            "\"message\": \""+ exception.getMessage() +"\",\"serverTime\": " + System.currentTimeMillis() +"}");
                }).and().exceptionHandling().and().csrf().disable();
        //????????????????????????
        http.authorizeRequests(requests -> requests.anyRequest().authenticated());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    public static void main(String[] args) {
        //$2a$10$r8G5IAYKzNbVRs4mCGCAMuWp8aSY17NipAQHUGMcLaNnaz9oSVOkS
//        String encode = new BCryptPasswordEncoder().encode("1234");
//        System.out.println(encode);

        boolean flag = new BCryptPasswordEncoder().matches("1235",
                "$2a$10$r8G5IAYKzNbVRs4mCGCAMuWp8aSY17NipAQHUGMcLaNnaz9oSVOkS");
        System.out.println(flag);
    }

}
