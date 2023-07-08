package pl.sowacustoms.security.authentication.localAuth;

public class AuthenticationRequest {
    private String username;
    private String password;
    private boolean persistent;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String username, String password, boolean persistent) {
        this.username = username;
        this.password = password;
        this.persistent = persistent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }
}
