package pl.sowacustoms.panel.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

//@PreAuthorize("hasAuthority('ROLE_USER')")
public interface OrderRepository extends JpaRepository<Order, Integer> {

}
