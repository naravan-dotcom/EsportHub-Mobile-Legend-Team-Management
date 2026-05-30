package esporthub.dashboard.player.team;

import esporthub.model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Optional;

public class TeamRoomHeader extends HBox {

    private final Player currentPlayer;
    private final Team team;
    private final Runnable onDataChanged;

    private Label teamNameLabel;
    private Label captainLabel;
    private Label recordLabel;

    public TeamRoomHeader(Player currentPlayer, Team team, Runnable onDataChanged) {
        this.currentPlayer = currentPlayer;
        this.team = team;
        this.onDataChanged = onDataChanged;

        this.setPadding(new Insets(24));
        this.setSpacing(24);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setStyle("-fx-background-color: linear-gradient(to right, #1E1B4B, #312E81); " +
                      "-fx-background-radius: 16px; " +
                      "-fx-effect: dropshadow(three-pass-box, rgba(49, 46, 129, 0.2), 20, 0, 0, 10);");

        setupUI();
    }

    private void setupUI() {
        this.getChildren().clear();

        // 1. Team Icon/Avatar
        StackPane teamAvatar = new StackPane();
        teamAvatar.setPrefSize(70, 70);
        teamAvatar.setMaxSize(70, 70);
        teamAvatar.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-background-radius: 16px; -fx-border-color: rgba(255, 255, 255, 0.2); -fx-border-width: 1px; -fx-border-radius: 16px;");
        Label letter = new Label(team.getName().substring(0, 1).toUpperCase());
        letter.setFont(Font.font("Outfit", FontWeight.BOLD, 32));
        letter.setStyle("-fx-text-fill: #E0E7FF; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 32px;");
        teamAvatar.getChildren().add(letter);

        // 2. Team Info Details
        VBox infoBox = new VBox(6);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        HBox nameRow = new HBox(12);
        nameRow.setAlignment(Pos.CENTER_LEFT);

        teamNameLabel = new Label(team.getName());
        teamNameLabel.setFont(Font.font("Outfit", FontWeight.BOLD, 24));
        teamNameLabel.setStyle("-fx-text-fill: #FFFFFF; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 24px;");

        nameRow.getChildren().add(teamNameLabel);

        // Captain Edit Capability
        boolean isCaptain = team.getCaptainName().equalsIgnoreCase(currentPlayer.getName());
        if (isCaptain) {
            Button editBtn = new Button("Edit Nama");
            editBtn.setFont(Font.font("Inter", FontWeight.BOLD, 10));
            editBtn.setStyle("-fx-background-color: rgba(255,255,255,0.15); -fx-text-fill: #F1F5F9; -fx-padding: 4px 8px; -fx-background-radius: 6px; -fx-font-weight: bold;");
            editBtn.setCursor(Cursor.HAND);
            editBtn.setOnMouseEntered(e -> editBtn.setStyle("-fx-background-color: rgba(255,255,255,0.25); -fx-text-fill: #FFFFFF; -fx-padding: 4px 8px; -fx-background-radius: 6px; -fx-font-weight: bold;"));
            editBtn.setOnMouseExited(e -> editBtn.setStyle("-fx-background-color: rgba(255,255,255,0.15); -fx-text-fill: #F1F5F9; -fx-padding: 4px 8px; -fx-background-radius: 6px; -fx-font-weight: bold;"));
            editBtn.setOnAction(e -> handleEditTeamName());

            Button settingBtn = new Button("Pengaturan Tim");
            settingBtn.setFont(Font.font("Inter", FontWeight.BOLD, 10));
            settingBtn.setStyle("-fx-background-color: rgba(255,255,255,0.15); -fx-text-fill: #F1F5F9; -fx-padding: 4px 8px; -fx-background-radius: 6px; -fx-font-weight: bold;");
            settingBtn.setCursor(Cursor.HAND);
            settingBtn.setOnMouseEntered(e -> settingBtn.setStyle("-fx-background-color: rgba(255,255,255,0.25); -fx-text-fill: #FFFFFF; -fx-padding: 4px 8px; -fx-background-radius: 6px; -fx-font-weight: bold;"));
            settingBtn.setOnMouseExited(e -> settingBtn.setStyle("-fx-background-color: rgba(255,255,255,0.15); -fx-text-fill: #F1F5F9; -fx-padding: 4px 8px; -fx-background-radius: 6px; -fx-font-weight: bold;"));
            settingBtn.setOnAction(e -> handleEditTeamSettings());

            nameRow.getChildren().addAll(editBtn, settingBtn);
        }

        captainLabel = new Label("Ketua: " + (team.getCaptainName().isEmpty() ? "-" : team.getCaptainName()));
        captainLabel.setFont(Font.font("Inter", 13));
        captainLabel.setStyle("-fx-text-fill: #C7D2FE; -fx-font-family: 'Inter'; -fx-font-size: 13px;");

        recordLabel = new Label("Rekor: " + team.getWins() + " W - " + team.getLosses() + " L");
        recordLabel.setFont(Font.font("Inter", FontWeight.BOLD, 12));
        recordLabel.setStyle("-fx-text-fill: #34D399; -fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 12px;");

        Label privacyLabel = new Label("Akses: " + team.getPrivacyType());
        privacyLabel.setFont(Font.font("Inter", FontWeight.BOLD, 12));
        if (team.getPrivacyType().equalsIgnoreCase("Public")) {
            privacyLabel.setStyle("-fx-text-fill: #38BDF8; -fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 12px;");
        } else {
            privacyLabel.setStyle("-fx-text-fill: #FB7185; -fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 12px;");
        }

        HBox metaRow = new HBox(16, captainLabel, recordLabel, privacyLabel);
        metaRow.setAlignment(Pos.CENTER_LEFT);

        infoBox.getChildren().addAll(nameRow, metaRow);

        // 3. Leave Team Button
        Button leaveBtn = new Button("Keluar Tim");
        leaveBtn.setFont(Font.font("Inter", FontWeight.BOLD, 13));
        leaveBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #F87171; -fx-border-color: #F87171; -fx-border-radius: 8px; -fx-border-width: 1px; -fx-padding: 10px 16px; -fx-font-weight: bold;");
        leaveBtn.setCursor(Cursor.HAND);
        leaveBtn.setOnMouseEntered(e -> leaveBtn.setStyle("-fx-background-color: rgba(239, 68, 68, 0.1); -fx-text-fill: #F87171; -fx-border-color: #F87171; -fx-border-radius: 8px; -fx-border-width: 1px; -fx-padding: 10px 16px; -fx-font-weight: bold;"));
        leaveBtn.setOnMouseExited(e -> leaveBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #F87171; -fx-border-color: #F87171; -fx-border-radius: 8px; -fx-border-width: 1px; -fx-padding: 10px 16px; -fx-font-weight: bold;"));
        leaveBtn.setOnAction(e -> handleLeaveTeam());

        this.getChildren().addAll(teamAvatar, infoBox, leaveBtn);
    }

    private void handleEditTeamSettings() {
        java.util.List<String> choices = new java.util.ArrayList<>();
        choices.add("Public");
        choices.add("Permission");

        ChoiceDialog<String> dialog = new ChoiceDialog<>(team.getPrivacyType(), choices);
        dialog.setTitle("Pengaturan Privasi Tim");
        dialog.setHeaderText("Pilih tipe pendaftaran untuk tim \"" + team.getName() + "\":");
        dialog.setContentText("Tipe Pendaftaran:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(type -> {
            team.setPrivacyType(type);
            if (type.equals("Public")) {
                team.getJoinRequests().clear();
            }
            showAlert("Sukses", "Tipe pendaftaran tim berhasil diubah menjadi: " + type, Alert.AlertType.INFORMATION);
            setupUI();
            onDataChanged.run();
        });
    }

    private void handleEditTeamName() {
        TextInputDialog dialog = new TextInputDialog(team.getName());
        dialog.setTitle("Edit Nama Tim");
        dialog.setHeaderText("Masukkan nama baru untuk tim Anda:");
        dialog.setContentText("Nama Tim:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            String cleanName = newName.trim();
            if (cleanName.isEmpty()) {
                showAlert("Error", "Nama tim tidak boleh kosong!", Alert.AlertType.WARNING);
                return;
            }

            // Sync with all player members
            String oldName = team.getName();
            
            // Check if another team already uses this name
            for (Team t : DataStore.getInstance().getTeams()) {
                if (t != team && t.getName().equalsIgnoreCase(cleanName)) {
                    showAlert("Error", "Nama tim sudah terpakai!", Alert.AlertType.ERROR);
                    return;
                }
            }

            // Update team model
            team.setName(cleanName);

            // Update all players belonging to this team
            for (Player p : DataStore.getInstance().getPlayers()) {
                if (p.getTeamName().equalsIgnoreCase(oldName)) {
                    p.setTeamName(cleanName);
                }
            }

            // Sync matches that reference this team's name
            // Wait, matches store team objects by reference, so no need to rename match properties, 
            // but we must make sure all displays refresh.
            
            showAlert("Sukses", "Nama tim berhasil diperbarui!", Alert.AlertType.INFORMATION);
            setupUI();
            onDataChanged.run();
        });
    }

    private void handleLeaveTeam() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, 
            "Apakah Anda yakin ingin keluar dari tim \"" + team.getName() + "\"?", 
            ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Keluar Tim");
        confirm.setHeaderText(null);
        
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            String teamName = currentPlayer.getTeamName();
            currentPlayer.setTeamName("None");

            // If player was captain
            if (team.getCaptainName().equalsIgnoreCase(currentPlayer.getName())) {
                // Find next captain
                Player newCaptain = null;
                for (Player p : DataStore.getInstance().getPlayers()) {
                    if (p.getTeamName().equalsIgnoreCase(teamName) && !p.getName().equalsIgnoreCase(currentPlayer.getName())) {
                        newCaptain = p;
                        break;
                    }
                }
                
                if (newCaptain != null) {
                    team.setCaptainName(newCaptain.getName());
                    showAlert("Informasi", "Anda telah keluar dari tim. Kepemimpinan tim dialihkan kepada " + newCaptain.getName() + ".", Alert.AlertType.INFORMATION);
                } else {
                    // Delete team
                    DataStore.getInstance().removeTeam(team);
                    showAlert("Informasi", "Anda telah keluar dari tim. Karena tidak ada anggota lain, tim \"" + teamName + "\" dibubarkan.", Alert.AlertType.INFORMATION);
                }
            } else {
                showAlert("Sukses", "Anda berhasil keluar dari tim.", Alert.AlertType.INFORMATION);
            }

            onDataChanged.run();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
