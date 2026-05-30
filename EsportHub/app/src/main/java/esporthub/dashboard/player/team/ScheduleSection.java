package esporthub.dashboard.player.team;

import esporthub.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;

import java.util.Optional;

public class ScheduleSection extends HBox {

    private final Player currentPlayer;
    private final Team team;
    private final Runnable onDataChanged;

    private TableView<TrainingSchedule> scheduleTable;
    private final ObservableList<TrainingSchedule> scheduleList = FXCollections.observableArrayList();

    // Form inputs (only for captain)
    private TextField titleField;
    private TextField dateTimeField;
    private TextArea notesArea;

    public ScheduleSection(Player currentPlayer, Team team, Runnable onDataChanged) {
        this.currentPlayer = currentPlayer;
        this.team = team;
        this.onDataChanged = onDataChanged;

        this.setSpacing(24);
        this.setPadding(new Insets(10, 0, 0, 0));
        HBox.setHgrow(this, Priority.ALWAYS);

        // 1. Table Container (Left or Full width)
        VBox tableContainer = new VBox(16);
        HBox.setHgrow(tableContainer, Priority.ALWAYS);

        Label sectionTitle = new Label("Jadwal Latihan Tim");
        sectionTitle.setFont(Font.font("Outfit", FontWeight.BOLD, 18));
        sectionTitle.setStyle("-fx-text-fill: #0F172A; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 18px;");

        setupTable();
        refreshList();

        tableContainer.getChildren().addAll(sectionTitle, scheduleTable);
        this.getChildren().add(tableContainer);

        // 2. Captain Add Form (Right side)
        boolean isCaptain = team.getCaptainName().equalsIgnoreCase(currentPlayer.getName());
        if (isCaptain) {
            VBox formContainer = setupAddForm();
            this.getChildren().add(formContainer);
        }
    }

    public void refreshList() {
        scheduleList.clear();
        String tName = team.getName();
        for (TrainingSchedule s : DataStore.getInstance().getTrainingSchedules()) {
            if (s.getTeamName().equalsIgnoreCase(tName)) {
                scheduleList.add(s);
            }
        }
    }

    private void setupTable() {
        scheduleTable = new TableView<>();
        scheduleTable.setItems(scheduleList);
        scheduleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        scheduleTable.setPrefHeight(320);
        scheduleTable.setStyle(
            "-fx-background-color: #FFFFFF; " +
            "-fx-border-color: #E2E8F0; " +
            "-fx-border-radius: 12px; " +
            "-fx-background-radius: 12px; " +
            "-fx-border-width: 1px; " +
            "-fx-padding: 8px; " +
            "-fx-control-inner-background: #FFFFFF; " +
            "-fx-table-cell-border-color: #F1F5F9; " +
            "-fx-table-header-border-color: transparent;"
        );

        TableColumn<TrainingSchedule, String> titleCol = new TableColumn<>("Materi Latihan");
        titleCol.setCellValueFactory(f -> f.getValue().titleProperty());

        TableColumn<TrainingSchedule, String> dateCol = new TableColumn<>("Tanggal & Waktu");
        dateCol.setCellValueFactory(f -> f.getValue().dateTimeProperty());
        dateCol.setPrefWidth(150);

        TableColumn<TrainingSchedule, String> notesCol = new TableColumn<>("Catatan");
        notesCol.setCellValueFactory(f -> f.getValue().notesProperty());
        notesCol.setPrefWidth(220);

        scheduleTable.getColumns().addAll(titleCol, dateCol, notesCol);

        // Action column for Deleting (only visible to Captain)
        boolean isCaptain = team.getCaptainName().equalsIgnoreCase(currentPlayer.getName());
        if (isCaptain) {
            TableColumn<TrainingSchedule, Void> actionCol = new TableColumn<>("Aksi");
            actionCol.setPrefWidth(80);

            Callback<TableColumn<TrainingSchedule, Void>, TableCell<TrainingSchedule, Void>> cellFactory = new Callback<>() {
                @Override
                public TableCell<TrainingSchedule, Void> call(final TableColumn<TrainingSchedule, Void> param) {
                    return new TableCell<>() {
                        private final Button deleteBtn = new Button("Hapus");
                        {
                            deleteBtn.setFont(Font.font("Inter", FontWeight.BOLD, 11));
                            deleteBtn.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #EF4444; -fx-padding: 4px 8px; -fx-background-radius: 4px; -fx-font-weight: bold;");
                            deleteBtn.setCursor(Cursor.HAND);
                            deleteBtn.setOnMouseEntered(e -> deleteBtn.setStyle("-fx-background-color: #FCA5A5; -fx-text-fill: #B91C1C; -fx-padding: 4px 8px; -fx-background-radius: 4px; -fx-font-weight: bold;"));
                            deleteBtn.setOnMouseExited(e -> deleteBtn.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #EF4444; -fx-padding: 4px 8px; -fx-background-radius: 4px; -fx-font-weight: bold;"));
                            deleteBtn.setOnAction(event -> {
                                TrainingSchedule s = getTableView().getItems().get(getIndex());
                                handleDelete(s);
                            });
                        }

                        @Override
                        protected void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(deleteBtn);
                            }
                        }
                    };
                }
            };
            actionCol.setCellFactory(cellFactory);
            scheduleTable.getColumns().add(actionCol);
        }
    }

    private VBox setupAddForm() {
        VBox form = new VBox(14);
        form.setPrefWidth(280);
        form.setMinWidth(280);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: #FFFFFF; " +
                      "-fx-border-color: #E2E8F0; " +
                      "-fx-border-width: 1px; " +
                      "-fx-border-radius: 12px; " +
                      "-fx-background-radius: 12px; " +
                      "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.02), 10, 0, 0, 5);");

        Label formTitle = new Label("Atur Jadwal Baru");
        formTitle.setFont(Font.font("Outfit", FontWeight.BOLD, 15));
        formTitle.setStyle("-fx-text-fill: #0F172A; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 15px;");

        titleField = new TextField();
        setupFieldStyle(titleField, "Materi Latihan (e.g. Scrim)");

        dateTimeField = new TextField();
        setupFieldStyle(dateTimeField, "Tanggal & Jam (YYYY-MM-DD HH:MM)");

        notesArea = new TextArea();
        notesArea.setPrefHeight(70);
        notesArea.setPromptText("Catatan latihan...");
        notesArea.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CBD5E1; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 8px; -fx-font-size: 13px; -fx-text-fill: #0F172A; -fx-font-family: 'Inter';");

        Button submitBtn = new Button("Tambah Jadwal");
        submitBtn.setMaxWidth(Double.MAX_VALUE);
        submitBtn.setFont(Font.font("Inter", FontWeight.BOLD, 13));
        submitBtn.setStyle("-fx-background-color: #4F46E5; -fx-text-fill: #FFFFFF; -fx-background-radius: 8px; -fx-padding: 10px 14px; -fx-font-weight: bold;");
        submitBtn.setCursor(Cursor.HAND);
        submitBtn.setOnMouseEntered(e -> submitBtn.setStyle("-fx-background-color: #4338CA; -fx-text-fill: #FFFFFF; -fx-background-radius: 8px; -fx-padding: 10px 14px; -fx-font-weight: bold;"));
        submitBtn.setOnMouseExited(e -> submitBtn.setStyle("-fx-background-color: #4F46E5; -fx-text-fill: #FFFFFF; -fx-background-radius: 8px; -fx-padding: 10px 14px; -fx-font-weight: bold;"));
        submitBtn.setOnAction(e -> handleAddSchedule());

        form.getChildren().addAll(formTitle, 
            createFieldLabel("Materi Latihan"), titleField, 
            createFieldLabel("Jadwal Latihan"), dateTimeField, 
            createFieldLabel("Catatan Tambahan"), notesArea, 
            submitBtn);
        return form;
    }

    private Label createFieldLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("Inter", FontWeight.SEMI_BOLD, 11));
        lbl.setStyle("-fx-text-fill: #475569; -fx-font-family: 'Inter'; -fx-font-weight: bold; -fx-font-size: 11px;");
        return lbl;
    }

    private void setupFieldStyle(TextField field, String prompt) {
        field.setPromptText(prompt);
        field.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CBD5E1; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 8px 12px; -fx-font-size: 13px; -fx-text-fill: #0F172A; -fx-font-family: 'Inter';");
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #4F46E5; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 8px 12px; -fx-font-size: 13px; -fx-text-fill: #0F172A; -fx-font-family: 'Inter';");
            } else {
                field.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CBD5E1; -fx-border-radius: 8px; -fx-background-radius: 8px; -fx-padding: 8px 12px; -fx-font-size: 13px; -fx-text-fill: #0F172A; -fx-font-family: 'Inter';");
            }
        });
    }

    private boolean isValidDateTimeFormat(String dt) {
        if (dt == null || !dt.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$")) {
            return false;
        }
        try {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            java.time.LocalDateTime.parse(dt, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void handleAddSchedule() {
        String title = titleField.getText().trim();
        String dateTime = dateTimeField.getText().trim();
        String notes = notesArea.getText().trim();

        if (title.isEmpty() || dateTime.isEmpty()) {
            showAlert("Error", "Materi latihan dan Tanggal/Jam harus diisi!", Alert.AlertType.WARNING);
            return;
        }

        if (!isValidDateTimeFormat(dateTime)) {
            showAlert("Error", "Format tanggal & waktu salah! Gunakan format YYYY-MM-DD HH:MM (contoh: 2026-06-01 19:00)", Alert.AlertType.WARNING);
            return;
        }

        String id = "S" + (DataStore.getInstance().getTrainingSchedules().size() + 1);
        TrainingSchedule newSchedule = new TrainingSchedule(id, team.getName(), title, dateTime, notes.isEmpty() ? "-" : notes);
        DataStore.getInstance().addTrainingSchedule(newSchedule);

        showAlert("Sukses", "Jadwal latihan berhasil dibuat!", Alert.AlertType.INFORMATION);
        titleField.clear();
        dateTimeField.clear();
        notesArea.clear();

        refreshList();
        onDataChanged.run();
    }

    private void handleDelete(TrainingSchedule s) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, 
            "Apakah Anda yakin ingin menghapus jadwal latihan ini?", 
            ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Hapus Jadwal Latihan");
        confirm.setHeaderText(null);

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            DataStore.getInstance().removeTrainingSchedule(s);
            showAlert("Sukses", "Jadwal latihan berhasil dihapus.", Alert.AlertType.INFORMATION);
            refreshList();
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
