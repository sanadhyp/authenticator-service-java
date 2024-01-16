package entity;

import java.time.Instant;
import java.util.Objects;

public class Session {
    private String token;
    private Instant tokenCreationTime;

    public Session(String token) {
        this.token = token;
        this.tokenCreationTime = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session that = (Session) o;
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getTokenCreationTime() {
        return tokenCreationTime;
    }

    public void setTokenCreationTime(Instant tokenCreationTime) {
        this.tokenCreationTime = tokenCreationTime;
    }
}
