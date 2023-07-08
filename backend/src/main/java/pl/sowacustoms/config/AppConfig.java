package pl.sowacustoms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.ArrayList;
import java.util.List;


@EnableAsync
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private List<String> authorizedRedirectUris = new ArrayList<>();

    private String tokenSecret;

    private long rememberMeExpirationMsec;
    private long tokenExpirationMsec;

    public AppConfig() {
    }

    public long getRememberMeExpirationMsec() {
        return rememberMeExpirationMsec;
    }

    public void setRememberMeExpirationMsec(long rememberMeExpirationMsec) {
        this.rememberMeExpirationMsec = rememberMeExpirationMsec;
    }

    public List<String> getAuthorizedRedirectUris() {
        return authorizedRedirectUris;
    }

    public void setAuthorizedRedirectUris(List<String> authorizedRedirectUris) {
        this.authorizedRedirectUris = authorizedRedirectUris;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public long getTokenExpirationMsec() {
        return tokenExpirationMsec;
    }

    public void setTokenExpirationMsec(long tokenExpirationMsec) {
        this.tokenExpirationMsec = tokenExpirationMsec;
    }
}
