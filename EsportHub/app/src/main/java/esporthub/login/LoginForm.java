package esporthub.login;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.function.BiConsumer;

public class LoginForm extends VBox {

    private final TextField usernameField;
    private final PasswordField passwordField;
    private final TextField visiblePasswordField;
    private final CheckBox showPassCheck;
    private final VBox alertContainer;
    private final Button loginBtn;

    public LoginForm(Runnable onRegisterLinkClick, BiConsumer<String, String> onLoginSubmit) {
        // Form structure configuration
        this.setAlignment(Pos.CENTER);
        this.setSpacing(22);
        this.setStyle("-fx-background-color: #FFFFFF;");
        this.setPadding(new Insets(45, 75, 45, 75));

        // Header Section
        VBox header = new VBox(6);
        header.setAlignment(Pos.CENTER);
        
        Label title = new Label("Masuk ke Akun");
        title.setTextFill(Color.web("#1c0f38"));
        title.setFont(Font.font("Outfit", FontWeight.BOLD, 28));

        Label subtitle = new Label("Silakan masuk dengan akun Anda");
        subtitle.setTextFill(Color.web("#8c92a6"));
        subtitle.setFont(Font.font("Inter", 13));

        header.getChildren().addAll(title, subtitle);

        // Fields and Alerts Section
        VBox fields = new VBox(15);
        alertContainer = new VBox();
        alertContainer.setAlignment(Pos.CENTER);

        // Username Field
        VBox usernameBox = new VBox(6);
        Label userLabel = new Label("Username / ID");
        userLabel.setTextFill(Color.web("#313b5e"));
        userLabel.setFont(Font.font("Inter", FontWeight.BOLD, 13));
        
        usernameField = new TextField();
        usernameField.setPromptText("Masukkan Username");
        setupFieldStyling(usernameField);
        usernameBox.getChildren().addAll(userLabel, usernameField);

        // Password Field
        VBox passwordBox = new VBox(6);
        Label passLabel = new Label("Password");
        passLabel.setTextFill(Color.web("#313b5e"));
        passLabel.setFont(Font.font("Inter", FontWeight.BOLD, 13));

        StackPane passwordStack = new StackPane();
        passwordField = new PasswordField();
        passwordField.setPromptText("Masukkan Password");
        setupFieldStyling(passwordField);

        visiblePasswordField = new TextField();
        visiblePasswordField.setPromptText("Masukkan Password");
        setupFieldStyling(visiblePasswordField);
        visiblePasswordField.setVisible(false);
        visiblePasswordField.setManaged(false);

        // Keep values in sync
        visiblePasswordField.textProperty().bindBidirectional(passwordField.textProperty());
        passwordStack.getChildren().addAll(passwordField, visiblePasswordField);

        // Password visibility toggle
        showPassCheck = new CheckBox("Tampilkan password");
        showPassCheck.setTextFill(Color.web("#8c92a6"));
        showPassCheck.setFont(Font.font("Inter", 12));
        showPassCheck.setCursor(Cursor.HAND);
        showPassCheck.setOnAction(e -> {
            boolean show = showPassCheck.isSelected();
            passwordField.setVisible(!show);
            passwordField.setManaged(!show);
            visiblePasswordField.setVisible(show);
            visiblePasswordField.setManaged(show);
        });

        passwordBox.getChildren().addAll(passLabel, passwordStack, showPassCheck);
        fields.getChildren().addAll(alertContainer, usernameBox, passwordBox);

        // Login Button
        loginBtn = new Button("Login");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setCursor(Cursor.HAND);
        setupButtonStyling(loginBtn);
        
        loginBtn.setOnAction(e -> {
            alertContainer.getChildren().clear();
            String u = usernameField.getText().trim();
            String p = passwordField.getText();
            onLoginSubmit.accept(u, p);
        });

        // Toggle to Register View
        HBox toggleContainer = new HBox(5);
        toggleContainer.setAlignment(Pos.CENTER);

        Label noAccountLabel = new Label("Belum punya akun?");
        noAccountLabel.setTextFill(Color.web("#8c92a6"));
        noAccountLabel.setFont(Font.font("Inter", 12.5));

        Hyperlink registerLink = new Hyperlink("Daftar Sekarang");
        registerLink.setTextFill(Color.web("#7c4ded"));
        registerLink.setFont(Font.font("Inter", FontWeight.BOLD, 12.5));
        registerLink.setUnderline(true);
        registerLink.setStyle("-fx-border-color: transparent; -fx-padding: 0;");
        registerLink.setOnAction(e -> {
            alertContainer.getChildren().clear();
            onRegisterLinkClick.run();
        });

        toggleContainer.getChildren().addAll(noAccountLabel, registerLink);

        this.getChildren().addAll(header, fields, loginBtn, toggleContainer);
    }

    /**
     * Shows a colored alert notification directly inside the form.
     */
    public void showAlert(String message, boolean isSuccess) {
        alertContainer.getChildren().clear();

        HBox alertBox = new HBox(10);
        alertBox.setAlignment(Pos.CENTER_LEFT);
        alertBox.setPadding(new Insets(10, 15, 10, 15));
        
        String bg = isSuccess ? "#ECFDF5" : "#FEF2F2";
        String border = isSuccess ? "#10B981" : "#EF4444";
        String textCol = isSuccess ? "#065F46" : "#991B1B";
        
        alertBox.setStyle(
            "-fx-background-color: " + bg + ";" +
            "-fx-border-color: " + border + ";" +
            "-fx-border-radius: 6px;" +
            "-fx-background-radius: 6px;" +
            "-fx-border-width: 1px;"
        );

        Label label = new Label(message);
        label.setTextFill(Color.web(textCol));
        label.setFont(Font.font("Inter", FontWeight.MEDIUM, 13));
        label.setWrapText(true);
        HBox.setHgrow(label, Priority.ALWAYS);

        Button closeBtn = new Button("✕");
        closeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: " + textCol + "; -fx-font-weight: bold; -fx-padding: 0;");
        closeBtn.setCursor(Cursor.HAND);
        closeBtn.setOnAction(e -> {
            FadeTransition ft = new FadeTransition(Duration.millis(150), alertBox);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setOnFinished(evt -> alertContainer.getChildren().clear());
            ft.play();
        });

        alertBox.getChildren().addAll(label, closeBtn);
        
        alertBox.setOpacity(0.0);
        alertContainer.getChildren().add(alertBox);
        FadeTransition ft = new FadeTransition(Duration.millis(250), alertBox);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    /**
     * Clears all fields.
     */
    public void clearInputs() {
        usernameField.clear();
        passwordField.clear();
        showPassCheck.setSelected(false);
        passwordField.setVisible(true);
        passwordField.setManaged(true);
        visiblePasswordField.setVisible(false);
        visiblePasswordField.setManaged(false);
    }

    private void setupFieldStyling(TextField field) {
        String baseStyle = 
            "-fx-background-color: #f4f5f8;" +
            "-fx-background-radius: 8px;" +
            "-fx-border-color: transparent;" +
            "-fx-border-radius: 8px;" +
            "-fx-border-width: 1px;" +
            "-fx-padding: 12px 16px;" +
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #1f2937;" +
            "-fx-prompt-text-fill: #9ca3af;";
        
        String focusStyle = 
            "-fx-background-color: #ffffff;" +
            "-fx-background-radius: 8px;" +
            "-fx-border-color: #7c4ded;" +
            "-fx-border-radius: 8px;" +
            "-fx-border-width: 1px;" +
            "-fx-padding: 12px 16px;" +
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #1f2937;" +
            "-fx-prompt-text-fill: #9ca3af;";

        field.setStyle(baseStyle);

        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(focusStyle);
            } else {
                field.setStyle(baseStyle);
            }
        });
    }

    private void setupButtonStyling(Button btn) {
        String idleStyle = 
            "-fx-background-color: #7c4ded;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8px;" +
            "-fx-padding: 12px 20px;";

        String hoverStyle = 
            "-fx-background-color: #6333e8;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8px;" +
            "-fx-padding: 12px 20px;";

        btn.setStyle(idleStyle);

        btn.setOnMouseEntered(e -> {
            btn.setStyle(hoverStyle);
            btn.setScaleX(1.01);
            btn.setScaleY(1.01);
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle(idleStyle);
            btn.setScaleX(1.0);
            btn.setScaleY(1.0);
        });
    }
}
