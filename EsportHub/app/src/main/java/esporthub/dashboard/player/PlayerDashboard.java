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
        this.setStyle("-fx-background-color: #F8FAFC;"); // Modern soft slate gray background

        // 1. Sidebar Navigation (Left - Deep Slate Navy style)
        sidebar = new VBox(20);
        sidebar.setPrefWidth(240);
        sidebar.setPadding(new Insets(30, 20, 30, 20));
        sidebar.setStyle("-fx-background-color: #0F172A; -fx-border-color: #1E293B; -fx-border-width: 0 1px 0 0;");

        // Branding
        Label logoLabel = new Label("EsportHub");
        logoLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 24));
        logoLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 24px;");

        Label roleLabel = new Label("DASHBOARD PLAYER");
        roleLabel.setFont(Font.font("Inter", FontWeight.BOLD, 10));
        roleLabel.setStyle("-fx-text-fill: #6366F1; -fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 10px; -fx-letter-spacing: 1.5px;");

        VBox brandBox = new VBox(4, logoLabel, roleLabel);
        brandBox.setPadding(new Insets(0, 0, 10, 0));

        // Separator
        Pane separator = new Pane();
        separator.setPrefHeight(1);
        separator.setStyle("-fx-background-color: #1E293B;");

        // Player Info Box in Sidebar (Glassy dark card look)
        VBox playerInfoBox = new VBox(8);
        playerInfoBox.setAlignment(Pos.CENTER);
        playerInfoBox.setPadding(new Insets(20, 15, 20, 15));
        playerInfoBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.03); -fx-background-radius: 12px; -fx-border-color: rgba(255, 255, 255, 0.08); -fx-border-width: 1px; -fx-border-radius: 12px;");
        
        // Circular Avatar with Gradient
        StackPane avatar = new StackPane();
        avatar.setPrefSize(56, 56);
        avatar.setMaxSize(56, 56);
        avatar.setStyle("-fx-background-color: linear-gradient(to bottom right, #6366F1, #4F46E5); -fx-background-radius: 28px;");
        Label avatarLetter = new Label(player.getName().substring(0, 1).toUpperCase());
        avatarLetter.setFont(Font.font("Outfit", FontWeight.BOLD, 22));
        avatarLetter.setStyle("-fx-text-fill: #FFFFFF; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 22px;");
        avatar.getChildren().add(avatarLetter);

        sidebarPlayerName = new Label(player.getName());
        sidebarPlayerName.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        sidebarPlayerName.setStyle("-fx-text-fill: #FFFFFF; -fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 14px;");

        sidebarPlayerRole = new Label(player.getRole());
        sidebarPlayerRole.setFont(Font.font("Inter", 12));
        sidebarPlayerRole.setStyle("-fx-text-fill: #818CF8; -fx-font-family: 'Inter'; -fx-font-size: 12px;");

        playerInfoBox.getChildren().addAll(avatar, sidebarPlayerName, sidebarPlayerRole);

        // Sidebar Navigation Button
        Button menuBtn = new Button("Profil & Statistik");
        menuBtn.setMaxWidth(Double.MAX_VALUE);
        menuBtn.setFont(Font.font("Inter", FontWeight.BOLD, 13));
        menuBtn.setStyle("-fx-background-color: #312E81; -fx-text-fill: #E0E7FF; -fx-alignment: center-left; -fx-padding: 12px 18px; -fx-background-radius: 8px; -fx-font-weight: bold;");

        // Logout Button
        Button logoutBtn = new Button("Keluar Aplikasi");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setFont(Font.font("Inter", FontWeight.BOLD, 13));
        logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #F87171; -fx-alignment: center-left; -fx-padding: 12px 18px; -fx-background-radius: 8px; -fx-font-weight: bold;");
        logoutBtn.setCursor(Cursor.HAND);
        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle("-fx-background-color: rgba(239, 68, 68, 0.1); -fx-text-fill: #F87171; -fx-alignment: center-left; -fx-padding: 12px 18px; -fx-background-radius: 8px; -fx-font-weight: bold;"));
        logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #F87171; -fx-alignment: center-left; -fx-padding: 12px 18px; -fx-background-radius: 8px; -fx-font-weight: bold;"));
        logoutBtn.setOnAction(e -> onLogout.run());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sidebar.getChildren().addAll(brandBox, separator, playerInfoBox, menuBtn, spacer, logoutBtn);
        this.setLeft(sidebar);

        // 2. Scrollable Content Area (Center)
        contentScrollPane = new ScrollPane();
        contentScrollPane.setFitToWidth(true);
        contentScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-padding: 0;");
        contentScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        contentScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        this.setCenter(contentScrollPane);

        setupContentWorkspace();
    }

    private void setupContentWorkspace() {
        VBox workspace = new VBox(24);
        workspace.setPadding(new Insets(30));
        workspace.setStyle("-fx-background-color: transparent;");

        // Welcome banner/title
        Label welcomeTitle = new Label("Selamat Datang, " + player.getName() + "!");
        welcomeTitle.setFont(Font.font("Outfit", FontWeight.BOLD, 24));
        welcomeTitle.setStyle("-fx-text-fill: #0F172A; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 24px;");
        
        Label welcomeSubtitle = new Label("Kelola informasi profil kamu dan pantau performa pertandingan di sini.");
        welcomeSubtitle.setFont(Font.font("Inter", 13));
        welcomeSubtitle.setStyle("-fx-text-fill: #64748B; -fx-font-family: 'Inter'; -fx-font-size: 13px;");

        VBox titleContainer = new VBox(6, welcomeTitle, welcomeSubtitle);

        // 1. Stats Card (Rank, Winrate, KDA) - Spans horizontally
        statsCard = new StatsCard(player);

        // 2. Bottom Grid layout (Profile Form on left, Graph and Match History on right)
        GridPane grid = new GridPane();
        grid.setHgap(24);
        grid.setVgap(24);

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

        VBox rightSide = new VBox(24);
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
