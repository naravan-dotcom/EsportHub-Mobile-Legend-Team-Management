package esporthub.dashboard.player;

import esporthub.login.AuthManager;
import esporthub.model.Player;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ProfileCard extends VBox {

    private final Player player;
    private final AuthManager authManager;
    private final Runnable onProfileUpdated;

    private final TextField usernameField;
    private final TextField emailField;
    private final ComboBox<String> roleCombo;
    private final Label idLabel;

    public ProfileCard(Player player, AuthManager authManager, Runnable onProfileUpdated) {
        this.player = player;
        this.authManager = authManager;
        this.onProfileUpdated = onProfileUpdated;

        this.setPadding(new Insets(24));
        this.setSpacing(16);
        this.setStyle("-fx-background-color: #FFFFFF; " +
                      "-fx-background-radius: 12px; " +
                      "-fx-border-color: #F3F4F6; " +
                      "-fx-border-width: 1px; " +
                      "-fx-border-radius: 12px; " +
                      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 4);");

        // Title
        Label titleLabel = new Label("Edit Profil");
        titleLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.web("#111827"));

        // Form elements
        VBox form = new VBox(14);
        
        // ID (Read-only)
        idLabel = new Label(player.getId());
        idLabel.setFont(Font.font("Inter", FontWeight.SEMI_BOLD, 14));
        idLabel.setTextFill(Color.web("#4B5563"));
        VBox idBox = createFieldContainer("Player ID (Read-only)", idLabel);
        idBox.setStyle("-fx-background-color: #F9FAFB; -fx-padding: 8px 12px; -fx-background-radius: 8px; -fx-border-color: #E5E7EB; -fx-border-width: 1px; -fx-border-radius: 8px;");

        // Username (Editable)
        usernameField = new TextField(player.getName());
        setupFieldStyle(usernameField, "Username");
        VBox usernameBox = createFieldContainer("Username", usernameField);

        // Email (Editable)
        emailField = new TextField(player.getEmail());
        setupFieldStyle(emailField, "Email");
        VBox emailBox = createFieldContainer("Email", emailField);

        // Role (Editable)
        roleCombo = new ComboBox<>();
        roleCombo.getItems().setAll("Jungler", "Gold Laner", "EXP Laner", "Mid Laner", "Roamer");
        roleCombo.setValue(player.getRole());
        roleCombo.setMaxWidth(Double.MAX_VALUE);
        setupComboStyle(roleCombo);
        VBox roleBox = createFieldContainer("Role / Posisi", roleCombo);

        form.getChildren().addAll(idBox, usernameBox, emailBox, roleBox);

        // Submit Button
        Button saveBtn = new Button("Simpan Perubahan");
        saveBtn.setMaxWidth(Double.MAX_VALUE);
        saveBtn.setCursor(Cursor.HAND);
        saveBtn.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        saveBtn.setStyle("-fx-background-color: linear-gradient(to right, #4F46E5, #6366F1); -fx-text-fill: #FFFFFF; -fx-background-radius: 8px; -fx-padding: 10px 16px;");
        
        // Hover effects
        saveBtn.setOnMouseEntered(e -> saveBtn.setStyle("-fx-background-color: linear-gradient(to right, #4338CA, #4F46E5); -fx-text-fill: #FFFFFF; -fx-background-radius: 8px; -fx-padding: 10px 16px;"));
        saveBtn.setOnMouseExited(e -> saveBtn.setStyle("-fx-background-color: linear-gradient(to right, #4F46E5, #6366F1); -fx-text-fill: #FFFFFF; -fx-background-radius: 8px; -fx-padding: 10px 16px;"));
        
        saveBtn.setOnAction(e -> handleSave());

        this.getChildren().addAll(titleLabel, form, saveBtn);
    }

    private VBox createFieldContainer(String labelText, javafx.scene.Node field) {
        Label label = new Label(labelText);
        label.setFont(Font.font("Inter", FontWeight.SEMI_BOLD, 12));
        label.setTextFill(Color.web("#4B5563"));
        VBox container = new VBox(6, label, field);
        return container;
    }

    private void setupFieldStyle(TextField field, String prompt) {
        field.setPromptText(prompt);
        field.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E5E7EB; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 10px 12px; -fx-font-size: 13px; -fx-text-fill: #111827;");
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #4F46E5; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 10px 12px; -fx-font-size: 13px; -fx-text-fill: #111827;");
            } else {
                field.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E5E7EB; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 10px 12px; -fx-font-size: 13px; -fx-text-fill: #111827;");
            }
        });
    }

    private void setupComboStyle(ComboBox<?> combo) {
        combo.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E5E7EB; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 4px 8px; -fx-font-size: 13px;");
    }

    private void handleSave() {
        String newName = usernameField.getText().trim();
        String newEmail = emailField.getText().trim();
        String newRole = roleCombo.getValue();

        if (newName.isEmpty() || newEmail.isEmpty() || newRole == null) {
            showAlert("Error", "Semua kolom editable harus diisi!", Alert.AlertType.WARNING);
            return;
        }

        String oldName = player.getName();

        // Check if username is changing and sync with AuthManager
        if (!oldName.equalsIgnoreCase(newName)) {
            boolean success = authManager.updateUsername(oldName, newName);
            if (!success) {
                showAlert("Error", "Username sudah digunakan atau tidak valid!", Alert.AlertType.ERROR);
                return;
            }
        }

        // Update player model
        player.setName(newName);
        player.setEmail(newEmail);
        player.setRole(newRole);

        showAlert("Sukses", "Profil berhasil diperbarui!", Alert.AlertType.INFORMATION);
        if (onProfileUpdated != null) {
            onProfileUpdated.run();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
