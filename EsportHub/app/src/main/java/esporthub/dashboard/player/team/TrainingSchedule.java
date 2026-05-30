package esporthub.dashboard.player.team;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TrainingSchedule {
    private final StringProperty id;
    private final StringProperty teamName;
    private final StringProperty title;
    private final StringProperty dateTime;
    private final StringProperty notes;

    public TrainingSchedule(String id, String teamName, String title, String dateTime, String notes) {
        this.id = new SimpleStringProperty(id);
        this.teamName = new SimpleStringProperty(teamName);
        this.title = new SimpleStringProperty(title);
        this.dateTime = new SimpleStringProperty(dateTime);
        this.notes = new SimpleStringProperty(notes);
    }

    public String getId() { return id.get(); }
    public void setId(String value) { id.set(value); }
    public StringProperty idProperty() { return id; }

    public String getTeamName() { return teamName.get(); }
    public void setTeamName(String value) { teamName.set(value); }
    public StringProperty teamNameProperty() { return teamName; }

    public String getTitle() { return title.get(); }
    public void setTitle(String value) { title.set(value); }
    public StringProperty titleProperty() { return title; }

    public String getDateTime() { return dateTime.get(); }
    public void setDateTime(String value) { dateTime.set(value); }
    public StringProperty dateTimeProperty() { return dateTime; }

    public String getNotes() { return notes.get(); }
    public void setNotes(String value) { notes.set(value); }
    public StringProperty notesProperty() { return notes; }
}
