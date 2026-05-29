package esporthub.dashboard.Admin;

import esporthub.model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.function.Consumer;

public class TeamPlayerDetailsCard extends VBox {

    private final DataStore dataStore = DataStore.getInstance();
    
    private final Label detailTitle;
    private final Label detailLine1;
    private final Label detailLine2;
    private final Label detailLine3;
    private final Label detailStatus;
    private final VBox actionsBox;

    private final Consumer<Object> onEditRequest;
    private final Runnable onDataChanged;

    public TeamPlayerDetailsCard(Consumer<Object> onEditRequest, Runnable onDataChanged) {
        this.onEditRequest = onEditRequest;
        this.onDataChanged = onDataChanged;

        this.setPadding(new Insets(15));
        this.setSpacing(12);
        this.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E5E7EB; -fx-border-width: 1px; -fx-border-radius: 6px; -fx-background-radius: 6px;");

        detailTitle = new Label("Pilih Tim atau Pemain");
        detailTitle.setFont(Font.font("Inter", FontWeight.BOLD, 15));
        detailTitle.setTextFill(Color.web("#1F2937"));

        detailLine1 = new Label("Pilih entri di tabel untuk melihat detail info");
        detailLine1.setFont(Font.font("Inter", 13));
        detailLine1.setTextFill(Color.web("#4B5563"));

        detailLine2 = new Label();
        detailLine2.setFont(Font.font("Inter", 13));
        detailLine2.setTextFill(Color.web("#4B5563"));

        detailLine3 = new Label();
        detailLine3.setFont(Font.font("Inter", 13));
        detailLine3.setTextFill(Color.web("#4B5563"));

        detailStatus = new Label();
        detailStatus.setFont(Font.font("Inter", FontWeight.BOLD, 12));

        actionsBox = new VBox(10);
        actionsBox.setAlignment(Pos.CENTER_LEFT);
        actionsBox.setPadding(new Insets(10, 0, 0, 0));

        this.getChildren().addAll(detailTitle, detailLine1, detailLine2, detailLine3, detailStatus, actionsBox);
    }

    public void showTeamDetails(Team team) {
        detailTitle.setText(team.getName());
        detailLine1.setText("ID Tim: " + team.getId());
        detailLine2.setText("Statistik Tanding: " + team.getWins() + " Menang / " + team.getLosses() + " Kalah");
        detailLine3.setText("");

        if (team.isBanned()) {
            detailStatus.setText("STATUS: BANNED");
            detailStatus.setTextFill(Color.web("#EF4444"));
        } else {
            detailStatus.setText("STATUS: AKTIF");
            detailStatus.setTextFill(Color.web("#10B981"));
        }

        actionsBox.getChildren().clear();

        Button editBtn = new Button("Edit Tim");
        setupButtonStyle(editBtn, "#5A67D8");
        editBtn.setOnAction(e -> onEditRequest.accept(team));

        Button banBtn = new Button(team.isBanned() ? "Lepas Ban" : "Ban Tim");
        setupButtonStyle(banBtn, team.isBanned() ? "#10B981" : "#EF4444");
        banBtn.setOnAction(e -> {
            team.setBanned(!team.isBanned());
            // Sync players under this team as well
            for (Player p : dataStore.getPlayers()) {
                if (p.getTeamName().equalsIgnoreCase(team.getName())) {
                    p.setBanned(team.isBanned());
                }
            }
            showTeamDetails(team);
            onDataChanged.run();
        });

        Button deleteBtn = new Button("Hapus Tim");
        setupButtonStyle(deleteBtn, "#4B5563");
        deleteBtn.setOnAction(e -> {
            dataStore.removeTeam(team);
            clearDetails();
            onDataChanged.run();
        });

        HBox btns = new HBox(8, editBtn, banBtn, deleteBtn);
        actionsBox.getChildren().add(btns);
    }

    public void showPlayerDetails(Player player) {
        detailTitle.setText(player.getName());
        detailLine1.setText("ID Pemain: " + player.getId());
        detailLine2.setText("Role: " + player.getRole());
        detailLine3.setText("Tim: " + player.getTeamName());

        if (player.isBanned()) {
            detailStatus.setText("STATUS: BANNED");
            detailStatus.setTextFill(Color.web("#EF4444"));
        } else {
            detailStatus.setText("STATUS: AKTIF");
            detailStatus.setTextFill(Color.web("#10B981"));
        }

        actionsBox.getChildren().clear();

        Button editBtn = new Button("Edit Pemain");
        setupButtonStyle(editBtn, "#5A67D8");
        editBtn.setOnAction(e -> onEditRequest.accept(player));

        Button banBtn = new Button(player.isBanned() ? "Lepas Ban" : "Ban Pemain");
        setupButtonStyle(banBtn, player.isBanned() ? "#10B981" : "#EF4444");
        banBtn.setOnAction(e -> {
            player.setBanned(!player.isBanned());
            showPlayerDetails(player);
            onDataChanged.run();
        });

        Button deleteBtn = new Button("Hapus Pemain");
        setupButtonStyle(deleteBtn, "#4B5563");
        deleteBtn.setOnAction(e -> {
            dataStore.removePlayer(player);
            clearDetails();
            onDataChanged.run();
        });

        HBox btns = new HBox(8, editBtn, banBtn, deleteBtn);
        actionsBox.getChildren().add(btns);
    }

    public void clearDetails() {
        detailTitle.setText("Pilih Tim atau Pemain");
        detailLine1.setText("Pilih entri di tabel untuk melihat detail info");
        detailLine2.setText("");
        detailLine3.setText("");
        detailStatus.setText("");
        actionsBox.getChildren().clear();
    }

    private void setupButtonStyle(Button btn, String hexColor) {
        btn.setStyle("-fx-background-color: " + hexColor + "; -fx-text-fill: #FFFFFF; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 8px 16px;");
        btn.setCursor(Cursor.HAND);
    }
}
