package esporthub.login;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class LeftPanel extends VBox {

    // SVG paths for icons
    public static final String GAMEPAD_SVG = 
        "M21.5,6H2.5A2.5,2.5,0,0,0,0,8.5v7A2.5,2.5,0,0,0,2.5,18h19a2.5,2.5,0,0,0,2.5-2.5v-7A2.5,2.5,0,0,0,21.5,6ZM8.5,13H6.5v2h-2V13H2.5v-2h2V9h2v2h2Zm11-1.5a1.5,1.5,0,1,1,1.5-1.5A1.5,1.5,0,0,1,19.5,11.5Zm-3,3a1.5,1.5,0,1,1,1.5-1.5A1.5,1.5,0,0,1,16.5,14.5Z";
    
    public static final String TROPHY_SVG = 
        "M18 2H6v2H2v3c0 3.24 2.13 5.96 5 6.74v1.76H5v2h14v-2h-2v-1.76c2.87-.78 5-3.5 5-6.74V4h-4V2zM4 6V5h2v3c0 2.21-1.79 4-4 4v-6zm16 6c-2.21 0-4-1.79-4-4V5h2v1c0 2.21 1.79 4 4 4z";

    private final VBox leftContentContainer;
    private final SVGPath leftIcon;
    private final Label leftSubtitle;

    public LeftPanel() {
        // Set layout rules for LeftPanel
        this.setPrefWidth(420);
        this.setMinWidth(420);
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(40, 30, 40, 30));

        // Deep rich purple/indigo color palette matching the desktop screenshot
        LinearGradient gradient = new LinearGradient(
            0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#2b2654")), // Rich Indigo/Purple from screenshot
            new Stop(0.5, Color.web("#221e42")), // Dark Slate/Indigo
            new Stop(1, Color.web("#171430")) // Deep Navy/Slate
        );
        this.setBackground(new Background(new BackgroundFill(gradient, new CornerRadii(15, 0, 0, 15, false), Insets.EMPTY)));

        // Sub-container for content
        leftContentContainer = new VBox(25);
        leftContentContainer.setAlignment(Pos.CENTER);

        // Icon SVG Setup
        leftIcon = new SVGPath();
        leftIcon.setContent(GAMEPAD_SVG);
        leftIcon.setFill(Color.BLACK);
        leftIcon.setOpacity(0.85);
        leftIcon.setScaleX(2.8);
        leftIcon.setScaleY(2.8);

        StackPane iconContainer = new StackPane(leftIcon);
        iconContainer.setPadding(new Insets(20, 0, 20, 0));

        // Brand Title
        Label brandTitle = new Label("EsportHub");
        brandTitle.setTextFill(Color.WHITE);
        brandTitle.setFont(Font.font("Outfit", FontWeight.BOLD, 36));

        // Glowing title drop shadow
        DropShadow titleGlow = new DropShadow();
        titleGlow.setRadius(12);
        titleGlow.setColor(Color.web("#a78bfa", 0.6));
        brandTitle.setEffect(titleGlow);

        // Subtitle
        leftSubtitle = new Label("Platform Manajemen\nTurnamen Esports #1");
        leftSubtitle.setTextFill(Color.web("#b4acc4"));
        leftSubtitle.setFont(Font.font("Inter", FontWeight.MEDIUM, 14));
        leftSubtitle.setTextAlignment(TextAlignment.CENTER);
        leftSubtitle.setAlignment(Pos.CENTER);
        leftSubtitle.setMinHeight(40);

        leftContentContainer.getChildren().addAll(iconContainer, brandTitle, leftSubtitle);

        // Stats Cards Row
        HBox statsContainer = new HBox(12);
        statsContainer.setAlignment(Pos.CENTER);
        statsContainer.setPadding(new Insets(30, 0, 35, 0));

        Pane card1 = createStatsCard("500+", "Pemain");
        Pane card2 = createStatsCard("50+", "Tim");
        Pane card3 = createStatsCard("120+", "Turnamen");
        statsContainer.getChildren().addAll(card1, card2, card3);

        // Footer Text
        Label footerLabel = new Label("© 2026 EsportHub. All rights reserved.");
        footerLabel.setTextFill(Color.web("#696085"));
        footerLabel.setFont(Font.font("Inter", 11));

        // Add flex spacers to separate components vertically
        Region spacer1 = new Region();
        VBox.setVgrow(spacer1, Priority.ALWAYS);

        Region spacer2 = new Region();
        VBox.setVgrow(spacer2, Priority.ALWAYS);

        this.getChildren().addAll(leftContentContainer, spacer1, statsContainer, spacer2, footerLabel);
    }

    /**
     * Updates Left Panel vectors and titles dynamically.
     */
    public void updateContent(String subtitleText, String svgPath, boolean isTrophy) {
        leftSubtitle.setText(subtitleText);
        leftIcon.setContent(svgPath);
        if (isTrophy) {
            leftIcon.setScaleX(2.5);
            leftIcon.setScaleY(2.5);
        } else {
            leftIcon.setScaleX(2.8);
            leftIcon.setScaleY(2.8);
        }
    }

    /**
     * Gets the content container (for fade animations).
     */
    public VBox getContentContainer() {
        return leftContentContainer;
    }

    /**
     * Helper to render glassmorphic metric cards.
     */
    private Pane createStatsCard(String number, String labelText) {
        VBox card = new VBox(4);
        card.setPrefSize(96, 84);
        card.setMinSize(96, 84);
        card.setMaxSize(96, 84);
        card.setAlignment(Pos.CENTER);

        // Glassmorphic translucent styling
        card.setBackground(new Background(new BackgroundFill(Color.web("#ffffff", 0.05), new CornerRadii(12), Insets.EMPTY)));
        card.setBorder(new Border(new BorderStroke(
            Color.web("#ffffff", 0.08), BorderStrokeStyle.SOLID, new CornerRadii(12), new BorderWidths(1)
        )));

        Label numLabel = new Label(number);
        numLabel.setTextFill(Color.web("#c084fc")); // Soft lilac highlight
        numLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 17));

        Label textLabel = new Label(labelText);
        textLabel.setTextFill(Color.web("#b4acc4", 0.8));
        textLabel.setFont(Font.font("Inter", 11));

        card.getChildren().addAll(numLabel, textLabel);
        return card;
    }
}
