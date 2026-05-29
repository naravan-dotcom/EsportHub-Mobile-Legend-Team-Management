package esporthub.dashboard.player;

import esporthub.model.Player;
import javafx.geometry.Insets;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;

public class KdaChartCard extends VBox {

    private final Player player;
    private AreaChart<String, Number> areaChart;

    public KdaChartCard(Player player) {
        this.player = player;

        this.setPadding(new Insets(20));
        this.setSpacing(12);
        this.setStyle("-fx-background-color: #FFFFFF; " +
                      "-fx-background-radius: 12px; " +
                      "-fx-border-color: #F3F4F6; " +
                      "-fx-border-width: 1px; " +
                      "-fx-border-radius: 12px; " +
                      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 4);");

        Label titleLabel = new Label("Grafik Perkembangan KDA");
        titleLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 16));
        titleLabel.setTextFill(Color.web("#111827"));

        Label subtitleLabel = new Label("Performa KDA dari 6 pertandingan terakhir");
        subtitleLabel.setFont(Font.font("Inter", 11));
        subtitleLabel.setTextFill(Color.web("#6B7280"));

        VBox headerBox = new VBox(2, titleLabel, subtitleLabel);
        this.getChildren().add(headerBox);

        setupChart();
    }

    private void setupChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Pertandingan");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Rasio KDA");
        // Remove ticks border
        yAxis.setTickMarkVisible(false);
        xAxis.setTickMarkVisible(false);

        areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setLegendVisible(false);
        areaChart.setTitle(null);
        areaChart.setCreateSymbols(true);
        areaChart.setPrefHeight(220);

        // Populate data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        List<Double> timeline = player.getKdaTimeline();
        for (int i = 0; i < timeline.size(); i++) {
            series.getData().add(new XYChart.Data<>("M" + (i + 1), timeline.get(i)));
        }

        areaChart.getData().add(series);

        // Styling the chart elements using lookup or direct stylesheets if needed
        // For standard JavaFX, applying styling inline:
        areaChart.setStyle("-fx-background-color: transparent;");
        areaChart.setHorizontalGridLinesVisible(true);
        areaChart.setVerticalGridLinesVisible(false);
        
        // Load custom styling for AreaChart via stylesheet or lookup
        // We will inject a custom inline stylesheet string to customize the area chart stroke and gradient fill
        String chartCss = 
            ".chart-plot-background { -fx-background-color: transparent; }\n" +
            ".chart-vertical-grid-lines { -fx-stroke: transparent; }\n" +
            ".chart-horizontal-grid-lines { -fx-stroke: #F3F4F6; }\n" +
            ".chart-series-area-line { -fx-stroke: #4F46E5; -fx-stroke-width: 3px; }\n" +
            ".chart-series-area-fill { -fx-fill: linear-gradient(to bottom, rgba(79, 70, 229, 0.3) 0%, rgba(79, 70, 229, 0.0) 100%); }\n" +
            ".chart-line-symbol { -fx-background-color: #4F46E5, #FFFFFF; -fx-background-insets: 0, 2; -fx-background-radius: 5px; -fx-padding: 5px; }\n" +
            ".axis { -fx-tick-label-fill: #6B7280; -fx-font-family: 'Inter'; -fx-font-size: 11px; }\n" +
            ".axis-label { -fx-text-fill: #4B5563; -fx-font-family: 'Inter'; -fx-font-size: 11px; -fx-font-weight: bold; }";
            
        try {
            java.io.File cssFile = new java.io.File("chart-styles.css");
            java.nio.file.Files.write(cssFile.toPath(), chartCss.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            areaChart.getStylesheets().add(cssFile.toURI().toString());
        } catch (Exception e) {
            System.err.println("Gagal memuat stylesheet grafik: " + e.getMessage());
        }

        this.getChildren().add(areaChart);
    }

    public void refresh() {
        areaChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        List<Double> timeline = player.getKdaTimeline();
        for (int i = 0; i < timeline.size(); i++) {
            series.getData().add(new XYChart.Data<>("M" + (i + 1), timeline.get(i)));
        }
        areaChart.getData().add(series);
    }
}
