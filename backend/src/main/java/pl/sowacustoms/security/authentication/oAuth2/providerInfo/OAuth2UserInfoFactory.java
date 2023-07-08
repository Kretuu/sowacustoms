package pl.sowacustoms.security.authentication.oAuth2.providerInfo;

import pl.sowacustoms.exceptions.OAuth2AuthenticationProcessingException;
import pl.sowacustoms.security.authentication.oAuth2.providerInfo.GoogleOAuth2UserInfo;
import pl.sowacustoms.security.authentication.oAuth2.providerInfo.OAuth2UserInfo;
import pl.sowacustoms.user.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
