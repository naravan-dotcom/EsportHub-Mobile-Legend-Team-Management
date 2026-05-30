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
        this.setSpacing(16);
        this.setStyle("-fx-background-color: #FFFFFF; " +
                      "-fx-background-radius: 16px; " +
                      "-fx-border-color: #E2E8F0; " +
                      "-fx-border-width: 1px; " +
                      "-fx-border-radius: 16px; " +
                      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 15, 0, 0, 8);");

        Label titleLabel = new Label("Riwayat Pertandingan");
        titleLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 16));
        titleLabel.setStyle("-fx-text-fill: #0F172A; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 16px;");

        Label subtitleLabel = new Label("5 pertandingan terakhir di turnamen");
        subtitleLabel.setFont(Font.font("Inter", 11));
        subtitleLabel.setStyle("-fx-text-fill: #64748B; -fx-font-family: 'Inter'; -fx-font-size: 11px;");

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
            row.setPadding(new Insets(12, 16, 12, 16));
            row.setStyle("-fx-background-color: #F8FAFC; -fx-background-radius: 10px; -fx-border-color: #E2E8F0; -fx-border-width: 1px; -fx-border-radius: 10px;");

            // Hover effect
            row.setOnMouseEntered(e -> row.setStyle("-fx-background-color: #F1F5F9; -fx-background-radius: 10px; -fx-border-color: #CBD5E1; -fx-border-width: 1px; -fx-border-radius: 10px;"));
            row.setOnMouseExited(e -> row.setStyle("-fx-background-color: #F8FAFC; -fx-background-radius: 10px; -fx-border-color: #E2E8F0; -fx-border-width: 1px; -fx-border-radius: 10px;"));

            // Hero badge
            VBox heroInfo = new VBox(2);
            Label heroLabel = new Label(item.getHero());
            heroLabel.setFont(Font.font("Inter", FontWeight.BOLD, 13));
            heroLabel.setStyle("-fx-text-fill: #1E293B; -fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 13px;");
            
            Label classLabel = new Label("Hero Utama");
            classLabel.setFont(Font.font("Inter", 10));
            classLabel.setStyle("-fx-text-fill: #94A3B8; -fx-font-family: 'Inter'; -fx-font-size: 10px;");
            heroInfo.getChildren().addAll(heroLabel, classLabel);
            heroInfo.setMinWidth(90);

            // Win / Lose Badge
            Label resultBadge = new Label(item.getResult().toUpperCase());
            resultBadge.setFont(Font.font("Inter", FontWeight.BOLD, 11));
            resultBadge.setPadding(new Insets(4, 10, 4, 10));
            resultBadge.setAlignment(Pos.CENTER);
            
            if ("WIN".equalsIgnoreCase(item.getResult())) {
                resultBadge.setStyle("-fx-background-color: #D1FAE5; -fx-text-fill: #065F46; -fx-background-radius: 99px; -fx-font-weight: bold;");
            } else {
                resultBadge.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B; -fx-background-radius: 99px; -fx-font-weight: bold;");
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
            scoreLabel.setStyle("-fx-text-fill: #334155; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 14px;");
            
            Label kdaTitle = new Label("K / D / A");
            kdaTitle.setFont(Font.font("Inter", 9));
            kdaTitle.setStyle("-fx-text-fill: #94A3B8; -fx-font-family: 'Inter'; -fx-font-size: 9px;");
            kdaBox.getChildren().addAll(scoreLabel, kdaTitle);

            // Date
            Label dateLabel = new Label(item.getDate());
            dateLabel.setFont(Font.font("Inter", 12));
            dateLabel.setStyle("-fx-text-fill: #64748B; -fx-font-family: 'Inter'; -fx-font-size: 12px; -fx-padding: 0 0 0 10;");

            row.getChildren().addAll(heroInfo, resultBadge, spacer, kdaBox, dateLabel);
            historyList.getChildren().add(row);
        }
    }

    public void refresh() {
        renderHistory();
    }
}
