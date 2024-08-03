package com.major.majorproject.AuthImplementaion;

import com.major.majorproject.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTServiceImpl implements JWTService {
    public String extractUserName(String token){//extracting UserName(email) in my case for further proceedings
        return extractClaim(token, Claims::getSubject);//method reference
        //I can even do
        //return extractClaim(token,claims->claims.getSubject());

    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){//Gives particular data
        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder() //component for extracting its components
                .setSigningKey(getSigninKey())//checking whether it is the same user using signingkey
                .build()
                .parseClaimsJws(token)//seperating into its constituents
                .getBody();
    }

    private Key getSigninKey() {
        byte key[]= Decoders.BASE64.decode("413F4428472B4B6250655368566D5970337336763979244226452948404D6351");
        return Keys.hmacShaKeyFor(key);//signing the key
    }

    public String generateToken(UserDetails userDetails){//token for generating
        return Jwts.builder().setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact(); //returns a token

    }
    public String generateRefreshToken(Map<String,Object> extraClaims, UserDetails userDetails){
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*24))
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact(); //returns a token

    }
    public boolean isTokenValid(String token,UserDetails userDetails){
        final String username=extractUserName(token);
        return(username.equals(userDetails.getUsername())&& !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){ //Checking the expiry
        return extractClaim(token,Claims::getExpiration).before(new Date());
    }
}
