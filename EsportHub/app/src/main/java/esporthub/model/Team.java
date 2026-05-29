package esporthub.model;

import javafx.beans.property.*;

public class Team {
    private final StringProperty id;
    private final StringProperty name;
    private final IntegerProperty wins;
    private final IntegerProperty losses;
    private final BooleanProperty banned;

    public Team(String id, String name, int wins, int losses, boolean banned) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.wins = new SimpleIntegerProperty(wins);
        this.losses = new SimpleIntegerProperty(losses);
        this.banned = new SimpleBooleanProperty(banned);
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

    @Override
    public String toString() {
        return getName();
    }
}
