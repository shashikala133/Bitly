package com.url.shortener.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    //single execution per request
    @Autowired
    private JwtUtils jwtTokenProvider;
    @Autowired
    UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
     try{
       //get Jwt from header
         String jwt=jwtTokenProvider.getJWTFromHeader(request);
         if(jwt!=null && jwtTokenProvider.validateToken(jwt)){
             String username=jwtTokenProvider.getUserNameFromJwtToken(jwt);
             UserDetails userDetails=userDetailsService.loadUserByUsername(username);
             if(userDetails!=null){
                 UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                 authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                 SecurityContextHolder.getContext().setAuthentication(authenticationToken);
             }
         }
       //Validate token
       //if valid get user details
       //--get user name -> load user -> set the auth context
     }catch (Exception e){
       e.printStackTrace();
     }
     filterChain.doFilter(request,response);
    }


}
