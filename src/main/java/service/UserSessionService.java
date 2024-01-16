package service;

import entity.Role;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static entity.ApplicationConfiguration.TOKEN_TTL_SECONDS;

public class UserSessionService {
    private int tokenTTL;
    private Map<String, String> sessions = new HashMap<>();
    private Map<String, Instant> tokens = new HashMap<>();

    public UserSessionService() {
        this.tokenTTL = TOKEN_TTL_SECONDS;
    }

    public UserSessionService(int tokenTTLseconds) {
        this.tokenTTL = tokenTTLseconds;
    }

    public String begin(String userId) throws IllegalStateException {
        if (sessions.containsKey(userId)) {
            return validateToken(sessions.get(userId));
        } else {
            String aToken = generateToken();
            sessions.put(userId, aToken);
            return aToken;
        }
    }

    public String validate(String userId) throws IllegalStateException {
        if (sessions.containsKey(userId)) {
            return validateToken(sessions.get(userId));
        } else {
            throw new IllegalStateException("Session expired. Login again.");
        }
    }

    public void inValidate(String token) throws IllegalStateException {
        if (validateToken(token).equals(token)) {
            tokens.remove(token);
        }
    }

    public String validateToken(String aToken) throws IllegalStateException {
        if (!tokens.containsKey(aToken)) {
            throw new IllegalStateException("Token is not valid.");
        }
        if (tokens.get(aToken).plusSeconds(tokenTTL).isAfter(Instant.now())) {
            return aToken;
        } else {
            throw new IllegalStateException("Token expired, login again");
        }

    }

    public String fetchUserId(String token) throws IllegalStateException {
        validateToken(token);
        var users = this.sessions.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), token))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return users.size() == 0 ? "" : users.get(0);
    }

    private String generateToken() {
        String aToken = UUID.randomUUID().toString();
        tokens.put(aToken, Instant.now());
        return aToken;
    }
}
