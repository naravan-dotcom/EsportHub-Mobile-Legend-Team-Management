package esporthub.login;

import java.util.HashMap;
import java.util.Map;

public class AuthManager {
    private final Map<String, String> userDatabase = new HashMap<>();

    public AuthManager() {
        // Pre-registered accounts
        userDatabase.put("admin", "admin123");
        userDatabase.put("player", "player123");
    }

    /**
     * Authenticates a user based on username and password.
     */
    public boolean authenticate(String username, String password) {
        if (username == null || password == null) return false;
        String storedPassword = userDatabase.get(username.trim().toLowerCase());
        return storedPassword != null && storedPassword.equals(password);
    }

    /**
     * Registers a new account. Returns true if successful, false if username already exists.
     */
    public boolean register(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty()) {
            return false;
        }
        String normalizedUser = username.trim().toLowerCase();
        if (userDatabase.containsKey(normalizedUser)) {
            return false;
        }
        userDatabase.put(normalizedUser, password);
        return true;
    }

    /**
     * Checks if a username already exists.
     */
    public boolean exists(String username) {
        if (username == null) return false;
        return userDatabase.containsKey(username.trim().toLowerCase());
    }
}
