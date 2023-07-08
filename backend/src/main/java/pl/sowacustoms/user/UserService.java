package pl.sowacustoms.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.sowacustoms.exceptions.ResourceNotFoundException;
import pl.sowacustoms.security.authorization.UserPrincipal;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return UserPrincipal.create(user);
    }

    public UserDetails loadUserById(String id) {
        User user = userRepository.findById(Integer.parseInt(id)).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", id));

        return UserPrincipal.create(user);
    }
}
