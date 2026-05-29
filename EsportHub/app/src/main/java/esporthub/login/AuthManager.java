package esporthub.login;

import java.util.HashMap;
import java.util.Map;

public class AuthManager {
    private final Map<String, String> userDatabase = new HashMap<>();

    public AuthManager() {
        // Pre-registered accounts
        userDatabase.put("admin", "admin123");
    }

    /**
     * Authenticates a user based on username and password.
     */
    public boolean authenticate(String username, String password) {
        if (username == null || password == null) return false;
        String normalizedUser = username.trim().toLowerCase();
        
        // Check userDatabase first (e.g. admin or newly registered players)
        if (userDatabase.containsKey(normalizedUser)) {
            String storedPassword = userDatabase.get(normalizedUser);
            return storedPassword != null && storedPassword.equals(password);
        }

        // Check if the username belongs to any player in DataStore (default password: "player123")
        try {
            for (esporthub.model.Player p : esporthub.model.DataStore.getInstance().getPlayers()) {
                if (p.getName().trim().toLowerCase().equals(normalizedUser)) {
                    return "player123".equals(password);
                }
            }
        } catch (NoClassDefFoundError | Exception e) {
            // Fallback in case class is not found during bootstrap/testing
        }

        return false;
    }

    /**
     * Registers a new account. Returns true if successful, false if username already exists.
     */
    public boolean register(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty()) {
            return false;
        }
        String normalizedUser = username.trim().toLowerCase();
        if (exists(username)) {
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
        String normalizedUser = username.trim().toLowerCase();
        
        if (userDatabase.containsKey(normalizedUser)) {
            return true;
        }

        try {
            for (esporthub.model.Player p : esporthub.model.DataStore.getInstance().getPlayers()) {
                if (p.getName().trim().toLowerCase().equals(normalizedUser)) {
                    return true;
                }
            }
        } catch (NoClassDefFoundError | Exception e) {
            // Fallback
        }

        return false;
    }

    /**
     * Updates the username of an existing account, keeping the same password.
     */
    public boolean updateUsername(String oldUsername, String newUsername) {
        if (oldUsername == null || newUsername == null || newUsername.trim().isEmpty()) {
            return false;
        }
        String oldNormalized = oldUsername.trim().toLowerCase();
        String newNormalized = newUsername.trim().toLowerCase();

        boolean isMockPlayer = false;
        try {
            for (esporthub.model.Player p : esporthub.model.DataStore.getInstance().getPlayers()) {
                if (p.getName().trim().toLowerCase().equals(oldNormalized)) {
                    isMockPlayer = true;
                    break;
                }
            }
        } catch (NoClassDefFoundError | Exception e) {
            // Fallback
        }

        if (!userDatabase.containsKey(oldNormalized) && !isMockPlayer) {
            return false;
        }
        
        if (!oldNormalized.equals(newNormalized) && exists(newUsername)) {
            return false; // New username already exists
        }

        String password = userDatabase.remove(oldNormalized);
        if (password == null && isMockPlayer) {
            password = "player123";
        }

        if (password != null) {
            userDatabase.put(newNormalized, password);
        }
        return true;
    }
}
