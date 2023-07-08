package pl.sowacustoms.panel.manager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface ManagerRepository extends JpaRepository<Manager, Integer> {

    Manager save(Manager manager);

    Manager findByName(String name);
}
