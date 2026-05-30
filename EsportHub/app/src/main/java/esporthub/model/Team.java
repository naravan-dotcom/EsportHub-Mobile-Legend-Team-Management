package esporthub.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Team {
    private final StringProperty id;
    private final StringProperty name;
    private final IntegerProperty wins;
    private final IntegerProperty losses;
    private final BooleanProperty banned;
    private final StringProperty captainName;
    private final StringProperty privacyType;
    private final ObservableList<String> joinRequests;

    public Team(String id, String name, int wins, int losses, boolean banned) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.wins = new SimpleIntegerProperty(wins);
        this.losses = new SimpleIntegerProperty(losses);
        this.banned = new SimpleBooleanProperty(banned);
        this.captainName = new SimpleStringProperty("");
        this.privacyType = new SimpleStringProperty("Public");
        this.joinRequests = FXCollections.observableArrayList();
    }

    public String getId() { return id.get(); }
    public void setId(String value) { id.set(value); }
    public StringProperty idProperty() { return id; }

    public String getName() { return name.get(); }
    public void setName(String value) { name.set(value); }
    public StringProperty nameProperty() { return name; }

    public int getWins() { return wins.get(); }
    public void setWins(int value) { wins.set(value); }
    public IntegerProperty winsProperty() { return wins; }

    public int getLosses() { return losses.get(); }
    public void setLosses(int value) { losses.set(value); }
    public IntegerProperty lossesProperty() { return losses; }

    public boolean isBanned() { return banned.get(); }
    public void setBanned(boolean value) { banned.set(value); }
    public BooleanProperty bannedProperty() { return banned; }

    public String getCaptainName() { return captainName.get(); }
    public void setCaptainName(String value) { captainName.set(value); }
    public StringProperty captainNameProperty() { return captainName; }

    public String getPrivacyType() { return privacyType.get(); }
    public void setPrivacyType(String value) { privacyType.set(value); }
    public StringProperty privacyTypeProperty() { return privacyType; }

    public ObservableList<String> getJoinRequests() { return joinRequests; }

    @Override
    public String toString() {
        return getName();
    }
}
