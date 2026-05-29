package esporthub.dashboard.player;

import esporthub.model.Player;
import javafx.application.Platform;
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

        // Apply inline styling for the chart
        areaChart.setStyle("-fx-background-color: transparent;");
        areaChart.setHorizontalGridLinesVisible(true);
        areaChart.setVerticalGridLinesVisible(false);
        
        // Apply programmatic styling via node lookup after the chart is rendered
        applyChartStyling();

        this.getChildren().add(areaChart);
    }

    /**
     * Applies styling to chart sub-nodes using Platform.runLater()
     * so that nodes are available for lookup after the CSS pass.
     * This replaces the external CSS file approach.
     */
    private void applyChartStyling() {
        Platform.runLater(() -> {
            // Style the plot background
            javafx.scene.Node plotBg = areaChart.lookup(".chart-plot-background");
            if (plotBg != null) {
                plotBg.setStyle("-fx-background-color: transparent;");
            }

            // Style the vertical grid lines
            javafx.scene.Node vertLines = areaChart.lookup(".chart-vertical-grid-lines");
            if (vertLines != null) {
                vertLines.setStyle("-fx-stroke: transparent;");
            }

            // Style the horizontal grid lines
            javafx.scene.Node horizLines = areaChart.lookup(".chart-horizontal-grid-lines");
            if (horizLines != null) {
                horizLines.setStyle("-fx-stroke: #F3F4F6;");
            }

            // Style the series area line
            javafx.scene.Node areaLine = areaChart.lookup(".chart-series-area-line");
            if (areaLine != null) {
                areaLine.setStyle("-fx-stroke: #4F46E5; -fx-stroke-width: 3px;");
            }
            javafx.scene.Node seriesLine = areaChart.lookup(".chart-series-line");
            if (seriesLine != null) {
                seriesLine.setStyle("-fx-stroke: #4F46E5; -fx-stroke-width: 3px;");
            }

            // Style the series area fill
            javafx.scene.Node areaFill = areaChart.lookup(".chart-series-area-fill");
            if (areaFill != null) {
                areaFill.setStyle("-fx-fill: linear-gradient(to bottom, rgba(79, 70, 229, 0.3) 0%, rgba(79, 70, 229, 0.0) 100%);");
            }

            // Style data point symbols
            for (javafx.scene.Node symbol : areaChart.lookupAll(".chart-line-symbol")) {
                symbol.setStyle("-fx-background-color: #4F46E5, #FFFFFF; -fx-background-insets: 0, 2; -fx-background-radius: 5px; -fx-padding: 5px;");
            }

            // Style axes
            for (javafx.scene.Node axis : areaChart.lookupAll(".axis")) {
                axis.setStyle("-fx-tick-label-fill: #6B7280; -fx-font-family: 'Inter'; -fx-font-size: 11px;");
            }

            // Style axis labels
            for (javafx.scene.Node axisLabel : areaChart.lookupAll(".axis-label")) {
                axisLabel.setStyle("-fx-text-fill: #4B5563; -fx-font-family: 'Inter'; -fx-font-size: 11px; -fx-font-weight: bold;");
            }
        });
    }

    public void refresh() {
        areaChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        List<Double> timeline = player.getKdaTimeline();
        for (int i = 0; i < timeline.size(); i++) {
            series.getData().add(new XYChart.Data<>("M" + (i + 1), timeline.get(i)));
        }
        areaChart.getData().add(series);
        applyChartStyling();
    }
}
