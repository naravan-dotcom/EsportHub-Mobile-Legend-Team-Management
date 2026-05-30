package esporthub.dashboard.player;

import esporthub.model.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StatsCard extends HBox {

    private final Player player;
    private Label rankValLabel;
    private Label winrateValLabel;
    private Label kdaValLabel;
    private ProgressBar winrateProgress;
    
    // Stats column labels
    private Label killsLabel;
    private Label deathsLabel;
    private Label assistsLabel;
    private Label ratioLabel;

    public StatsCard(Player player) {
        this.player = player;

        this.setSpacing(20);
        this.setPadding(new Insets(5, 0, 5, 0));
        this.setStyle("-fx-background-color: transparent;");

        // 1. Rank Card (Mythic look, dark indigo background with purple glow)
        VBox rankCard = createStatBox("RANK SEKARANG");
        rankCard.setStyle("-fx-background-color: linear-gradient(to bottom right, #1E1B4B, #312E81); " +
                          "-fx-background-radius: 16px; " +
                          "-fx-border-color: #4338CA; " +
                          "-fx-border-width: 1px; " +
                          "-fx-border-radius: 16px; " +
                          "-fx-padding: 20px; " +
                          "-fx-effect: dropshadow(three-pass-box, rgba(79,70,229,0.15), 15, 0, 0, 8);");
        
        // Custom header for rank card to look better on dark
        Label rankHeader = (Label) rankCard.getChildren().get(0);
        rankHeader.setStyle("-fx-text-fill: #C7D2FE; -fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 11px; -fx-letter-spacing: 1px;");
        
        rankValLabel = new Label(player.getRank());
        rankValLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 22));
        rankValLabel.setStyle("-fx-text-fill: #F59E0B; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 22px;");
        
        Label subRank = new Label("Divisi Utama");
        subRank.setFont(Font.font("Inter", 11));
        subRank.setStyle("-fx-text-fill: #94A3B8; -fx-font-family: 'Inter'; -fx-font-size: 11px;");
        
        rankCard.getChildren().addAll(rankValLabel, subRank);

        // 2. Winrate Card (Glassmorphism look with progress bar)
        VBox winrateCard = createStatBox("WIN RATE");
        winrateCard.setStyle("-fx-background-color: #FFFFFF; " +
                             "-fx-background-radius: 16px; " +
                             "-fx-border-color: #E2E8F0; " +
                             "-fx-border-width: 1px; " +
                             "-fx-border-radius: 16px; " +
                             "-fx-padding: 20px; " +
                             "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 15, 0, 0, 8);");
        
        Label wrHeader = (Label) winrateCard.getChildren().get(0);
        wrHeader.setStyle("-fx-text-fill: #64748B; -fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 11px; -fx-letter-spacing: 1px;");

        winrateValLabel = new Label(player.getWinrate());
        winrateValLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 26));
        winrateValLabel.setStyle("-fx-text-fill: #10B981; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 26px;");
        
        winrateProgress = new ProgressBar();
        double wrValue = parseWinrate(player.getWinrate());
        winrateProgress.setProgress(wrValue);
        winrateProgress.setMaxWidth(Double.MAX_VALUE);
        winrateProgress.setPrefHeight(8);
        winrateProgress.setStyle("-fx-accent: #10B981; -fx-control-inner-background: #F1F5F9; -fx-background-radius: 99px; -fx-padding: 0px;");
        
        winrateCard.getChildren().addAll(winrateValLabel, winrateProgress);

        // 3. KDA Card (3 Columns: Kills, Deaths, Assists, Ratio)
        VBox kdaCard = createStatBox("KDA RATIO");
        kdaCard.setStyle("-fx-background-color: #FFFFFF; " +
                          "-fx-background-radius: 16px; " +
                          "-fx-border-color: #E2E8F0; " +
                          "-fx-border-width: 1px; " +
                          "-fx-border-radius: 16px; " +
                          "-fx-padding: 20px; " +
                          "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.03), 15, 0, 0, 8);");
        HBox.setHgrow(kdaCard, Priority.ALWAYS);

        Label kdaHeader = (Label) kdaCard.getChildren().get(0);
        kdaHeader.setStyle("-fx-text-fill: #64748B; -fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 11px; -fx-letter-spacing: 1px;");

        kdaValLabel = new Label(player.getKda());
        kdaValLabel.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        kdaValLabel.setStyle("-fx-text-fill: #334155; -fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 14px;");

        HBox kdaSplitBox = new HBox(16);
        kdaSplitBox.setAlignment(Pos.CENTER_LEFT);
        kdaSplitBox.setPadding(new Insets(5, 0, 0, 0));
        
        killsLabel = new Label("0.0");
        deathsLabel = new Label("0.0");
        assistsLabel = new Label("0.0");
        ratioLabel = new Label("0.00 KDA");

        setupKdaSplit(player.getKda());

        VBox kBox = createSplitItem("K", killsLabel, "#0F172A");
        VBox dBox = createSplitItem("D", deathsLabel, "#F43F5E");
        VBox aBox = createSplitItem("A", assistsLabel, "#6366F1");
        
        Pane sep = new Pane();
        sep.setPrefWidth(1);
        sep.setStyle("-fx-background-color: #E2E8F0;");
        HBox.setHgrow(sep, Priority.NEVER);
        
        ratioLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 18));
        ratioLabel.setStyle("-fx-text-fill: #4F46E5; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 18px;");
        
        Label averageTitle = new Label("RATA-RATA");
        averageTitle.setStyle("-fx-text-fill: #94A3B8; -fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 9px;");
        VBox rBox = new VBox(2, averageTitle, ratioLabel);

        kdaSplitBox.getChildren().addAll(kBox, dBox, aBox, sep, rBox);
        kdaCard.getChildren().addAll(kdaValLabel, kdaSplitBox);
        
        // Set layout weights
        HBox.setHgrow(rankCard, Priority.ALWAYS);
        HBox.setHgrow(winrateCard, Priority.ALWAYS);
        HBox.setHgrow(kdaCard, Priority.ALWAYS);
        
        rankCard.setPrefWidth(220);
        winrateCard.setPrefWidth(220);
        kdaCard.setPrefWidth(320);

        this.getChildren().addAll(rankCard, winrateCard, kdaCard);
    }

    private VBox createStatBox(String titleText) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.TOP_LEFT);
        
        Label titleLabel = new Label(titleText);
        titleLabel.setFont(Font.font("Inter", FontWeight.BOLD, 10));
        titleLabel.setStyle("-fx-text-fill: #94A3B8; -fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 10px; -fx-letter-spacing: 1px;");
        
        box.getChildren().add(titleLabel);
        return box;
    }

    private VBox createSplitItem(String name, Label val, String hexColor) {
        val.setFont(Font.font("Outfit", FontWeight.BOLD, 18));
        val.setStyle("-fx-text-fill: " + hexColor + "; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 18px;");
        
        Label header = new Label(name);
        header.setFont(Font.font("Inter", FontWeight.BOLD, 10));
        header.setStyle("-fx-text-fill: #94A3B8; -fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 10px;");
        
        VBox box = new VBox(2, header, val);
        box.setAlignment(Pos.CENTER);
        box.setMinWidth(35);
        return box;
    }

    private double parseWinrate(String wrStr) {
        try {
            String clean = wrStr.replace("%", "").trim();
            return Double.parseDouble(clean) / 100.0;
        } catch (Exception e) {
            return 0.5;
        }
    }

    private void setupKdaSplit(String kdaStr) {
        try {
            String[] parts = kdaStr.split("/");
            if (parts.length == 3) {
                double k = Double.parseDouble(parts[0].trim());
                double d = Double.parseDouble(parts[1].trim());
                double a = Double.parseDouble(parts[2].trim());
                
                killsLabel.setText(String.format("%.1f", k));
                deathsLabel.setText(String.format("%.1f", d));
                assistsLabel.setText(String.format("%.1f", a));
                
                double ratio = d == 0 ? (k + a) : (k + a) / d;
                ratioLabel.setText(String.format("%.2f KDA", ratio));
                ratioLabel.setStyle("-fx-text-fill: #4F46E5; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 18px;");
            }
        } catch (Exception e) {
            killsLabel.setText("-");
            deathsLabel.setText("-");
            assistsLabel.setText("-");
            ratioLabel.setText("- KDA");
            ratioLabel.setStyle("-fx-text-fill: #64748B; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 18px;");
        }
    }

    public void refresh() {
        rankValLabel.setText(player.getRank());
        winrateValLabel.setText(player.getWinrate());
        kdaValLabel.setText(player.getKda());
        winrateProgress.setProgress(parseWinrate(player.getWinrate()));
        setupKdaSplit(player.getKda());
    }
}
