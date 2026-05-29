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

        this.setSpacing(16);
        this.setPadding(new Insets(2)); // outer margins handled by parent
        this.setStyle("-fx-background-color: transparent;");

        // 1. Rank Card (Mythic look, dark indigo background with purple glow)
        VBox rankCard = createStatBox("RANK SEKARANG", Color.web("#EEF2F6"));
        rankCard.setStyle("-fx-background-color: linear-gradient(to bottom right, #312E81, #1E1B4B); " +
                          "-fx-background-radius: 12px; " +
                          "-fx-padding: 18px; " +
                          "-fx-effect: dropshadow(three-pass-box, rgba(49,46,129,0.2), 10, 0, 0, 4);");
        
        // Custom header for rank card to look better on dark
        Label rankHeader = (Label) rankCard.getChildren().get(0);
        rankHeader.setTextFill(Color.web("#C7D2FE"));
        
        rankValLabel = new Label(player.getRank());
        rankValLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 22));
        rankValLabel.setTextFill(Color.web("#F59E0B")); // Gold color
        
        Label subRank = new Label("Divisi Utama");
        subRank.setFont(Font.font("Inter", 11));
        subRank.setTextFill(Color.web("#98A2B3"));
        
        rankCard.getChildren().addAll(rankValLabel, subRank);

        // 2. Winrate Card (Glassmorphism look with progress bar)
        VBox winrateCard = createStatBox("WIN RATE", Color.web("#FFFFFF"));
        winrateCard.setStyle("-fx-background-color: #FFFFFF; " +
                             "-fx-background-radius: 12px; " +
                             "-fx-border-color: #F3F4F6; " +
                             "-fx-border-width: 1px; " +
                             "-fx-border-radius: 12px; " +
                             "-fx-padding: 18px; " +
                             "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 4);");
        
        winrateValLabel = new Label(player.getWinrate());
        winrateValLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 26));
        winrateValLabel.setTextFill(Color.web("#10B981")); // Emerald Green
        
        winrateProgress = new ProgressBar();
        double wrValue = parseWinrate(player.getWinrate());
        winrateProgress.setProgress(wrValue);
        winrateProgress.setMaxWidth(Double.MAX_VALUE);
        winrateProgress.setPrefHeight(6);
        // Style progress bar green
        winrateProgress.setStyle("-fx-accent: #10B981; -fx-control-inner-background: #E5E7EB;");
        
        winrateCard.getChildren().addAll(winrateValLabel, winrateProgress);

        // 3. KDA Card (3 Columns: Kills, Deaths, Assists, Ratio)
        VBox kdaCard = createStatBox("KDA RATIO", Color.web("#FFFFFF"));
        kdaCard.setStyle("-fx-background-color: #FFFFFF; " +
                          "-fx-background-radius: 12px; " +
                          "-fx-border-color: #F3F4F6; " +
                          "-fx-border-width: 1px; " +
                          "-fx-border-radius: 12px; " +
                          "-fx-padding: 18px; " +
                          "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 4);");
        HBox.setHgrow(kdaCard, Priority.ALWAYS);

        kdaValLabel = new Label(player.getKda());
        kdaValLabel.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        kdaValLabel.setTextFill(Color.web("#374151"));

        HBox kdaSplitBox = new HBox(12);
        kdaSplitBox.setAlignment(Pos.CENTER_LEFT);
        
        killsLabel = new Label("0.0");
        deathsLabel = new Label("0.0");
        assistsLabel = new Label("0.0");
        ratioLabel = new Label("0.00 KDA");

        setupKdaSplit(player.getKda());

        VBox kBox = createSplitItem("K", killsLabel, "#111827");
        VBox dBox = createSplitItem("D", deathsLabel, "#EF4444");
        VBox aBox = createSplitItem("A", assistsLabel, "#6366F1");
        
        Pane sep = new Pane();
        sep.setPrefWidth(1);
        sep.setStyle("-fx-background-color: #E5E7EB;");
        HBox.setHgrow(sep, Priority.NEVER);
        
        ratioLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 18));
        ratioLabel.setTextFill(Color.web("#4F46E5"));
        VBox rBox = new VBox(2, new Label("RATA-RATA"), ratioLabel);
        ((Label)rBox.getChildren().get(0)).setFont(Font.font("Inter", FontWeight.SEMI_BOLD, 9));
        ((Label)rBox.getChildren().get(0)).setTextFill(Color.web("#9CA3AF"));

        kdaSplitBox.getChildren().addAll(kBox, dBox, aBox, sep, rBox);
        kdaCard.getChildren().addAll(kdaValLabel, kdaSplitBox);
        
        // Set layout weights
        HBox.setHgrow(rankCard, Priority.ALWAYS);
        HBox.setHgrow(winrateCard, Priority.ALWAYS);
        HBox.setHgrow(kdaCard, Priority.ALWAYS);
        
        rankCard.setPrefWidth(200);
        winrateCard.setPrefWidth(200);
        kdaCard.setPrefWidth(280);

        this.getChildren().addAll(rankCard, winrateCard, kdaCard);
    }

    private VBox createStatBox(String titleText, Color titleColor) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.TOP_LEFT);
        
        Label titleLabel = new Label(titleText);
        titleLabel.setFont(Font.font("Inter", FontWeight.BOLD, 10));
        titleLabel.setTextFill(Color.web("#9CA3AF"));
        
        box.getChildren().add(titleLabel);
        return box;
    }

    private VBox createSplitItem(String name, Label val, String hexColor) {
        val.setFont(Font.font("Outfit", FontWeight.BOLD, 18));
        val.setTextFill(Color.web(hexColor));
        
        Label header = new Label(name);
        header.setFont(Font.font("Inter", FontWeight.BOLD, 10));
        header.setTextFill(Color.web("#9CA3AF"));
        
        VBox box = new VBox(1, header, val);
        box.setAlignment(Pos.CENTER);
        box.setMinWidth(30);
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
            }
        } catch (Exception e) {
            killsLabel.setText("-");
            deathsLabel.setText("-");
            assistsLabel.setText("-");
            ratioLabel.setText("- KDA");
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
