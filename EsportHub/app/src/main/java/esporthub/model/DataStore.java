package esporthub.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataStore {
    private static DataStore instance;

    public static synchronized DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    private final ObservableList<Team> teams = FXCollections.observableArrayList();
    private final ObservableList<Player> players = FXCollections.observableArrayList();
    private final ObservableList<Match> matches = FXCollections.observableArrayList();

    private DataStore() {
        // Initialize mock teams
        Team rrq = new Team("T1", "RRQ Hoshi", 4, 2, false);
        Team onic = new Team("T2", "ONIC Esports", 5, 1, false);
        Team evos = new Team("T3", "EVOS Glory", 2, 4, false);
        Team ae = new Team("T4", "Alter Ego", 3, 3, false);

        teams.addAll(rrq, onic, evos, ae);

        // Initialize mock players for RRQ
        players.add(new Player("P1", "Sutsujin", "Jungler", "RRQ Hoshi", false));
        players.add(new Player("P2", "Skylar", "Gold Laner", "RRQ Hoshi", false));
        players.add(new Player("P3", "Dyrennn", "EXP Laner", "RRQ Hoshi", false));
        players.add(new Player("P4", "Idok", "Roamer", "RRQ Hoshi", false));
        players.add(new Player("P5", "Rinz", "Mid Laner", "RRQ Hoshi", false));

        // Initialize mock players for ONIC
        players.add(new Player("P6", "Kairi", "Jungler", "ONIC Esports", false));
        players.add(new Player("P7", "Sanz", "Mid Laner", "ONIC Esports", false));
        players.add(new Player("P8", "CW", "Gold Laner", "ONIC Esports", false));
        players.add(new Player("P9", "Kiboy", "Roamer", "ONIC Esports", false));
        players.add(new Player("P10", "Rezz", "EXP Laner", "ONIC Esports", false));

        // Initialize mock players for EVOS
        players.add(new Player("P11", "Anavel", "Jungler", "EVOS Glory", false));
        players.add(new Player("P12", "Branz", "Gold Laner", "EVOS Glory", false));
        players.add(new Player("P13", "Claw", "Mid Laner", "EVOS Glory", false));
        players.add(new Player("P14", "DreamS", "Roamer", "EVOS Glory", false));
        players.add(new Player("P15", "Fluffy", "EXP Laner", "EVOS Glory", false));

        // Initialize mock players for Alter Ego
        players.add(new Player("P16", "Tazz", "Jungler", "Alter Ego", false));
        players.add(new Player("P17", "Nino", "Gold Laner", "Alter Ego", false));
        players.add(new Player("P18", "Cr1mpa", "Mid Laner", "Alter Ego", false));
        players.add(new Player("P19", "Rasy", "Roamer", "Alter Ego", false));
        players.add(new Player("P20", "Pai", "EXP Laner", "Alter Ego", false));

        // Initialize mock matches
        matches.add(new Match("M1", onic, rrq, "2026-05-30 18:00"));
        matches.add(new Match("M2", evos, ae, "2026-05-31 15:30"));
        
        // Add a finished match as reference
        Match m3 = new Match("M3", rrq, evos, "2026-05-25 19:00");
        m3.setStatus("Finished");
        m3.setScore1(2);
        m3.setScore2(1);
        m3.setWinner("RRQ Hoshi");
        matches.add(m3);
    }

    public ObservableList<Team> getTeams() { return teams; }
    public ObservableList<Player> getPlayers() { return players; }
    public ObservableList<Match> getMatches() { return matches; }

    public void addTeam(Team team) { teams.add(team); }
    public void removeTeam(Team team) {
        teams.remove(team);
        // Clean players belonging to this team or set their team to None
        for (Player p : players) {
            if (p.getTeamName().equalsIgnoreCase(team.getName())) {
                p.setTeamName("None");
            }
        }
    }

    public void addPlayer(Player player) { players.add(player); }
    public void removePlayer(Player player) { players.remove(player); }

    public void addMatch(Match match) { matches.add(match); }
    public void removeMatch(Match match) { matches.remove(match); }
}
