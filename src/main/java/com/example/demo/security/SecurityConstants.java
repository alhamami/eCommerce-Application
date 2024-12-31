package com.example.demo.security;

public class SecurityConstants {
    public static String SECRET = "outSecret";
    public static long EXPIRATION_TIME = 864_000_000; //10 days
    public static String TOKEN_PREFIX = "Bearer ";
    public static String HEADER_STRING = "Authorization";
    public static String SIGN_UP_URL = "/api/user/create";
    public static String LOG_IN_URL = "/login";
}