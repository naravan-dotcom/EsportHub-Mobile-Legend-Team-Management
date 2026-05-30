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

        this.setPadding(new Insets(24));
        this.setSpacing(16);
        this.setStyle("-fx-background-color: #FFFFFF; " +
                      "-fx-border-color: #E2E8F0; " +
                      "-fx-border-width: 1px; " +
                      "-fx-border-radius: 16px; " +
                      "-fx-background-radius: 16px; " +
                      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 15, 0, 0, 8);");

        Label schedTitle = new Label("Jadwalkan Pertandingan");
        schedTitle.setFont(Font.font("Outfit", FontWeight.BOLD, 15));
        schedTitle.setStyle("-fx-text-fill: #0F172A; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 15px;");

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
        setupButtonStyle(scheduleBtn, "#4F46E5", "#4338CA");
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
        field.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CBD5E1; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 10px 14px; -fx-font-size: 13px; -fx-text-fill: #0F172A; -fx-font-family: 'Inter';");
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #4F46E5; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 10px 14px; -fx-font-size: 13px; -fx-text-fill: #0F172A; -fx-font-family: 'Inter';");
            } else {
                field.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CBD5E1; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 10px 14px; -fx-font-size: 13px; -fx-text-fill: #0F172A; -fx-font-family: 'Inter';");
            }
        });
    }

    private void setupComboStyle(ComboBox<?> combo) {
        combo.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CBD5E1; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 6px 10px; -fx-font-size: 13px; -fx-font-family: 'Inter';");
    }

    private void setupButtonStyle(Button btn, String hexColor, String hoverColor) {
        btn.setStyle("-fx-background-color: " + hexColor + "; -fx-text-fill: #FFFFFF; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-padding: 10px 16px;");
        btn.setCursor(Cursor.HAND);
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: " + hoverColor + "; -fx-text-fill: #FFFFFF; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-padding: 10px 16px;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + hexColor + "; -fx-text-fill: #FFFFFF; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-padding: 10px 16px;"));
    }
}
