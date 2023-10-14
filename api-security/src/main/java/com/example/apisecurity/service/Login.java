package com.example.apisecurity.service;

import lombok.Getter;

public class Login {
    @Getter
    private final Jwt refreshToken;
    @Getter
    private final Jwt accessToken;

    private static final long ACCESS_VALIDITY = 2l;
    private static final long REFRESH_VALIDITY = 2880l;

    public static Login of(Long userId,String accessSecret, String refreshSecret){
        return new Login(

                Jwt.of(userId,REFRESH_VALIDITY,refreshSecret),
                Jwt.of(userId,ACCESS_VALIDITY,accessSecret)

        );
    }

    public Login(Jwt accessToken, Jwt refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
