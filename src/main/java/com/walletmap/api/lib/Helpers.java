package com.walletmap.api.lib;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class Helpers {

    private static SecretKey secretKey = Keys
            .hmacShaKeyFor(Decoders.BASE64.decode("QZFTf7QZFTf74czgwgdfKEHpLqDz1Tr0onffje4czgwgdfKEHpLqDz1Tr0onffje"));

    public static String generateJWT(String id) {

        String jws = Jwts
                .builder()
                .subject(id)
                .signWith(secretKey)
                .compact();
        return jws;
    }

    public static String parseJWT(String token) {
        Jws<Claims> jws;

        try {
            jws = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

            return jws.getPayload().getSubject();
        } catch (JwtException ex) { // (5)
            return ex.toString();
            // we *cannot* use the JWT as intended by its creator
        }
    }
}
