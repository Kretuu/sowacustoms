package pl.sowacustoms.security.authorization;

import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.sowacustoms.config.AppConfig;

import java.util.Date;

@Service
public class TokenService {
    private final AppConfig appConfig;

    public TokenService(AppConfig appConfig) {
        this.appConfig = appConfig;
    }


    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return generateToken(userPrincipal);
    }

    public String createToken(UserDetails userDetails) {
        UserPrincipal userPrincipal = (UserPrincipal) userDetails;
        return generateToken(userPrincipal);
    }

    private String generateToken(UserPrincipal principal) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appConfig.getTokenExpirationMsec());

        return Jwts.builder().setSubject(principal.getId()).setIssuedAt(new Date())
                .setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512, appConfig.getTokenSecret()).compact();
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(appConfig.getTokenSecret()).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(appConfig.getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            System.out.println("Token validation error.");
        }
        return false;
    }
}
