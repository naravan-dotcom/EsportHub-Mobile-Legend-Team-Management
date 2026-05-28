package esporthub.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;

public class Player {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty role;
    private final StringProperty teamName;
    private final BooleanProperty banned;

    public Player(String id, String name, String role, String teamName, boolean banned) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.role = new SimpleStringProperty(role);
        this.teamName = new SimpleStringProperty(teamName);
        this.banned = new SimpleBooleanProperty(banned);
    }

    public String getId() { return id.get(); }
    public void setId(String value) { id.set(value); }
    public StringProperty idProperty() { return id; }

    public String getName() { return name.get(); }
    public void setName(String value) { name.set(value); }
    public StringProperty nameProperty() { return name; }

    public String getRole() { return role.get(); }
    public void setRole(String value) { role.set(value); }
    public StringProperty roleProperty() { return role; }

    public String getTeamName() { return teamName.get(); }
    public void setTeamName(String value) { teamName.set(value); }
    public StringProperty teamNameProperty() { return teamName; }

    public boolean isBanned() { return banned.get(); }
    public void setBanned(boolean value) { banned.set(value); }
    public BooleanProperty bannedProperty() { return banned; }

    @Override
    public String toString() {
        return getName() + " (" + getRole() + ")";
    }
}
