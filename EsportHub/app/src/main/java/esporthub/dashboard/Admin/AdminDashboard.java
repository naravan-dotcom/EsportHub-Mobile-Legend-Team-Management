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
        this.setStyle("-fx-background-color: #FFFFFF;");

        // 1. Sidebar Navigation (Left)
        sidebar = new VBox(15);
        sidebar.setPrefWidth(220);
        sidebar.setPadding(new Insets(25, 15, 25, 15));
        sidebar.setStyle("-fx-background-color: #F3F4F6; -fx-border-color: #E5E7EB; -fx-border-width: 0 1px 0 0;");

        // Title and branding
        Label logoLabel = new Label("EsportHub");
        logoLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 22));
        logoLabel.setTextFill(Color.web("#4F46E5")); // Slate-Indigo theme primary

        Label roleLabel = new Label("Dashboard Admin");
        roleLabel.setFont(Font.font("Inter", FontWeight.SEMI_BOLD, 12));
        roleLabel.setTextFill(Color.web("#6B7280"));

        VBox brandBox = new VBox(2, logoLabel, roleLabel);
        brandBox.setPadding(new Insets(0, 0, 15, 0));

        // Horizontal separator line
        Pane separator = new Pane();
        separator.setPrefHeight(1);
        separator.setStyle("-fx-background-color: #E5E7EB;");

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
        logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #EF4444; -fx-font-weight: bold; -fx-alignment: center-left; -fx-padding: 10px 15px;");
        logoutBtn.setOnAction(e -> onLogout.run());

        sidebar.getChildren().addAll(brandBox, separator, scheduleNavBtn, teamPlayerNavBtn, spacer, logoutBtn);
        this.setLeft(sidebar);

        // 2. Content Workspace (Center)
        contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: #FFFFFF;");
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
        String activeStyle = "-fx-background-color: #E0E7FF; -fx-text-fill: #4F46E5; -fx-font-weight: bold; -fx-alignment: center-left; -fx-padding: 10px 15px; -fx-background-radius: 6px;";
        String inactiveStyle = "-fx-background-color: transparent; -fx-text-fill: #4B5563; -fx-alignment: center-left; -fx-padding: 10px 15px; -fx-background-radius: 6px;";

        scheduleNavBtn.setStyle(scheduleNavBtn == activeBtn ? activeStyle : inactiveStyle);
        teamPlayerNavBtn.setStyle(teamPlayerNavBtn == activeBtn ? activeStyle : inactiveStyle);
    }

    private void setupNavButton(Button btn) {
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setFont(Font.font("Inter", 13));
        btn.setCursor(Cursor.HAND);
        
        // Inactive styling by default
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #4B5563; -fx-alignment: center-left; -fx-padding: 10px 15px; -fx-background-radius: 6px;");

        // Hover highlight
        btn.setOnMouseEntered(e -> {
            if (!btn.getStyle().contains("#E0E7FF")) {
                btn.setStyle("-fx-background-color: #F3F4F6; -fx-text-fill: #1F2937; -fx-alignment: center-left; -fx-padding: 10px 15px; -fx-background-radius: 6px;");
            }
        });
        btn.setOnMouseExited(e -> {
            if (!btn.getStyle().contains("#E0E7FF")) {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #4B5563; -fx-alignment: center-left; -fx-padding: 10px 15px; -fx-background-radius: 6px;");
            }
        });
    }
}
