package pl.sowacustoms.security.authentication.localAuth;

public class AuthenticationResponse {
    private final String jwt;
    private String refreshToken;

    public AuthenticationResponse(String jwt, String refreshToken) {
        this(jwt);
        this.refreshToken = refreshToken;
    }

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
