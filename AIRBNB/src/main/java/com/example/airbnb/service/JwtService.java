package com.example.airbnb.service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.aspectj.weaver.bcel.UnwovenClassFileWithThirdPartyManagedBytecode;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//
//@Service
//public class JwtService {
//
//    // Must be at least 32 characters long
//    private final String SECRET_KEY = "my_super_secret_key_which_must_be_long_enough_for_security";
//
//    private SecretKey getSigningKey() {
//        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
//    }
//
//    public String generateToken(String email, long expirationTimeInMs) {
//        return Jwts.builder()
//                .subject(email)
//                .issuedAt(new Date())
//                .expiration(new Date(System.currentTimeMillis() + expirationTimeInMs))
//                .signWith(getSigningKey())
//                .compact();
//    }
//
//    public String extractEmail(String token) {
//        return Jwts.parser()
//                .verifyWith(getSigningKey()) // Replaces setSigningKey
//                .build()
//                .parseSignedClaims(token)    // Replaces parseClaimsJws
//                .getPayload()                // Replaces getBody
//                .getSubject();
//    }
//}

@Service
public class JwtService
{
    private String secretKey= "";

    //TO Generate Key
    public JwtService()
    {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = keyGen.generateKey();
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());


        }
        catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }

    private Key getKey()
    {
//        as hmacShakeyFor works on byte so we need to convert the secret key into
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(String useremail)
    {
       Map<String, Object> claims = new HashMap<>();
       return Jwts.builder()
               .claims()
               .add(claims)
               .subject(useremail)
               .issuedAt(new Date(System.currentTimeMillis()))
               .expiration(new Date(System.currentTimeMillis()  + 60 *60 * 10))
               .and()
               .signWith(getKey())
               .compact();


    }


    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey()) // Use the same key to unlock
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject(); // Returns the username/email you put in
    }

}
