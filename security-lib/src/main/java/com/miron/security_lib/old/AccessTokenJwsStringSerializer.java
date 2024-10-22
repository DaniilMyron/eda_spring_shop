package com.miron.security_lib.old;

import com.miron.security_lib.models.Token;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.util.function.Function;

public class AccessTokenJwsStringSerializer implements Function<Token, String> {
    private final JWSSigner jwsSigner;
    private JWSAlgorithm jwsAlgorithm = JWSAlgorithm.HS256;
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenJwsStringSerializer.class);

    public AccessTokenJwsStringSerializer(JWSSigner jwsSigner) {
        this.jwsSigner = jwsSigner;
    }

    public void setJwsAlgorithm(JWSAlgorithm jwsAlgorithm) {
        this.jwsAlgorithm = jwsAlgorithm;
    }

    public AccessTokenJwsStringSerializer(JWSSigner jwsSigner, JWSAlgorithm jwsAlgorithm) {
        this.jwsSigner = jwsSigner;
        this.jwsAlgorithm = jwsAlgorithm;
    }

    @Override
    public String apply(Token token) {
        var jwsHeader = new JWSHeader.Builder(this.jwsAlgorithm)
                .keyID(token.id().toString())
                .build();
        var jwtClaimsSet = new JWTClaimsSet.Builder()
                .jwtID(token.id().toString())
                .subject(token.subject())
                .issueTime(Date.from(token.createdAt()))
                .expirationTime(Date.from(token.expiresAt()))
                .claim("authorities", token.authorities())
                .build();
        var signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        try {
            signedJWT.sign(this.jwsSigner);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
}
