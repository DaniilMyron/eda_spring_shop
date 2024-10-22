package com.miron.security_lib.old;

public record Tokens (String accessToken, String accessTokenExpiring,
                      String refreshToken, String refreshTokenExpiring){
}
