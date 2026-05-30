package esporthub.dashboard.Admin;


import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class AdminDashboard extends BorderPane {

    private final VBox sidebar;
    private final StackPane contentArea;

    private final Button scheduleNavBtn;
    private final Button teamPlayerNavBtn;
    private final Button logoutBtn;

    // Tabs
    private ScheduleTab scheduleTab;
    private TeamPlayerTab teamPlayerTab;

    public AdminDashboard(Runnable onLogout) {
        this.setPrefSize(1000, 680);
        this.setStyle("-fx-background-color: #F8FAFC;");

        // 1. Sidebar Navigation (Left - Deep Slate Navy style)
        sidebar = new VBox(20);
        sidebar.setPrefWidth(240);
        sidebar.setPadding(new Insets(30, 20, 30, 20));
        sidebar.setStyle("-fx-background-color: #0F172A; -fx-border-color: #1E293B; -fx-border-width: 0 1px 0 0;");

        // Title and branding
        Label logoLabel = new Label("EsportHub");
        logoLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 24));
        logoLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 24px;");

        Label roleLabel = new Label("DASHBOARD ADMIN");
        roleLabel.setFont(Font.font("Inter", FontWeight.BOLD, 10));
        roleLabel.setStyle("-fx-text-fill: #6366F1; -fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 10px; -fx-letter-spacing: 1.5px;");

        VBox brandBox = new VBox(4, logoLabel, roleLabel);
        brandBox.setPadding(new Insets(0, 0, 10, 0));

        // Horizontal separator line
        Pane separator = new Pane();
        separator.setPrefHeight(1);
        separator.setStyle("-fx-background-color: #1E293B;");

        // Nav Buttons
        scheduleNavBtn = new Button("Jadwal Tanding");
        setupNavButton(scheduleNavBtn);
        scheduleNavBtn.setOnAction(e -> showScheduleTab());

        teamPlayerNavBtn = new Button("Tim & Pemain");
        setupNavButton(teamPlayerNavBtn);
        teamPlayerNavBtn.setOnAction(e -> showTeamPlayerTab());

        // Push logout to the bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        logoutBtn = new Button("Logout");
        setupNavButton(logoutBtn);
        logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #F87171; -fx-alignment: center-left; -fx-padding: 12px 18px; -fx-background-radius: 8px; -fx-font-weight: bold;");
        logoutBtn.setOnAction(e -> onLogout.run());
        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle("-fx-background-color: rgba(239, 68, 68, 0.1); -fx-text-fill: #F87171; -fx-alignment: center-left; -fx-padding: 12px 18px; -fx-background-radius: 8px; -fx-font-weight: bold;"));
        logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #F87171; -fx-alignment: center-left; -fx-padding: 12px 18px; -fx-background-radius: 8px; -fx-font-weight: bold;"));

        sidebar.getChildren().addAll(brandBox, separator, scheduleNavBtn, teamPlayerNavBtn, spacer, logoutBtn);
        this.setLeft(sidebar);

        // 2. Content Workspace (Center)
        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: #F8FAFC; -fx-padding: 0;");
        this.setCenter(contentArea);

        // Show schedule tab by default
        showScheduleTab();
    }

    private void showScheduleTab() {
        if (scheduleTab == null) {
            scheduleTab = new ScheduleTab();
        }
        contentArea.getChildren().setAll(scheduleTab);
        
        // Update active nav button state
        setActiveNav(scheduleNavBtn);
    }

    private void showTeamPlayerTab() {
        if (teamPlayerTab == null) {
            teamPlayerTab = new TeamPlayerTab();
        }
        contentArea.getChildren().setAll(teamPlayerTab);

        setActiveNav(teamPlayerNavBtn);
    }

    private void setActiveNav(Button activeBtn) {
        String activeStyle = "-fx-background-color: #312E81; -fx-text-fill: #E0E7FF; -fx-font-weight: bold; -fx-alignment: center-left; -fx-padding: 12px 18px; -fx-background-radius: 8px;";
        String inactiveStyle = "-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-alignment: center-left; -fx-padding: 12px 18px; -fx-background-radius: 8px;";

        scheduleNavBtn.setStyle(scheduleNavBtn == activeBtn ? activeStyle : inactiveStyle);
        teamPlayerNavBtn.setStyle(teamPlayerNavBtn == activeBtn ? activeStyle : inactiveStyle);
    }

    private void setupNavButton(Button btn) {
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setFont(Font.font("Inter", 13));
        btn.setCursor(Cursor.HAND);
        
        // Inactive styling by default
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-alignment: center-left; -fx-padding: 12px 18px; -fx-background-radius: 8px;");

        // Hover highlight
        btn.setOnMouseEntered(e -> {
            if (!btn.getStyle().contains("#312E81")) {
                btn.setStyle("-fx-background-color: #1E293B; -fx-text-fill: #F1F5F9; -fx-alignment: center-left; -fx-padding: 12px 18px; -fx-background-radius: 8px;");
            }
        });
        btn.setOnMouseExited(e -> {
            if (!btn.getStyle().contains("#312E81")) {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #94A3B8; -fx-alignment: center-left; -fx-padding: 12px 18px; -fx-background-radius: 8px;");
            }
        });
    }
}

