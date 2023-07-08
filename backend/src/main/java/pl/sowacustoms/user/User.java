package pl.sowacustoms.user;

import jakarta.persistence.*;
import net.minidev.json.annotate.JsonIgnore;

@Entity
public class User {
    private @Id
    @GeneratedValue int id;
    private String email;
    private String userName;
    private String name;
    private @JsonIgnore String password;
    private boolean active;
    private String providerId;
    private String roles;
    private @Enumerated(EnumType.STRING) AuthProvider provider;
    public User() {
    }
    public User(int id, String email, String userName, String password, boolean active, String roles) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.active = active;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public AuthProvider getProvider() {
        return provider;
    }

    public void setProvider(AuthProvider provider) {
        this.provider = provider;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
