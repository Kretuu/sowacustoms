package pl.sowacustoms.panel.manager;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Objects;

@Entity
public class Manager {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private @Id @GeneratedValue Long id;

    private String name;

    private @JsonIgnore String password;

    private String roles;

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }


    protected Manager() {}

    public Manager(String name, String password, String... roles) {
        this.name = name;
        this.password = password;
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(roles).forEach(role -> stringBuilder.append(role).append(","));
        this.roles = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manager manager = (Manager) o;
        return Objects.equals(id, manager.id) &&
                Objects.equals(name, manager.name) &&
                Objects.equals(password, manager.password) &&
                Objects.equals(roles, manager.roles);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, password, roles);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public String[] getRoles() {
        return roles.split(",");
    }

    public void setRoles(String[] roles) {
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(roles).forEach(role -> stringBuilder.append(role).append(","));
        this.roles = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", roles=" + roles +
                '}';
    }
}
