package pl.sowacustoms.security.authorization;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sowacustoms.config.AppConfig;

import java.util.Date;

@Service
public class RefreshTokenService {
    private final AppConfig appConfig;

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    public RefreshTokenService(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public String createToken(int userId) {
        PersistentToken token = new PersistentToken(userId, appConfig.getRememberMeExpirationMsec());
        persistentTokenRepository.save(token);

        return Jwts.builder().setSubject(String.valueOf(token.getId())).setIssuedAt(new Date(token.getIssuedAt()))
                .setExpiration(new Date(token.getExpiryDate())).claim("refresh", "true")
                .signWith(SignatureAlgorithm.HS512, appConfig.getTokenSecret()).compact();
    }

    public long getTokenIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(appConfig.getTokenSecret()).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean isRefreshToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(appConfig.getTokenSecret()).parseClaimsJws(token).getBody();
        return Boolean.parseBoolean((String) claims.get("refresh"));
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
