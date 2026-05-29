package esporthub.dashboard;

import esporthub.model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MatchResultCard extends VBox {

    private final Label resultsTitle;
    private final Label team1Label;
    private final Label team2Label;
    private final TextField score1Field;
    private final TextField score2Field;
    private final Button saveResultsBtn;

    private final Runnable onResultsSaved;
    private Match activeMatch = null;

    public MatchResultCard(Runnable onResultsSaved) {
        this.onResultsSaved = onResultsSaved;

        this.setPadding(new Insets(15));
        this.setSpacing(12);
        this.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E5E7EB; -fx-border-width: 1px; -fx-border-radius: 6px; -fx-background-radius: 6px;");
        this.setVisible(false);
        this.setManaged(false);

        resultsTitle = new Label("Input Hasil Pertandingan");
        resultsTitle.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        resultsTitle.setTextFill(Color.web("#1F2937"));

        HBox scoresRow = new HBox(10);
        scoresRow.setAlignment(Pos.CENTER);

        VBox team1Box = new VBox(6);
        team1Box.setAlignment(Pos.CENTER);
        team1Label = new Label("Tim 1");
        team1Label.setFont(Font.font("Inter", FontWeight.SEMI_BOLD, 12));
        score1Field = new TextField();
        score1Field.setPrefWidth(70);
        score1Field.setAlignment(Pos.CENTER);
        setupFieldStyle(score1Field);
        team1Box.getChildren().addAll(team1Label, score1Field);

        Label vsLabel = new Label("VS");
        vsLabel.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        vsLabel.setTextFill(Color.web("#9CA3AF"));

        VBox team2Box = new VBox(6);
        team2Box.setAlignment(Pos.CENTER);
        team2Label = new Label("Tim 2");
        team2Label.setFont(Font.font("Inter", FontWeight.SEMI_BOLD, 12));
        score2Field = new TextField();
        score2Field.setPrefWidth(70);
        score2Field.setAlignment(Pos.CENTER);
        setupFieldStyle(score2Field);
        team2Box.getChildren().addAll(team2Label, score2Field);

        scoresRow.getChildren().addAll(team1Box, vsLabel, team2Box);

        saveResultsBtn = new Button("Simpan Hasil Skor");
        saveResultsBtn.setMaxWidth(Double.MAX_VALUE);
        setupButtonStyle(saveResultsBtn, "#10B981");
        saveResultsBtn.setOnAction(e -> handleSaveResults());

        this.getChildren().addAll(resultsTitle, scoresRow, saveResultsBtn);
    }

    public void showResultsForm(Match match) {
        this.activeMatch = match;
        this.setVisible(true);
        this.setManaged(true);
        resultsTitle.setText("Hasil Skor: " + match.getTeam1().getName() + " vs " + match.getTeam2().getName());
        team1Label.setText(match.getTeam1().getName());
        team2Label.setText(match.getTeam2().getName());
        score1Field.clear();
        score2Field.clear();
    }

    public void hideResultsForm() {
        this.activeMatch = null;
        this.setVisible(false);
        this.setManaged(false);
    }

    private void handleSaveResults() {
        if (activeMatch == null) return;

        String s1 = score1Field.getText().trim();
        String s2 = score2Field.getText().trim();

        int sc1, sc2;
        try {
            sc1 = Integer.parseInt(s1);
            sc2 = Integer.parseInt(s2);
            if (sc1 < 0 || sc2 < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Skor harus berupa angka positif!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        activeMatch.setScore1(sc1);
        activeMatch.setScore2(sc2);
        activeMatch.setStatus("Finished");

        if (sc1 > sc2) {
            activeMatch.setWinner(activeMatch.getTeam1().getName());
            activeMatch.getTeam1().setWins(activeMatch.getTeam1().getWins() + 1);
            activeMatch.getTeam2().setLosses(activeMatch.getTeam2().getLosses() + 1);
        } else if (sc2 > sc1) {
            activeMatch.setWinner(activeMatch.getTeam2().getName());
            activeMatch.getTeam2().setWins(activeMatch.getTeam2().getWins() + 1);
            activeMatch.getTeam1().setLosses(activeMatch.getTeam1().getLosses() + 1);
        } else {
            activeMatch.setWinner("Draw");
        }

        hideResultsForm();
        onResultsSaved.run();
    }

    private void setupFieldStyle(TextField field) {
        field.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E5E7EB; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-padding: 8px 12px; -fx-font-size: 13px;");
    }

    private void setupButtonStyle(Button btn, String hexColor) {
        btn.setStyle("-fx-background-color: " + hexColor + "; -fx-text-fill: #FFFFFF; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 8px 16px;");
        btn.setCursor(Cursor.HAND);
    }
}
