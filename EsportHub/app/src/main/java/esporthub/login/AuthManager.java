package esporthub.login;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AuthManager {
    private final Map<String, String> userDatabase = new HashMap<>();
    private static final String FILE_PATH = "app/src/main/java/esporthub/login/user_credentials.txt";

    public AuthManager() {
        loadDatabase();
    }

    private void loadDatabase() {
        // Pre-registered accounts and mock players default passwords
        userDatabase.put("admin", "admin123");
        userDatabase.put("sutsujin", "player123");
        userDatabase.put("skylar", "player123");
        userDatabase.put("dyrennn", "player123");
        userDatabase.put("idok", "player123");
        userDatabase.put("rinz", "player123");
        userDatabase.put("kairi", "player123");
        userDatabase.put("sanz", "player123");
        userDatabase.put("cw", "player123");
        userDatabase.put("kiboy", "player123");
        userDatabase.put("rezz", "player123");
        userDatabase.put("anavel", "player123");
        userDatabase.put("branz", "player123");
        userDatabase.put("claw", "player123");
        userDatabase.put("dreams", "player123");
        userDatabase.put("fluffy", "player123");
        userDatabase.put("tazz", "player123");
        userDatabase.put("nino", "player123");
        userDatabase.put("cr1mpa", "player123");
        userDatabase.put("rasy", "player123");
        userDatabase.put("pai", "player123");

        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) continue;
                    int colonIdx = line.indexOf(':');
                    if (colonIdx > 0) {
                        String username = line.substring(0, colonIdx).trim().toLowerCase();
                        String password = line.substring(colonIdx + 1);
                        userDatabase.put(username, password);
                    }
                }
            } catch (Exception e) {
                System.err.println("Gagal membaca database login: " + e.getMessage());
            }
        } else {
            saveDatabase();
        }
    }

    private void saveDatabase() {
        File file = new File(FILE_PATH);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("# Database Login EsportHub");
            for (Map.Entry<String, String> entry : userDatabase.entrySet()) {
                writer.println(entry.getKey() + ":" + entry.getValue());
            }
        } catch (Exception e) {
            System.err.println("Gagal menyimpan database login: " + e.getMessage());
        }
    }

    /**
     * Authenticates a user based on username and password.
     */
    public boolean authenticate(String username, String password) {
        if (username == null || password == null) return false;
        String normalizedUser = username.trim().toLowerCase();
        
        if (userDatabase.containsKey(normalizedUser)) {
            String storedPassword = userDatabase.get(normalizedUser);
            return storedPassword != null && storedPassword.equals(password);
        }

        // Fallback check if the username belongs to any player in DataStore (default password: "player123")
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
        saveDatabase();
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
        saveDatabase();
        return true;
    }

    /**
     * Updates the password of an existing account.
     */
    public boolean updatePassword(String username, String newPassword) {
        if (username == null || newPassword == null || newPassword.trim().isEmpty()) {
            return false;
        }
        String normalizedUser = username.trim().toLowerCase();
        if (!exists(username)) {
            return false;
        }
        userDatabase.put(normalizedUser, newPassword);
        saveDatabase();
        return true;
    }

    /**
     * Retrieves the password of an existing account.
     */
    public String getPassword(String username) {
        if (username == null) return null;
        String normalizedUser = username.trim().toLowerCase();
        if (userDatabase.containsKey(normalizedUser)) {
            return userDatabase.get(normalizedUser);
        }
        // Fallback for mock players who are not in the loaded database yet
        try {
            for (esporthub.model.Player p : esporthub.model.DataStore.getInstance().getPlayers()) {
                if (p.getName().trim().toLowerCase().equals(normalizedUser)) {
                    return "player123";
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }
}
