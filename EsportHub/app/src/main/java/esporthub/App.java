package esporthub;

import esporthub.login.*;
import esporthub.dashboard.Admin.*;
import esporthub.dashboard.player.PlayerDashboard;
import esporthub.model.*;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {

    // Subtitles for Left Panel
    private static final String LOGIN_SUBTITLE = "Platform Manajemen\nTurnamen Esports #1";
    private static final String REGISTER_SUBTITLE = "Bergabung & Jadilah\nBagian dari Komunitas";

    // Application window dimensions (Login/Register)
    private static final int WIDTH = 980;
    private static final int HEIGHT = 620;

    private StackPane rootContainer;
    private HBox authLayout;
    
    // Modular UI Components
    private AuthManager authManager;
    private LeftPanel leftPanel;
    private StackPane rightPanelContainer;
    private LoginForm loginForm;
    private RegisterForm registerForm;

    private Stage mainStage;

    @Override
    public void start(Stage primaryStage) {
        this.mainStage = primaryStage;
        authManager = new AuthManager();

        // The root container is bright and white
        rootContainer = new StackPane();
        rootContainer.setStyle("-fx-background-color: #FFFFFF;");

        // The main layout splits into left purple and right white panels
        authLayout = new HBox();
        authLayout.setPrefSize(WIDTH, HEIGHT);
        authLayout.setMaxSize(WIDTH, HEIGHT);
        authLayout.setStyle("-fx-background-color: #FFFFFF;");

        // Instantiate modular panels
        leftPanel = new LeftPanel();
        rightPanelContainer = new StackPane();
        rightPanelContainer.setPrefWidth(WIDTH - 420);
        rightPanelContainer.setStyle("-fx-background-color: #FFFFFF;");

        // Initialize Login and Register forms with callbacks
        setupForms();

        rightPanelContainer.getChildren().addAll(loginForm, registerForm);
        authLayout.getChildren().addAll(leftPanel, rightPanelContainer);
        rootContainer.getChildren().add(authLayout);

        // Scene is set exactly to the size of the cards for a bright, borderless appearance
        Scene scene = new Scene(rootContainer, WIDTH, HEIGHT);

        primaryStage.setTitle("EsportHub - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Initializes the LoginForm and RegisterForm with callback handlers.
     */
    private void setupForms() {
        // LoginForm Callbacks
        loginForm = new LoginForm(
            // When register link is clicked
            () -> transitionForms(loginForm, registerForm, REGISTER_SUBTITLE, LeftPanel.TROPHY_SVG, true),
            // When login form is submitted
            (username, password) -> {
                if (username.isEmpty() || password.isEmpty()) {
                    loginForm.showAlert("Harap isi semua kolom!", false);
                    return;
                }

                if (authManager.authenticate(username, password)) {
                    if (username.equalsIgnoreCase("admin")) {
                        loginForm.showAlert("Login Berhasil! Mengalihkan...", true);
                        
                        // Instantly swap scene to Admin Dashboard without animation
                        showAdminDashboard();
                    } else {
                        loginForm.showAlert("Login Berhasil sebagai Player! Mengalihkan...", true);
                        
                        // Redirect to Player Dashboard after 1 second
                        javafx.animation.Timeline delay = new javafx.animation.Timeline(
                            new javafx.animation.KeyFrame(Duration.seconds(1.0), evt -> {
                                showPlayerDashboard(username);
                            })
                        );
                        delay.play();
                    }
                } else {
                    loginForm.showAlert("Username atau Password salah!", false);
                }
            }
        );

        // RegisterForm Callbacks
        registerForm = new RegisterForm(
            // When login link is clicked
            () -> transitionForms(registerForm, loginForm, LOGIN_SUBTITLE, LeftPanel.GAMEPAD_SVG, false),
            // When register form is submitted
            (username, password) -> {
                if (username.isEmpty() || password.isEmpty()) {
                    registerForm.showAlert("Harap isi semua kolom!", false);
                    return;
                }

                if (authManager.exists(username)) {
                    registerForm.showAlert("Username sudah terdaftar!", false);
                } else {
                    authManager.register(username, password);
                    registerForm.showAlert("Akun Berhasil Dibuat! Mengalihkan ke Login...", true);
                    
                    // Auto transition back to login screen after 1.5 seconds
                    javafx.animation.Timeline delay = new javafx.animation.Timeline(
                        new javafx.animation.KeyFrame(Duration.seconds(1.5), evt -> {
                            transitionForms(registerForm, loginForm, LOGIN_SUBTITLE, LeftPanel.GAMEPAD_SVG, false);
                        })
                    );
                    delay.play();
                }
            }
        );

        // Start with RegisterForm hidden
        registerForm.setVisible(false);
        registerForm.setManaged(false);
    }

    /**
     * Transitions between Login and Register views with cross-fading and slide-up actions.
     */
    private void transitionForms(Pane currentPane, Pane nextPane, String targetSubtitle, String svgPath, boolean isTrophy) {
        // Fade out current form and left content
        FadeTransition fadeOutCurrent = new FadeTransition(Duration.millis(200), currentPane);
        fadeOutCurrent.setFromValue(1.0);
        fadeOutCurrent.setToValue(0.0);

        FadeTransition fadeOutLeft = new FadeTransition(Duration.millis(200), leftPanel.getContentContainer());
        fadeOutLeft.setFromValue(1.0);
        fadeOutLeft.setToValue(0.0);

        ParallelTransition fadeOutAll = new ParallelTransition(fadeOutCurrent, fadeOutLeft);
        fadeOutAll.setOnFinished(e -> {
            // Update window title dynamically
            mainStage.setTitle(isTrophy ? "EsportHub - Daftar Akun" : "EsportHub - Login");

            currentPane.setVisible(false);
            currentPane.setManaged(false);

            nextPane.setVisible(true);
            nextPane.setManaged(true);
            nextPane.setOpacity(0.0);

            // Update Left Panel contents
            leftPanel.updateContent(targetSubtitle, svgPath, isTrophy);

            // Fade in next form and left content
            FadeTransition fadeInNext = new FadeTransition(Duration.millis(250), nextPane);
            fadeInNext.setFromValue(0.0);
            fadeInNext.setToValue(1.0);

            FadeTransition fadeInLeft = new FadeTransition(Duration.millis(250), leftPanel.getContentContainer());
            fadeInLeft.setFromValue(0.0);
            fadeInLeft.setToValue(1.0);

            // Slide-up transition
            TranslateTransition slideIn = new TranslateTransition(Duration.millis(250), nextPane);
            slideIn.setFromY(10);
            slideIn.setToY(0);

            ParallelTransition fadeInAll = new ParallelTransition(fadeInNext, fadeInLeft, slideIn);
            fadeInAll.play();
        });

        fadeOutAll.play();
    }

    /**
     * Instantly shows the Admin Dashboard and resizes stage.
     */
    private void showAdminDashboard() {
        loginForm.clearInputs();
        registerForm.clearInputs();

        AdminDashboard dashboard = new AdminDashboard(() -> {
            // Logout callback - transition back to Login
            Scene loginScene = new Scene(rootContainer, WIDTH, HEIGHT);
            mainStage.setTitle("EsportHub - Login");
            mainStage.setScene(loginScene);
            mainStage.centerOnScreen();
        });

        Scene dashScene = new Scene(dashboard, 1000, 680);
        mainStage.setTitle("EsportHub - Dashboard Admin");
        mainStage.setScene(dashScene);
        mainStage.centerOnScreen();
    }

    /**
     * Instantly shows the Player Dashboard and resizes stage.
     */
    private void showPlayerDashboard(String username) {
        loginForm.clearInputs();
        registerForm.clearInputs();

        // Find or dynamically create Player in DataStore
        DataStore dataStore = DataStore.getInstance();
        Player loggedInPlayer = null;
        for (Player p : dataStore.getPlayers()) {
            if (p.getName().equalsIgnoreCase(username)) {
                loggedInPlayer = p;
                break;
            }
        }

        if (loggedInPlayer == null) {
            // New user registered, create default player profile
            String id = "P" + (dataStore.getPlayers().size() + 1);
            loggedInPlayer = new Player(id, username, "Roamer", "None", false);
            dataStore.addPlayer(loggedInPlayer);
        }

        Player finalPlayer = loggedInPlayer;

        PlayerDashboard dashboard = new PlayerDashboard(finalPlayer, authManager, () -> {
            // Logout callback - transition back to Login
            Scene loginScene = new Scene(rootContainer, WIDTH, HEIGHT);
            mainStage.setTitle("EsportHub - Login");
            mainStage.setScene(loginScene);
            mainStage.centerOnScreen();
        });

        Scene dashScene = new Scene(dashboard, 1000, 680);
        mainStage.setTitle("EsportHub - Dashboard Player (" + finalPlayer.getName() + ")");
        mainStage.setScene(dashScene);
        mainStage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
