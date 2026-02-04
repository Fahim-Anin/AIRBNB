package com.example.airbnb.service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.aspectj.weaver.bcel.UnwovenClassFileWithThirdPartyManagedBytecode;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.beans.factory.annotation.Value;
@Service
public class JwtService
{

//    Raw way to generate secret key:-

//    private String secretKey= "";
//
////    Generate Key here
////    This means Spring creates only one instance of JwtService when the application starts and keeps it in memory.
////
////  We put the key generation in the constructor so that the "key" is created once and only once.
//    public JwtService()
//    {
//        try {
//            // 1. Initialize a 'factory' specifically for the HmacSHA256 algorithm.
//            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//
//            // 2. Generate a high-entropy, cryptographically secure random key.
//            SecretKey sk = keyGen.generateKey();
//
//            // 3. Convert the raw binary key into a Base64 String so we can store it
//            // in our 'secretKey' variable easily.
//            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
//
//
//        }
//        catch (NoSuchAlgorithmException e){
//            // If the JVM doesn't support the algorithm, we must stop the app.
//            throw new RuntimeException(e);
//        }
//    }

@Value("${jwt.secret}")
private String secretKey; // Spring injects the fixed key from properties

    /**
     * HELPER METHOD: Translates our stored String back into a 'Key' object.
     * We need this because the JJWT library cannot sign with a String;
     * it needs a 'javax.crypto.SecretKey' object to perform the math.
     */
    private Key getKey()
    {
//      // 1. Decode the Base64 string back into the original raw byte array.
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//
//        // 2. Wrap those bytes into a HMAC-specific Key object that the library understands.
       return Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(String userEmail, String role) {
        // A Map to hold any extra data we want to hide in the token (e.g., Roles).
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return Jwts.builder()
                .claims()               // Start the Claims section.
                .add(claims)            // Add our (currently empty) map.
                .subject(userEmail)     // Put the "Specific Element" (Identity) in the Payload.
                .issuedAt(new Date(System.currentTimeMillis())) // Set the "Birth Date".
                // Set "Expiration Date" (10 hours from now).
                .expiration(new Date(System.currentTimeMillis() +  10 * 1000))
                .and()                  // Close Claims section and return to main Builder.
                .signWith(getKey())     // CALLS getKey() to create the Digital Signature.
                .compact();             // Combines Header.Payload.Signature into one String.
    }


    public String extractUserEmail(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey()) // Uses the same 'Royal Seal' to unlock the box.
                .build()
                .parseSignedClaims(token)         // Decodes the string and verifies the Signature.
                .getPayload()                     // Grabs the data if (and only if) the signature is valid.
                .getSubject();                    // Extracts the email we put in during generation.
    }

    public String extractRole(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey()) // Uses the same 'Royal Seal' to unlock the box.
                .build()
                .parseSignedClaims(token)         // Decodes the string and verifies the Signature.
                .getPayload()                     // Grabs the data if (and only if) the signature is valid.
                .get("role", String.class);            // Extracts the role we put in during token generation
    }


}
