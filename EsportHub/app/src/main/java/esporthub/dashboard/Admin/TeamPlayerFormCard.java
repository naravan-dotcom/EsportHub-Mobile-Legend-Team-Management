package esporthub.dashboard.Admin;

import esporthub.model.*;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TeamPlayerFormCard extends VBox {

    private final DataStore dataStore = DataStore.getInstance();

    private final Label formTitle;
    private final TextField nameField;
    private final ComboBox<String> secondaryField; // Role selector for player
    private final ComboBox<Team> teamSelector; // Team selector for player
    private final Button submitFormBtn;
    private final Button cancelEditBtn;

    private final Runnable onDataChanged;
    
    private Team selectedTeam = null;
    private Player selectedPlayer = null;
    private boolean isEditing = false;
    private boolean isPlayerMode = false;

    public TeamPlayerFormCard(Runnable onDataChanged) {
        this.onDataChanged = onDataChanged;

        this.setPadding(new Insets(15));
        this.setSpacing(12);
        this.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E5E7EB; -fx-border-width: 1px; -fx-border-radius: 6px; -fx-background-radius: 6px;");

        formTitle = new Label("Tambah Tim Baru");
        formTitle.setFont(Font.font("Inter", FontWeight.BOLD, 14));
        formTitle.setTextFill(Color.web("#1F2937"));

        nameField = new TextField();
        nameField.setPromptText("Nama Tim");
        setupFieldStyle(nameField);

        secondaryField = new ComboBox<>();
        secondaryField.setMaxWidth(Double.MAX_VALUE);
        setupComboStyle(secondaryField);
        secondaryField.setVisible(false);
        secondaryField.setManaged(false);

        teamSelector = new ComboBox<>();
        teamSelector.setMaxWidth(Double.MAX_VALUE);
        setupComboStyle(teamSelector);
        teamSelector.setItems(dataStore.getTeams());
        teamSelector.setVisible(false);
        teamSelector.setManaged(false);

        submitFormBtn = new Button("Tambah Tim");
        submitFormBtn.setMaxWidth(Double.MAX_VALUE);
        setupButtonStyle(submitFormBtn, "#5A67D8");
        submitFormBtn.setOnAction(e -> handleFormSubmit());

        cancelEditBtn = new Button("Batal");
        cancelEditBtn.setMaxWidth(Double.MAX_VALUE);
        setupButtonStyle(cancelEditBtn, "#6B7280");
        cancelEditBtn.setVisible(false);
        cancelEditBtn.setManaged(false);
        cancelEditBtn.setOnAction(e -> exitEditMode());

        this.getChildren().addAll(formTitle, nameField, secondaryField, teamSelector, submitFormBtn, cancelEditBtn);
    }

    public void showTeamMode() {
        if (isEditing) exitEditMode();
        isPlayerMode = false;
        formTitle.setText("Tambah Tim Baru");
        nameField.setPromptText("Nama Tim");
        nameField.clear();

        secondaryField.setVisible(false);
        secondaryField.setManaged(false);
        teamSelector.setVisible(false);
        teamSelector.setManaged(false);

        submitFormBtn.setText("Tambah Tim");
    }

    public void showPlayerMode() {
        if (isEditing) exitEditMode();
        isPlayerMode = true;
        formTitle.setText("Tambah Pemain Baru");
        nameField.setPromptText("Nama Pemain");
        nameField.clear();

        secondaryField.setVisible(true);
        secondaryField.setManaged(true);
        secondaryField.setPromptText("Pilih Role");
        secondaryField.getItems().setAll("Jungler", "Gold Laner", "EXP Laner", "Mid Laner", "Roamer");
        secondaryField.setValue(null);

        teamSelector.setVisible(true);
        teamSelector.setManaged(true);
        teamSelector.setPromptText("Pilih Tim");
        teamSelector.setValue(null);

        submitFormBtn.setText("Tambah Pemain");
    }

    public void enterEditMode(Team team) {
        isEditing = true;
        selectedTeam = team;
        selectedPlayer = null;
        isPlayerMode = false;
        
        formTitle.setText("Edit Tim: " + team.getName());
        nameField.setText(team.getName());

        secondaryField.setVisible(false);
        secondaryField.setManaged(false);
        teamSelector.setVisible(false);
        teamSelector.setManaged(false);

        submitFormBtn.setText("Simpan Perubahan");
        cancelEditBtn.setVisible(true);
        cancelEditBtn.setManaged(true);
    }

    public void enterEditMode(Player player) {
        isEditing = true;
        selectedPlayer = player;
        selectedTeam = null;
        isPlayerMode = true;
        
        formTitle.setText("Edit Pemain: " + player.getName());
        nameField.setText(player.getName());

        secondaryField.setVisible(true);
        secondaryField.setManaged(true);
        secondaryField.getItems().setAll("Jungler", "Gold Laner", "EXP Laner", "Mid Laner", "Roamer");
        secondaryField.setValue(player.getRole());

        teamSelector.setVisible(true);
        teamSelector.setManaged(true);
        
        // Find team
        Team currentTeam = null;
        for (Team t : dataStore.getTeams()) {
            if (t.getName().equalsIgnoreCase(player.getTeamName())) {
                currentTeam = t;
                break;
            }
        }
        teamSelector.setValue(currentTeam);

        submitFormBtn.setText("Simpan Perubahan");
        cancelEditBtn.setVisible(true);
        cancelEditBtn.setManaged(true);
    }

    public void exitEditMode() {
        isEditing = false;
        selectedTeam = null;
        selectedPlayer = null;
        cancelEditBtn.setVisible(false);
        cancelEditBtn.setManaged(false);

        if (isPlayerMode) {
            showPlayerMode();
        } else {
            showTeamMode();
        }
    }

    private void handleFormSubmit() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Nama tidak boleh kosong!", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (isEditing) {
            if (selectedTeam != null) {
                String oldName = selectedTeam.getName();
                selectedTeam.setName(name);
                for (Player p : dataStore.getPlayers()) {
                    if (p.getTeamName().equalsIgnoreCase(oldName)) {
                        p.setTeamName(name);
                    }
                }
            } else if (selectedPlayer != null) {
                String role = secondaryField.getValue();
                Team team = teamSelector.getValue();
                if (role == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Silakan pilih Role pemain!", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
                selectedPlayer.setName(name);
                selectedPlayer.setRole(role);
                selectedPlayer.setTeamName(team != null ? team.getName() : "None");
                if (team != null && team.isBanned()) {
                    selectedPlayer.setBanned(true);
                }
            }
            exitEditMode();
        } else {
            // Add Mode
            if (isPlayerMode) {
                String role = secondaryField.getValue();
                Team team = teamSelector.getValue();
                if (role == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Silakan pilih Role pemain!", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
                String id = "P" + (dataStore.getPlayers().size() + 1);
                boolean teamBanned = (team != null && team.isBanned());
                Player player = new Player(id, name, role, team != null ? team.getName() : "None", teamBanned);
                dataStore.addPlayer(player);
                nameField.clear();
                secondaryField.setValue(null);
                teamSelector.setValue(null);
            } else {
                String id = "T" + (dataStore.getTeams().size() + 1);
                Team team = new Team(id, name, 0, 0, false);
                dataStore.addTeam(team);
                nameField.clear();
            }
        }
        onDataChanged.run();
    }

    private void setupFieldStyle(TextField field) {
        field.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E5E7EB; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-padding: 8px 12px; -fx-font-size: 13px;");
    }

    private void setupComboStyle(ComboBox<?> combo) {
        combo.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E5E7EB; -fx-border-radius: 6px; -fx-background-radius: 6px; -fx-font-size: 13px;");
    }

    private void setupButtonStyle(Button btn, String hexColor) {
        btn.setStyle("-fx-background-color: " + hexColor + "; -fx-text-fill: #FFFFFF; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 8px 16px;");
        btn.setCursor(Cursor.HAND);
    }
}
