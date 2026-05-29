package esporthub.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty role;
    private final StringProperty teamName;
    private final BooleanProperty banned;

    // Additional fields for Player Dashboard
    private final StringProperty email;
    private final StringProperty rank;
    private final StringProperty kda;
    private final StringProperty winrate;
    private final List<Double> kdaTimeline;
    private final List<MatchHistoryItem> matchesPlayed;

    public Player(String id, String name, String role, String teamName, boolean banned) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.role = new SimpleStringProperty(role);
        this.teamName = new SimpleStringProperty(teamName);
        this.banned = new SimpleBooleanProperty(banned);

        // Default values for new profile fields
        String cleanName = name != null ? name.trim().toLowerCase().replace(" ", "") : "player";
        this.email = new SimpleStringProperty(cleanName + "@esporthub.com");
        this.rank = new SimpleStringProperty("Mythical Glory");
        this.kda = new SimpleStringProperty("4.5 / 2.0 / 6.5");
        this.winrate = new SimpleStringProperty("62.5%");

        this.kdaTimeline = new ArrayList<>();
        this.kdaTimeline.add(3.2);
        this.kdaTimeline.add(4.5);
        this.kdaTimeline.add(3.8);
        this.kdaTimeline.add(5.1);
        this.kdaTimeline.add(4.8);
        this.kdaTimeline.add(6.2);

        this.matchesPlayed = new ArrayList<>();
        this.matchesPlayed.add(new MatchHistoryItem("Ling", "Win", "8 / 1 / 9", "2026-05-28"));
        this.matchesPlayed.add(new MatchHistoryItem("Hayabusa", "Win", "12 / 2 / 5", "2026-05-27"));
        this.matchesPlayed.add(new MatchHistoryItem("Lancelot", "Loss", "3 / 4 / 2", "2026-05-26"));
        this.matchesPlayed.add(new MatchHistoryItem("Fanny", "Win", "10 / 0 / 7", "2026-05-25"));
        this.matchesPlayed.add(new MatchHistoryItem("Gusion", "Win", "7 / 3 / 8", "2026-05-24"));
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

    // Getters and setters for Player Dashboard fields
    public String getEmail() { return email.get(); }
    public void setEmail(String value) { email.set(value); }
    public StringProperty emailProperty() { return email; }

    public String getRank() { return rank.get(); }
    public void setRank(String value) { rank.set(value); }
    public StringProperty rankProperty() { return rank; }

    public String getKda() { return kda.get(); }
    public void setKda(String value) { kda.set(value); }
    public StringProperty kdaProperty() { return kda; }

    public String getWinrate() { return winrate.get(); }
    public void setWinrate(String value) { winrate.set(value); }
    public StringProperty winrateProperty() { return winrate; }

    public List<Double> getKdaTimeline() { return kdaTimeline; }
    public List<MatchHistoryItem> getMatchesPlayed() { return matchesPlayed; }

    @Override
    public String toString() {
        return getName() + " (" + getRole() + ")";
    }

    // Nested class for match history items
    public static class MatchHistoryItem {
        private final String hero;
        private final String result; // "Win" or "Loss"
        private final String score;  // e.g. "8 / 1 / 9"
        private final String date;   // e.g. "2026-05-28"

        public MatchHistoryItem(String hero, String result, String score, String date) {
            this.hero = hero;
            this.result = result;
            this.score = score;
            this.date = date;
        }

        public String getHero() { return hero; }
        public String getResult() { return result; }
        public String getScore() { return score; }
        public String getDate() { return date; }
    }
}
