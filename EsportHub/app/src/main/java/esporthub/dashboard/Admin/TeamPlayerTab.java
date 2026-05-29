package esporthub.dashboard.Admin;

import esporthub.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TeamPlayerTab extends HBox {

    private final DataStore dataStore = DataStore.getInstance();

    private TableView<Team> teamTable;
    private TableView<Player> playerTable;

    private TeamPlayerDetailsCard detailsCard;
    private TeamPlayerFormCard formCard;

    public TeamPlayerTab() {
        this.setPadding(new Insets(20));
        this.setSpacing(20);
        this.setStyle("-fx-background-color: #FFFFFF;");

        // 1. Left Panel (Tables inside a TabPane)
        VBox leftPane = new VBox(15);
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        leftPane.setStyle("-fx-background-color: #FFFFFF;");

        Label sectionTitle = new Label("Database Tim & Pemain");
        sectionTitle.setFont(Font.font("Inter", FontWeight.BOLD, 18));
        sectionTitle.setTextFill(Color.web("#1F2937"));

        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: #FFFFFF; -fx-tab-max-width: 150px;");
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab teamTab = new Tab("Daftar Tim");
        setupTeamTable();
        teamTab.setContent(teamTable);

        Tab playerTab = new Tab("Daftar Pemain");
        setupPlayerTable();
        playerTab.setContent(playerTable);

        tabPane.getTabs().addAll(teamTab, playerTab);
        leftPane.getChildren().addAll(sectionTitle, tabPane);

        // 2. Right Panel (Modular Cards)
        VBox rightPane = new VBox(20);
        rightPane.setPrefWidth(380);
        rightPane.setMinWidth(380);
        rightPane.setPadding(new Insets(15));
        rightPane.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB; -fx-border-width: 1px; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        // Use method references to avoid capturing uninitialized finals in lambdas
        detailsCard = new TeamPlayerDetailsCard(this::handleEditRequest, this::refreshTables);
        formCard    = new TeamPlayerFormCard(this::refreshTables);

        rightPane.getChildren().addAll(detailsCard, formCard);
        this.getChildren().addAll(leftPane, rightPane);

        // Tab selection listener to switch form mode
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            clearSelection();
            if (newTab == teamTab) {
                formCard.showTeamMode();
            } else {
                formCard.showPlayerMode();
            }
        });

        // Initialize with default team form
        formCard.showTeamMode();
    }

    // Called when details card triggers an edit request
    private void handleEditRequest(Object item) {
        if (item instanceof Team) {
            formCard.enterEditMode((Team) item);
        } else if (item instanceof Player) {
            formCard.enterEditMode((Player) item);
        }
    }

    // Called when any data changes (add, edit, delete, ban)
    private void refreshTables() {
        teamTable.refresh();
        playerTable.refresh();
        // Refresh details card if something is selected
        Team selectedT = teamTable.getSelectionModel().getSelectedItem();
        if (selectedT != null) {
            detailsCard.showTeamDetails(selectedT);
        }
        Player selectedP = playerTable.getSelectionModel().getSelectedItem();
        if (selectedP != null) {
            detailsCard.showPlayerDetails(selectedP);
        }
    }

    private void setupTeamTable() {
        teamTable = new TableView<>();
        teamTable.setItems(dataStore.getTeams());
        teamTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        teamTable.setStyle("-fx-background-color: #FFFFFF; -fx-selection-bar: #E2E8F0;");

        TableColumn<Team, String> nameCol = new TableColumn<>("Nama Tim");
        nameCol.setCellValueFactory(f -> f.getValue().nameProperty());

        TableColumn<Team, Number> winsCol = new TableColumn<>("Menang");
        winsCol.setCellValueFactory(f -> f.getValue().winsProperty());
        winsCol.setPrefWidth(80);

        TableColumn<Team, Number> lossesCol = new TableColumn<>("Kalah");
        lossesCol.setCellValueFactory(f -> f.getValue().lossesProperty());
        lossesCol.setPrefWidth(80);

        TableColumn<Team, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(f ->
            new SimpleStringProperty(f.getValue().isBanned() ? "Banned" : "Aktif"));
        statusCol.setPrefWidth(100);

        teamTable.getColumns().addAll(nameCol, winsCol, lossesCol, statusCol);

        teamTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                playerTable.getSelectionModel().clearSelection();
                detailsCard.showTeamDetails(newSel);
            }
        });
    }

    private void setupPlayerTable() {
        playerTable = new TableView<>();
        playerTable.setItems(dataStore.getPlayers());
        playerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        playerTable.setStyle("-fx-background-color: #FFFFFF; -fx-selection-bar: #E2E8F0;");

        TableColumn<Player, String> nameCol = new TableColumn<>("Nama Pemain");
        nameCol.setCellValueFactory(f -> f.getValue().nameProperty());

        TableColumn<Player, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(f -> f.getValue().roleProperty());

        TableColumn<Player, String> teamCol = new TableColumn<>("Tim");
        teamCol.setCellValueFactory(f -> f.getValue().teamNameProperty());

        TableColumn<Player, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(f ->
            new SimpleStringProperty(f.getValue().isBanned() ? "Banned" : "Aktif"));
        statusCol.setPrefWidth(100);

        playerTable.getColumns().addAll(nameCol, roleCol, teamCol, statusCol);

        playerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                teamTable.getSelectionModel().clearSelection();
                detailsCard.showPlayerDetails(newSel);
            }
        });
    }

    private void clearSelection() {
        teamTable.getSelectionModel().clearSelection();
        playerTable.getSelectionModel().clearSelection();
        detailsCard.clearDetails();
        formCard.exitEditMode();
    }
}
