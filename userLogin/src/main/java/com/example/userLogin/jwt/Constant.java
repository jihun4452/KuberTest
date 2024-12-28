package com.example.userLogin.jwt;

public class Constant {
    public static final long ACCESS_TOKEN_EXPIRES_TIME = 1000 * 60 * 30; // 30분
    public static final long REFRESH_TOKEN_EXPIRES_TIME = 1000 * 60 * 60 * 24; // 1일
    public static final String GRANT_TYPE = "Bearer ";
}
