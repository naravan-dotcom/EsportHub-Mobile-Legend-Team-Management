package esporthub.dashboard.player.team;

import esporthub.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MatchHistorySection extends VBox {

    private final Team team;
    private TableView<Match> matchTable;
    private final ObservableList<Match> matchHistoryList = FXCollections.observableArrayList();

    public MatchHistorySection(Team team) {
        this.team = team;

        this.setSpacing(16);
        this.setPadding(new Insets(10, 0, 0, 0));

        Label sectionTitle = new Label("Riwayat Pertandingan");
        sectionTitle.setFont(Font.font("Outfit", FontWeight.BOLD, 18));
        sectionTitle.setStyle("-fx-text-fill: #0F172A; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 18px;");

        setupTable();
        refreshList();

        this.getChildren().addAll(sectionTitle, matchTable);
    }

    public void refreshList() {
        matchHistoryList.clear();
        String tName = team.getName();
        for (Match m : DataStore.getInstance().getMatches()) {
            if (m.getTeam1().getName().equalsIgnoreCase(tName) || m.getTeam2().getName().equalsIgnoreCase(tName)) {
                matchHistoryList.add(m);
            }
        }
    }

    private void setupTable() {
        matchTable = new TableView<>();
        matchTable.setItems(matchHistoryList);
        matchTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        matchTable.setPrefHeight(300);
        matchTable.setStyle(
            "-fx-background-color: #FFFFFF; " +
            "-fx-border-color: #E2E8F0; " +
            "-fx-border-radius: 12px; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-width: 1px; " +
            "-fx-padding: 8px; " +
            "-fx-control-inner-background: #FFFFFF; " +
            "-fx-table-cell-border-color: #F1F5F9; " +
            "-fx-table-header-border-color: transparent;"
        );

        TableColumn<Match, String> matchUpCol = new TableColumn<>("Pertandingan");
        matchUpCol.setCellValueFactory(f -> {
            Match m = f.getValue();
            return new SimpleStringProperty(m.getTeam1().getName() + " vs " + m.getTeam2().getName());
        });

        TableColumn<Match, String> dateCol = new TableColumn<>("Jadwal");
        dateCol.setCellValueFactory(f -> f.getValue().dateTimeProperty());
        dateCol.setPrefWidth(140);

        TableColumn<Match, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(f -> f.getValue().statusProperty());
        statusCol.setPrefWidth(90);

        TableColumn<Match, String> scoreCol = new TableColumn<>("Skor");
        scoreCol.setCellValueFactory(f -> {
            Match m = f.getValue();
            if ("Finished".equalsIgnoreCase(m.getStatus())) {
                return new SimpleStringProperty(m.getScore1() + " - " + m.getScore2());
            }
            return new SimpleStringProperty("-");
        });
        scoreCol.setPrefWidth(80);

        TableColumn<Match, String> winnerCol = new TableColumn<>("Pemenang");
        winnerCol.setCellValueFactory(f -> f.getValue().winnerProperty());
        winnerCol.setPrefWidth(120);

        matchTable.getColumns().addAll(matchUpCol, dateCol, statusCol, scoreCol, winnerCol);
    }
}
