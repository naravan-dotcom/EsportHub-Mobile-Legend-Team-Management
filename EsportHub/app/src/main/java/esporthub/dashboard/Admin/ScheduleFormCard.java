package esporthub.dashboard.Admin;

import esporthub.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.stream.Collectors;

public class ScheduleFormCard extends VBox {

    private final DataStore dataStore = DataStore.getInstance();

    private final ComboBox<Team> team1Combo;
    private final ComboBox<Team> team2Combo;
    private final TextField dateTimeField;
    private final Button scheduleBtn;

    private final Runnable onMatchScheduled;

    public ScheduleFormCard(Runnable onMatchScheduled) {
        this.onMatchScheduled = onMatchScheduled;

        this.setPadding(new Insets(15));
        this.setSpacing(12);
        this.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E5E7EB; -fx-border-width: 1px; -fx-border-radius: 6px; -fx-background-radius: 6px;");

        Label schedTitle = new Label("Jadwalkan Pertandingan");
        schedTitle.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        schedTitle.setTextFill(Color.web("#1F2937"));

        team1Combo = new ComboBox<>();
        team1Combo.setPromptText("Pilih Tim 1");
        team1Combo.setMaxWidth(Double.MAX_VALUE);
        setupComboStyle(team1Combo);

        team2Combo = new ComboBox<>();
        team2Combo.setPromptText("Pilih Tim 2");
        team2Combo.setMaxWidth(Double.MAX_VALUE);
        setupComboStyle(team2Combo);

        // Populate initial selectors
        populateTeamSelectors();

        dateTimeField = new TextField();
        dateTimeField.setPromptText("Tanggal & Waktu (e.g. YYYY-MM-DD HH:MM)");
        setupFieldStyle(dateTimeField);

        scheduleBtn = new Button("Jadwalkan Pertandingan");
        scheduleBtn.setMaxWidth(Double.MAX_VALUE);
        setupButtonStyle(scheduleBtn, "#5A67D8");
        scheduleBtn.setOnAction(e -> handleScheduleMatch());

        this.getChildren().addAll(schedTitle, team1Combo, team2Combo, dateTimeField, scheduleBtn);

        // Update list dynamically when teams list changes
        dataStore.getTeams().addListener((javafx.collections.ListChangeListener<Team>) c -> populateTeamSelectors());
    }

    public void populateTeamSelectors() {
        // Filter out banned teams
        ObservableList<Team> activeTeams = FXCollections.observableArrayList(
            dataStore.getTeams().stream().filter(t -> !t.isBanned()).collect(Collectors.toList())
        );
        team1Combo.setItems(activeTeams);
        team2Combo.setItems(activeTeams);
    }

    private void handleScheduleMatch() {
        Team t1 = team1Combo.getValue();
        Team t2 = team2Combo.getValue();
        String dt = dateTimeField.getText().trim();

        if (t1 == null || t2 == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Silakan pilih kedua tim yang tanding!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (t1.getId().equals(t2.getId())) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Tim 1 dan Tim 2 tidak boleh sama!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (dt.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Silakan masukkan tanggal & waktu tanding!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        String matchId = "M" + (dataStore.getMatches().size() + 1);
        Match match = new Match(matchId, t1, t2, dt);
        dataStore.addMatch(match);

        // Reset inputs
        team1Combo.setValue(null);
        team2Combo.setValue(null);
        dateTimeField.clear();

        // Refresh table list
        onMatchScheduled.run();
    }

    private void setupFieldStyle(TextField field) {
        field.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E5E7EB; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-padding: 8px 12px; -fx-font-size: 13px;");
    }

    private void setupComboStyle(ComboBox<?> combo) {
        combo.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E5E7EB; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-font-size: 13px;");
    }

    private void setupButtonStyle(Button btn, String hexColor) {
        btn.setStyle("-fx-background-color: " + hexColor + "; -fx-text-fill: #FFFFFF; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 8px 16px;");
        btn.setCursor(Cursor.HAND);
    }
}
