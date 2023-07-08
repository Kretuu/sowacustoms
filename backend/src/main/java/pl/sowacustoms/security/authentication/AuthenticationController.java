package pl.sowacustoms.security.authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import pl.sowacustoms.security.authentication.localAuth.AuthenticationRequest;
import pl.sowacustoms.security.authentication.localAuth.AuthenticationResponse;
import pl.sowacustoms.security.authorization.PersistentToken;
import pl.sowacustoms.security.authorization.PersistentTokenRepository;
import pl.sowacustoms.security.authorization.RefreshTokenService;
import pl.sowacustoms.security.authorization.TokenService;
import pl.sowacustoms.user.User;
import pl.sowacustoms.user.UserRepository;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RestController
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        System.out.println("Doing auth");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            System.out.println("Wrong pass");
            throw new Exception("Incorrect username or password", e);
        }

        System.out.println("Auth successful");
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = tokenService.createToken(userDetails);

        if(authenticationRequest.isPersistent()) {
            String jwtRefresh = "";
            final User user = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
            if(user != null) {
                jwtRefresh = refreshTokenService.createToken(user.getId());
                return ResponseEntity.ok(new AuthenticationResponse(jwt, jwtRefresh));
            }
        }

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> getNewJwtToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("PersistentToken");
        String token = null;
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) token = bearerToken.substring(7);

        if(token != null && refreshTokenService.validateToken(token) && refreshTokenService.isRefreshToken(token)) {
            long tokenId = refreshTokenService.getTokenIdFromToken(token);
            Optional<PersistentToken> optionalToken = persistentTokenRepository.findPersistentTokenById(tokenId);
            if(optionalToken.isPresent()) {
                int userId = optionalToken.get().getUserId();
                Optional<User> userOptional = userRepository.findById(userId);

                try {
                    UserDetails userDetails = userOptional.stream().map(user -> userDetailsService.loadUserByUsername(user.getEmail())).toList().get(0);
                    String jwt = tokenService.createToken(userDetails);
                    return ResponseEntity.ok(jwt);
                } catch (UsernameNotFoundException e) {
                    persistentTokenRepository.delete(optionalToken.get());
                    return ResponseEntity.status(403).build();
                }

            }
        }

        return ResponseEntity.status(403).build();
    }
}
