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

public class MemberListSection extends VBox {

    private final Player currentPlayer;
    private final Team team;
    private final Runnable onDataChanged;

    private TableView<Player> memberTable;
    private final ObservableList<Player> memberList = FXCollections.observableArrayList();

    public MemberListSection(Player currentPlayer, Team team, Runnable onDataChanged) {
        this.currentPlayer = currentPlayer;
        this.team = team;
        this.onDataChanged = onDataChanged;

        this.setSpacing(16);
        this.setPadding(new Insets(10, 0, 0, 0));

        Label sectionTitle = new Label("Anggota Tim");
        sectionTitle.setFont(Font.font("Outfit", FontWeight.BOLD, 18));
        sectionTitle.setStyle("-fx-text-fill: #0F172A; -fx-font-family: 'Outfit'; -fx-font-weight: bold; -fx-font-size: 18px;");

        setupTable();
        refreshList();

        this.getChildren().addAll(sectionTitle, memberTable);
    }

    public void refreshList() {
        memberList.clear();
        String tName = team.getName();
        for (Player p : DataStore.getInstance().getPlayers()) {
            if (p.getTeamName().equalsIgnoreCase(tName)) {
                memberList.add(p);
            }
        }
    }

    private void setupTable() {
        memberTable = new TableView<>();
        memberTable.setItems(memberList);
        memberTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        memberTable.setPrefHeight(300);
        memberTable.setStyle(
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

        TableColumn<Player, String> nameCol = new TableColumn<>("Nama");
        nameCol.setCellValueFactory(f -> f.getValue().nameProperty());

        TableColumn<Player, String> roleCol = new TableColumn<>("Role / Posisi");
        roleCol.setCellValueFactory(f -> f.getValue().roleProperty());
        roleCol.setPrefWidth(120);

        TableColumn<Player, String> statusCol = new TableColumn<>("Jabatan");
        statusCol.setCellValueFactory(f -> {
            Player p = f.getValue();
            boolean isCap = team.getCaptainName().equalsIgnoreCase(p.getName());
            return new SimpleStringProperty(isCap ? "Ketua" : "Anggota");
        });
        statusCol.setPrefWidth(100);

        // Action column for Kicking (only visible / usable if current user is Captain)
        TableColumn<Player, Void> actionCol = new TableColumn<>("Aksi");
        actionCol.setPrefWidth(100);

        Callback<TableColumn<Player, Void>, TableCell<Player, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Player, Void> call(final TableColumn<Player, Void> param) {
                return new TableCell<>() {
                    private final Button kickBtn = new Button("Kick");
                    {
                        kickBtn.setFont(Font.font("Inter", FontWeight.BOLD, 12));
                        kickBtn.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #EF4444; -fx-padding: 6px 12px; -fx-background-radius: 6px; -fx-font-weight: bold;");
                        kickBtn.setCursor(Cursor.HAND);
                        kickBtn.setOnMouseEntered(e -> kickBtn.setStyle("-fx-background-color: #FCA5A5; -fx-text-fill: #B91C1C; -fx-padding: 6px 12px; -fx-background-radius: 6px; -fx-font-weight: bold;"));
                        kickBtn.setOnMouseExited(e -> kickBtn.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #EF4444; -fx-padding: 6px 12px; -fx-background-radius: 6px; -fx-font-weight: bold;"));
                        kickBtn.setOnAction(event -> {
                            Player p = getTableView().getItems().get(getIndex());
                            handleKick(p);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            Player p = getTableView().getItems().get(getIndex());
                            boolean isCurrentCap = team.getCaptainName().equalsIgnoreCase(currentPlayer.getName());
                            boolean isTargetCap = team.getCaptainName().equalsIgnoreCase(p.getName());
                            
                            // Only allow kick if current user is Captain AND target is not the captain
                            if (isCurrentCap && !isTargetCap) {
                                setGraphic(kickBtn);
                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                };
            }
        };

        actionCol.setCellFactory(cellFactory);

        memberTable.getColumns().addAll(nameCol, roleCol, statusCol, actionCol);
    }

    private void handleKick(Player p) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, 
            "Apakah Anda yakin ingin mengeluarkan " + p.getName() + " dari tim?", 
            ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Kick Anggota");
        confirm.setHeaderText(null);

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            p.setTeamName("None");
            showAlert("Sukses", p.getName() + " telah dikeluarkan dari tim.", Alert.AlertType.INFORMATION);
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
