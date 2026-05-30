package esporthub.dashboard.player.team;

import esporthub.model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TeamRoomTab extends VBox {

    private final Player currentPlayer;
    private final Runnable onDashboardRefresh;

    private TeamRoomHeader header;
    private MemberListSection memberSection;
    private ScheduleSection scheduleSection;
    private MatchHistorySection matchSection;

    public TeamRoomTab(Player currentPlayer, Runnable onDashboardRefresh) {
        this.currentPlayer = currentPlayer;
        this.onDashboardRefresh = onDashboardRefresh;

        this.setPadding(new Insets(30));
        this.setSpacing(24);
        this.setStyle("-fx-background-color: transparent;");

        setupContent();
    }

    public void setupContent() {
        this.getChildren().clear();

        String teamName = currentPlayer.getTeamName();
        if (teamName == null || teamName.equalsIgnoreCase("None") || teamName.isEmpty()) {
            setupNoTeamView();
        } else {
            setupTeamView(teamName);
        }
    }

    private void setupNoTeamView() {
        // Welcome and intro
        Label titleLabel = new Label("Ruang Tim EsportHub");
        titleLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #0F172A; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 24px;");

        Label subtitleLabel = new Label("Anda belum memiliki tim saat ini. Silakan gabung dengan tim yang sudah ada atau buat tim baru Anda!");
        subtitleLabel.setFont(Font.font("Inter", 13));
        subtitleLabel.setStyle("-fx-text-fill: #64748B; -fx-font-family: 'Inter'; -fx-font-size: 13px;");

        VBox titleBox = new VBox(6, titleLabel, subtitleLabel);

        // Grid for Join / Create choices
        GridPane grid = new GridPane();
        grid.setHgap(24);
        grid.setVgap(24);
        
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(50);
        grid.getColumnConstraints().addAll(col, col);

        // 1. Join Team Card
        VBox joinCard = new VBox(16);
        joinCard.setPadding(new Insets(24));
        joinCard.setStyle("-fx-background-color: #FFFFFF; " +
                          "-fx-border-color: #E2E8F0; " +
                          "-fx-border-width: 1px; " +
                          "-fx-border-radius: 16px; " +
                          "-fx-background-radius: 16px; " +
                          "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 15, 0, 0, 8);");

        Label joinTitle = new Label("Gabung Tim");
        joinTitle.setFont(Font.font("Outfit", FontWeight.BOLD, 18));
        joinTitle.setStyle("-fx-text-fill: #1E1B4B; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 18px;");

        Label joinDesc = new Label("Bergabunglah dengan tim yang sudah terdaftar dalam turnamen ini dan berjuang bersama.");
        joinDesc.setWrapText(true);
        joinDesc.setFont(Font.font("Inter", 12));
        joinDesc.setStyle("-fx-text-fill: #64748B; -fx-font-family: 'Inter'; -fx-font-size: 12px; -fx-min-height: 40px;");

        ComboBox<Team> teamCombo = new ComboBox<>();
        teamCombo.setMaxWidth(Double.MAX_VALUE);
        teamCombo.setPromptText("Pilih Tim...");
        teamCombo.setItems(DataStore.getInstance().getTeams());
        teamCombo.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CBD5E1; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 6px 10px; -fx-font-size: 13px; -fx-font-family: 'Inter';");

        Button joinBtn = new Button("Gabung Sekarang");
        joinBtn.setMaxWidth(Double.MAX_VALUE);
        joinBtn.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        joinBtn.setStyle("-fx-background-color: #312E81; -fx-text-fill: #FFFFFF; -fx-background-radius: 8px; -fx-padding: 12px; -fx-font-weight: bold;");
        joinBtn.setCursor(Cursor.HAND);
        joinBtn.setOnMouseEntered(e -> joinBtn.setStyle("-fx-background-color: #1E1B4B; -fx-text-fill: #FFFFFF; -fx-background-radius: 8px; -fx-padding: 12px; -fx-font-weight: bold;"));
        joinBtn.setOnMouseExited(e -> joinBtn.setStyle("-fx-background-color: #312E81; -fx-text-fill: #FFFFFF; -fx-background-radius: 8px; -fx-padding: 12px; -fx-font-weight: bold;"));
        joinBtn.setOnAction(e -> {
            Team selected = teamCombo.getValue();
            if (selected == null) {
                showAlert("Peringatan", "Silakan pilih tim terlebih dahulu!", Alert.AlertType.WARNING);
                return;
            }
            if (selected.isBanned()) {
                showAlert("Error", "Tim ini dalam status banned dan tidak bisa dimasuki!", Alert.AlertType.ERROR);
                return;
            }
            currentPlayer.setTeamName(selected.getName());
            showAlert("Sukses", "Anda telah bergabung dengan tim " + selected.getName() + "!", Alert.AlertType.INFORMATION);
            setupContent();
            onDashboardRefresh.run();
        });

        joinCard.getChildren().addAll(joinTitle, joinDesc, teamCombo, joinBtn);

        // 2. Create Team Card
        VBox createCard = new VBox(16);
        createCard.setPadding(new Insets(24));
        createCard.setStyle("-fx-background-color: #FFFFFF; " +
                            "-fx-border-color: #E2E8F0; " +
                            "-fx-border-width: 1px; " +
                            "-fx-border-radius: 16px; " +
                            "-fx-background-radius: 16px; " +
                            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 15, 0, 0, 8);");

        Label createTitle = new Label("Buat Tim Baru");
        createTitle.setFont(Font.font("Outfit", FontWeight.BOLD, 18));
        createTitle.setStyle("-fx-text-fill: #4F46E5; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 18px;");

        Label createDesc = new Label("Buat tim kreasimu sendiri dan undang pemain lain. Kamu otomatis akan menjadi Ketua Tim.");
        createDesc.setWrapText(true);
        createDesc.setFont(Font.font("Inter", 12));
        createDesc.setStyle("-fx-text-fill: #64748B; -fx-font-family: 'Inter'; -fx-font-size: 12px; -fx-min-height: 40px;");

        TextField teamNameField = new TextField();
        teamNameField.setPromptText("Nama Tim Baru...");
        teamNameField.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CBD5E1; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 10px 14px; -fx-font-size: 13px; -fx-text-fill: #0F172A; -fx-font-family: 'Inter';");

        Button createBtn = new Button("Buat Tim");
        createBtn.setMaxWidth(Double.MAX_VALUE);
        createBtn.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        createBtn.setStyle("-fx-background-color: linear-gradient(to right, #4F46E5, #6366F1); -fx-text-fill: #FFFFFF; -fx-background-radius: 8px; -fx-padding: 12px; -fx-font-weight: bold;");
        createBtn.setCursor(Cursor.HAND);
        createBtn.setOnMouseEntered(e -> createBtn.setStyle("-fx-background-color: linear-gradient(to right, #4338CA, #4F46E5); -fx-text-fill: #FFFFFF; -fx-background-radius: 8px; -fx-padding: 12px; -fx-font-weight: bold;"));
        createBtn.setOnMouseExited(e -> createBtn.setStyle("-fx-background-color: linear-gradient(to right, #4F46E5, #6366F1); -fx-text-fill: #FFFFFF; -fx-background-radius: 8px; -fx-padding: 12px; -fx-font-weight: bold;"));
        createBtn.setOnAction(e -> {
            String name = teamNameField.getText().trim();
            if (name.isEmpty()) {
                showAlert("Peringatan", "Nama tim tidak boleh kosong!", Alert.AlertType.WARNING);
                return;
            }

            // Check duplicate team name
            for (Team t : DataStore.getInstance().getTeams()) {
                if (t.getName().equalsIgnoreCase(name)) {
                    showAlert("Error", "Nama tim sudah digunakan oleh tim lain!", Alert.AlertType.ERROR);
                    return;
                }
            }

            // Create team
            String id = "T" + (DataStore.getInstance().getTeams().size() + 1);
            Team newTeam = new Team(id, name, 0, 0, false);
            newTeam.setCaptainName(currentPlayer.getName());
            DataStore.getInstance().addTeam(newTeam);

            // Join team
            currentPlayer.setTeamName(name);

            showAlert("Sukses", "Tim \"" + name + "\" berhasil dibuat dan Anda menjadi Ketua!", Alert.AlertType.INFORMATION);
            setupContent();
            onDashboardRefresh.run();
        });

        createCard.getChildren().addAll(createTitle, createDesc, teamNameField, createBtn);

        grid.add(joinCard, 0, 0);
        grid.add(createCard, 1, 0);

        this.getChildren().addAll(titleBox, grid);
    }

    private void setupTeamView(String teamName) {
        // Find team
        Team currentTeam = null;
        for (Team t : DataStore.getInstance().getTeams()) {
            if (t.getName().equalsIgnoreCase(teamName)) {
                currentTeam = t;
                break;
            }
        }

        if (currentTeam == null) {
            // Handle edge case where the team name doesn't match any stored team (perhaps deleted)
            currentPlayer.setTeamName("None");
            setupNoTeamView();
            return;
        }

        Team team = currentTeam;

        // 1. Team Room Header
        header = new TeamRoomHeader(currentPlayer, team, () -> {
            // Refresh on data changed (like rename or leave)
            setupContent();
            onDashboardRefresh.run();
        });

        // 2. Tab Pane for members, practice schedule, match history
        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: transparent; -fx-tab-max-width: 160px; -fx-tab-min-height: 36px;");
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        // Members Tab
        Tab memberTab = new Tab("Anggota");
        memberSection = new MemberListSection(currentPlayer, team, () -> {
            // Refresh sections if someone gets kicked
            if (scheduleSection != null) scheduleSection.refreshList();
            if (memberSection != null) memberSection.refreshList();
            onDashboardRefresh.run();
        });
        memberTab.setContent(memberSection);

        // Schedule Tab
        Tab scheduleTab = new Tab("Jadwal Latihan");
        scheduleSection = new ScheduleSection(currentPlayer, team, () -> {
            if (scheduleSection != null) scheduleSection.refreshList();
            onDashboardRefresh.run();
        });
        scheduleTab.setContent(scheduleSection);

        // Match History Tab
        Tab matchTab = new Tab("Riwayat Match");
        matchSection = new MatchHistorySection(team);
        matchTab.setContent(matchSection);

        tabPane.getTabs().addAll(memberTab, scheduleTab, matchTab);

        this.getChildren().addAll(header, tabPane);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
