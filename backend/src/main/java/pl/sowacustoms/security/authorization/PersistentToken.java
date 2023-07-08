package pl.sowacustoms.security.authorization;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.rest.core.annotation.RestResource;
import pl.sowacustoms.config.AppConfig;

import java.util.Date;

@Entity
@Table(name = "persistent_tokens")
public class PersistentToken {
    private @Id @GeneratedValue long id;
    private int userId;
    private long issuedAt;
    private long expiryDate;

    protected PersistentToken() {}

    public PersistentToken(int userId, long expiryTime) {
        Date now = new Date();
        this.issuedAt = now.getTime();
        this.expiryDate = now.getTime() + expiryTime;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(long issuedAt) {
        this.issuedAt = issuedAt;
    }

    public long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(long expiryDate) {
        this.expiryDate = expiryDate;
    }
}
