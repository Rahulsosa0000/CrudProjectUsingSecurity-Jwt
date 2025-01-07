package com.crud.services;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;


import org.springframework.stereotype.Service;

import com.crud.config.MyUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	
	

	
	private SecretKey secretkey =Keys.hmacShaKeyFor("afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf".getBytes());
   
// cryptographic algorithms work with byte data

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 ))
                //.expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .and()
                .signWith(secretkey) // key use for token signing
                .compact();  // converts the token into a string format

    }
    
    public String againGenerateToken(String username) {
        return generateToken(username); // Reuse the existing token generation logic
    }

    

    public String extractUserName(String token) {
        // retrieve username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims); //A claim is a piece of information stored in a JWT token.
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser() //A parser is used to decode and validate a JWT token.
                .verifyWith(secretkey) // verify secret key
                .build()  
                .parseSignedClaims(token) //header ,PayLoad, signature
                .getPayload();  // retrieves the PayLoad which contain actual claims (information like sub,iat, exp) 
    }

    public boolean validateToken(String token, MyUserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // check if the token has expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // retrieve expiration date from jwt token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }







	

	

}