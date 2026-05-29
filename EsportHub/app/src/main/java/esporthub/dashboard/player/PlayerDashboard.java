package esporthub.dashboard.player;

import esporthub.login.AuthManager;
import esporthub.model.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PlayerDashboard extends BorderPane {

    private final Player player;
    private final AuthManager authManager;
    private final Runnable onLogout;

    private final VBox sidebar;
    private final ScrollPane contentScrollPane;
    
    // Components
    private ProfileCard profileCard;
    private StatsCard statsCard;
    private KdaChartCard kdaChartCard;
    private MatchHistoryCard matchHistoryCard;
    
    // Sidebar Header Info
    private Label sidebarPlayerName;
    private Label sidebarPlayerRole;

    public PlayerDashboard(Player player, AuthManager authManager, Runnable onLogout) {
        this.player = player;
        this.authManager = authManager;
        this.onLogout = onLogout;

        this.setPrefSize(1000, 680);
        this.setStyle("-fx-background-color: #F9FAFB;"); // Soft background color

        // 1. Sidebar Navigation (Left)
        sidebar = new VBox(15);
        sidebar.setPrefWidth(220);
        sidebar.setPadding(new Insets(25, 15, 25, 15));
        sidebar.setStyle("-fx-background-color: #F3F4F6; -fx-border-color: #E5E7EB; -fx-border-width: 0 1px 0 0;");

        // Branding
        Label logoLabel = new Label("EsportHub");
        logoLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 22));
        logoLabel.setTextFill(Color.web("#4F46E5")); // Indigo

        Label roleLabel = new Label("Dashboard Player");
        roleLabel.setFont(Font.font("Inter", FontWeight.SEMI_BOLD, 11));
        roleLabel.setTextFill(Color.web("#6B7280"));

        VBox brandBox = new VBox(2, logoLabel, roleLabel);
        brandBox.setPadding(new Insets(0, 0, 10, 0));

        // Separator
        Pane separator = new Pane();
        separator.setPrefHeight(1);
        separator.setStyle("-fx-background-color: #E5E7EB;");

        // Player Info Box in Sidebar
        VBox playerInfoBox = new VBox(4);
        playerInfoBox.setAlignment(Pos.CENTER);
        playerInfoBox.setPadding(new Insets(15, 10, 15, 10));
        playerInfoBox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 8px; -fx-border-color: #E5E7EB; -fx-border-width: 1px; -fx-border-radius: 8px;");
        
        // Circular Avatar Placeholder
        StackPane avatar = new StackPane();
        avatar.setPrefSize(50, 50);
        avatar.setMaxSize(50, 50);
        avatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #4F46E5, #818CF8); -fx-background-radius: 25px;");
        Label avatarLetter = new Label(player.getName().substring(0, 1).toUpperCase());
        avatarLetter.setFont(Font.font("Outfit", FontWeight.BOLD, 20));
        avatarLetter.setTextFill(Color.WHITE);
        avatar.getChildren().add(avatarLetter);

        sidebarPlayerName = new Label(player.getName());
        sidebarPlayerName.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        sidebarPlayerName.setTextFill(Color.web("#111827"));

        sidebarPlayerRole = new Label(player.getRole());
        sidebarPlayerRole.setFont(Font.font("Inter", 11));
        sidebarPlayerRole.setTextFill(Color.web("#4F46E5"));

        playerInfoBox.getChildren().addAll(avatar, sidebarPlayerName, sidebarPlayerRole);

        // Sidebar Navigation Button
        Button menuBtn = new Button("Profil & Statistik");
        menuBtn.setMaxWidth(Double.MAX_VALUE);
        menuBtn.setFont(Font.font("Inter", FontWeight.BOLD, 13));
        menuBtn.setStyle("-fx-background-color: #E0E7FF; -fx-text-fill: #4F46E5; -fx-alignment: center-left; -fx-padding: 10px 15px; -fx-background-radius: 6px;");

        // Logout Button
        Button logoutBtn = new Button("Keluar Aplikasi");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setFont(Font.font("Inter", FontWeight.BOLD, 13));
        logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #EF4444; -fx-alignment: center-left; -fx-padding: 10px 15px; -fx-background-radius: 6px;");
        logoutBtn.setCursor(Cursor.HAND);
        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #EF4444; -fx-alignment: center-left; -fx-padding: 10px 15px; -fx-background-radius: 6px;"));
        logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #EF4444; -fx-alignment: center-left; -fx-padding: 10px 15px; -fx-background-radius: 6px;"));
        logoutBtn.setOnAction(e -> onLogout.run());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sidebar.getChildren().addAll(brandBox, separator, playerInfoBox, menuBtn, spacer, logoutBtn);
        this.setLeft(sidebar);

        // 2. Scrollable Content Area (Center)
        contentScrollPane = new ScrollPane();
        contentScrollPane.setFitToWidth(true);
        contentScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        contentScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        contentScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        this.setCenter(contentScrollPane);

        setupContentWorkspace();
    }

    private void setupContentWorkspace() {
        VBox workspace = new VBox(24);
        workspace.setPadding(new Insets(24));
        workspace.setStyle("-fx-background-color: transparent;");

        // Welcome banner/title
        Label welcomeTitle = new Label("Selamat Datang, " + player.getName() + "!");
        welcomeTitle.setFont(Font.font("Outfit", FontWeight.BOLD, 24));
        welcomeTitle.setTextFill(Color.web("#111827"));
        
        Label welcomeSubtitle = new Label("Kelola informasi profil kamu dan pantau performa pertandingan di sini.");
        welcomeSubtitle.setFont(Font.font("Inter", 13));
        welcomeSubtitle.setTextFill(Color.web("#6B7280"));

        VBox titleContainer = new VBox(4, welcomeTitle, welcomeSubtitle);

        // 1. Stats Card (Rank, Winrate, KDA) - Spans horizontally
        statsCard = new StatsCard(player);

        // 2. Bottom Grid layout (Profile Form on left, Graph and Match History on right)
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);

        // Column Constraints
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(350);
        col1.setMinWidth(350);
        col1.setMaxWidth(350);
        col1.setHgrow(Priority.NEVER);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(col1, col2);

        // Components creation
        profileCard = new ProfileCard(player, authManager, this::refreshProfileViews);
        kdaChartCard = new KdaChartCard(player);
        matchHistoryCard = new MatchHistoryCard(player);

        VBox rightSide = new VBox(20);
        rightSide.getChildren().addAll(kdaChartCard, matchHistoryCard);

        grid.add(profileCard, 0, 0);
        grid.add(rightSide, 1, 0);

        workspace.getChildren().addAll(titleContainer, statsCard, grid);
        contentScrollPane.setContent(workspace);
    }

    private void refreshProfileViews() {
        // Refresh values inside sidebar
        sidebarPlayerName.setText(player.getName());
        sidebarPlayerRole.setText(player.getRole());
        
        // Refresh StatsCard (KDA and Rank can technically be affected if edited, though rank is read-only)
        statsCard.refresh();
        
        // Refresh welcome title
        setupContentWorkspace();
    }
}
