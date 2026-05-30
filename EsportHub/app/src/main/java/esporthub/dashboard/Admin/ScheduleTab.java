package esporthub.dashboard.Admin;

import esporthub.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ScheduleTab extends HBox {

    private final DataStore dataStore = DataStore.getInstance();

    private TableView<Match> matchTable;
    private final ScheduleFormCard scheduleFormCard;
    private final MatchResultCard matchResultCard;

    public ScheduleTab() {
        this.setPadding(new Insets(30));
        this.setSpacing(24);
        this.setStyle("-fx-background-color: transparent;");

        // 1. Left Panel (Match Schedule Table)
        VBox leftPane = new VBox(20);
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        leftPane.setStyle("-fx-background-color: transparent;");

        Label sectionTitle = new Label("Jadwal & Hasil Pertandingan");
        sectionTitle.setFont(Font.font("Outfit", FontWeight.BOLD, 22));
        sectionTitle.setStyle("-fx-text-fill: #0F172A; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 22px;");

        setupMatchTable();
        leftPane.getChildren().addAll(sectionTitle, matchTable);

        // 2. Right Panel (Modular Form Cards Container)
        VBox rightPane = new VBox(24);
        rightPane.setPrefWidth(380);
        rightPane.setMinWidth(380);
        rightPane.setStyle("-fx-background-color: transparent;");

        // Instantiate Match Result Card
        matchResultCard = new MatchResultCard(() -> {
            handleDataChanged();
            matchTable.getSelectionModel().clearSelection();
        });

        // Instantiate Schedule Form Card
        scheduleFormCard = new ScheduleFormCard(this::handleDataChanged);

        rightPane.getChildren().addAll(scheduleFormCard, matchResultCard);
        this.getChildren().addAll(leftPane, rightPane);
    }

    private void handleDataChanged() {
        matchTable.refresh();
        if (scheduleFormCard != null) {
            scheduleFormCard.populateTeamSelectors();
        }
    }

    private void setupMatchTable() {
        matchTable = new TableView<>();
        matchTable.setItems(dataStore.getMatches());
        matchTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        matchTable.setStyle(
            "-fx-background-color: #FFFFFF; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-color: #E2E8F0; " +
            "-fx-border-radius: 12px; " +
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

        matchTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null && "Scheduled".equalsIgnoreCase(newSel.getStatus())) {
                matchResultCard.showResultsForm(newSel);
            } else {
                matchResultCard.hideResultsForm();
            }
        });
    }
}
