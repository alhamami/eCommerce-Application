package com.example.demo.security;

import com.auth0.jwt.JWT;
import com.example.demo.controllers.UserController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Component
public class JWTAuthenticationVerficationFilter extends BasicAuthenticationFilter {

    public static final Logger logger = LogManager.getLogger(UserController.class);


    public JWTAuthenticationVerficationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String header = req.getHeader(SecurityConstants.HEADER_STRING);

        if (header == null) {
            logger.info("No Authorization header found: "+ req.getRemoteAddr());
            chain.doFilter(req, res);
            return;
        }


        if (!header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            logger.warn("Authorization header does not start with expected token prefix: "+req.getRemoteAddr()+" Header: "+header);
            chain.doFilter(req, res);
            return;
        }

        logger.info("Authorization header found: "+ req.getRemoteAddr());

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        if (authentication == null) {
            logger.error("Invalid or missing authentication token for user: "+req.getRemoteAddr());
        } else {
            logger.info("Authentication successfully extracted for user: "+ authentication.getName());
        }


        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
        String token = req.getHeader(SecurityConstants.HEADER_STRING);

        if (token == null) {
            logger.info("No Authorization token found: "+ req.getRemoteAddr());
            return null;
        }


        if (token != null) {

            String user = JWT.require(HMAC512(SecurityConstants.SECRET.getBytes())).build()
                    .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                    .getSubject();
            if (user != null) {
                logger.info("User authenticated successfully: "+ user);
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            logger.warn("Token is invalid or user not found"+ req.getRemoteAddr());
            return null;
        }
        logger.warn("Token is invalid or user not found"+ req.getRemoteAddr());
        return null;
    }
}