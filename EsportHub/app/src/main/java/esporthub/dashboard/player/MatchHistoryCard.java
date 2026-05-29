package esporthub.dashboard.player;

import esporthub.model.Player;
import esporthub.model.Player.MatchHistoryItem;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MatchHistoryCard extends VBox {

    private final Player player;
    private final VBox historyList;

    public MatchHistoryCard(Player player) {
        this.player = player;

        this.setPadding(new Insets(20));
        this.setSpacing(14);
        this.setStyle("-fx-background-color: #FFFFFF; " +
                      "-fx-background-radius: 12px; " +
                      "-fx-border-color: #F3F4F6; " +
                      "-fx-border-width: 1px; " +
                      "-fx-border-radius: 12px; " +
                      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 4);");

        Label titleLabel = new Label("Riwayat Pertandingan");
        titleLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 16));
        titleLabel.setTextFill(Color.web("#111827"));

        Label subtitleLabel = new Label("5 pertandingan terakhir di turnamen");
        subtitleLabel.setFont(Font.font("Inter", 11));
        subtitleLabel.setTextFill(Color.web("#6B7280"));

        VBox headerBox = new VBox(2, titleLabel, subtitleLabel);
        this.getChildren().add(headerBox);

        historyList = new VBox(10);
        historyList.setStyle("-fx-background-color: transparent;");
        this.getChildren().add(historyList);

        renderHistory();
    }

    private void renderHistory() {
        historyList.getChildren().clear();

        for (MatchHistoryItem item : player.getMatchesPlayed()) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(10, 14, 10, 14));
            row.setStyle("-fx-background-color: #F9FAFB; -fx-background-radius: 8px; -fx-border-color: #E5E7EB; -fx-border-width: 1px; -fx-border-radius: 8px;");

            // Hover effect
            row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: #F3F4F6; -fx-background-radius: 8px; -fx-border-color: #D1D5DB; -fx-border-width: 1px; -fx-border-radius: 8px;"));
            row.setOnMouseExited(e -> row.setStyle("-fx-background-color: #F9FAFB; -fx-background-radius: 8px; -fx-border-color: #E5E7EB; -fx-border-width: 1px; -fx-border-radius: 8px;"));

            // Hero badge
            VBox heroInfo = new VBox(2);
            Label heroLabel = new Label(item.getHero());
            heroLabel.setFont(Font.font("Inter", FontWeight.BOLD, 13));
            heroLabel.setTextFill(Color.web("#1F2937"));
            
            Label classLabel = new Label("Hero Utama");
            classLabel.setFont(Font.font("Inter", 10));
            classLabel.setTextFill(Color.web("#9CA3AF"));
            heroInfo.getChildren().addAll(heroLabel, classLabel);
            heroInfo.setMinWidth(90);

            // Win / Lose Badge
            Label resultBadge = new Label(item.getResult().toUpperCase());
            resultBadge.setFont(Font.font("Inter", FontWeight.BOLD, 11));
            resultBadge.setPadding(new Insets(4, 10, 4, 10));
            resultBadge.setAlignment(Pos.CENTER);
            
            if ("WIN".equalsIgnoreCase(item.getResult())) {
                resultBadge.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-background-radius: 99px;");
            } else {
                resultBadge.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-background-radius: 99px;");
            }
            resultBadge.setMinWidth(65);

            // Spacer
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // KDA Score
            VBox kdaBox = new VBox(2);
            kdaBox.setAlignment(Pos.CENTER_RIGHT);
            Label scoreLabel = new Label(item.getScore());
            scoreLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 14));
            scoreLabel.setTextFill(Color.web("#374151"));
            
            Label kdaTitle = new Label("K / D / A");
            kdaTitle.setFont(Font.font("Inter", 9));
            kdaTitle.setTextFill(Color.web("#9CA3AF"));
            kdaBox.getChildren().addAll(scoreLabel, kdaTitle);

            // Date
            Label dateLabel = new Label(item.getDate());
            dateLabel.setFont(Font.font("Inter", 12));
            dateLabel.setTextFill(Color.web("#6B7280"));
            dateLabel.setPadding(new Insets(0, 0, 0, 10));

            row.getChildren().addAll(heroInfo, resultBadge, spacer, kdaBox, dateLabel);
            historyList.getChildren().add(row);
        }
    }

    public void refresh() {
        renderHistory();
    }
}
