package pl.sowacustoms.security.authorization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface PersistentTokenRepository extends JpaRepository<PersistentToken, Long> {
    Optional<PersistentToken> findPersistentTokenById(Long id);
}
