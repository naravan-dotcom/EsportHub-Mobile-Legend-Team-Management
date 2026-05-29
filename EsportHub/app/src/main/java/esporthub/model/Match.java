package esporthub.model;

import javafx.beans.property.*;

public class Match {
    private final StringProperty id;
    private final ObjectProperty<Team> team1;
    private final ObjectProperty<Team> team2;
    private final StringProperty dateTime;
    private final StringProperty status; // "Scheduled" or "Finished"
    private final IntegerProperty score1;
    private final IntegerProperty score2;
    private final StringProperty winner; // Winner team name or "TBD"

    public Match(String id, Team team1, Team team2, String dateTime) {
        this.id = new SimpleStringProperty(id);
        this.team1 = new SimpleObjectProperty<>(team1);
        this.team2 = new SimpleObjectProperty<>(team2);
        this.dateTime = new SimpleStringProperty(dateTime);
        this.status = new SimpleStringProperty("Scheduled");
        this.score1 = new SimpleIntegerProperty(0);
        this.score2 = new SimpleIntegerProperty(0);
        this.winner = new SimpleStringProperty("TBD");
    }

    public String getId() { return id.get(); }
    public void setId(String value) { id.set(value); }
    public StringProperty idProperty() { return id; }

    public Team getTeam1() { return team1.get(); }
    public void setTeam1(Team value) { team1.set(value); }
    public ObjectProperty<Team> team1Property() { return team1; }

    public Team getTeam2() { return team2.get(); }
    public void setTeam2(Team value) { team2.set(value); }
    public ObjectProperty<Team> team2Property() { return team2; }

    public String getDateTime() { return dateTime.get(); }
    public void setDateTime(String value) { dateTime.set(value); }
    public StringProperty dateTimeProperty() { return dateTime; }

    public String getStatus() { return status.get(); }
    public void setStatus(String value) { status.set(value); }
    public StringProperty statusProperty() { return status; }

    public int getScore1() { return score1.get(); }
    public void setScore1(int value) { score1.set(value); }
    public IntegerProperty score1Property() { return score1; }

    public int getScore2() { return score2.get(); }
    public void setScore2(int value) { score2.set(value); }
    public IntegerProperty score2Property() { return score2; }

    public String getWinner() { return winner.get(); }
    public void setWinner(String value) { winner.set(value); }
    public StringProperty winnerProperty() { return winner; }
}
