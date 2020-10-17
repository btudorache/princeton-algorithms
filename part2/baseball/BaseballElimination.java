import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class BaseballElimination {
    private final int numTeams;
    private final String[] teams;
    private final int[] wins;
    private final int[] loss;
    private final int[] remaining;
    private final int[][] games;

    private final HashMap<String, Integer> nameMapping;
    private final Queue<String> iterableTeams;

    public BaseballElimination(String filename) {
        In input = new In(filename);
        this.numTeams = input.readInt();

        this.teams = new String[numTeams];
        this.wins = new int[numTeams];
        this.loss = new int[numTeams];
        this.remaining = new int[numTeams];
        this.games = new int[numTeams][numTeams];

        this.nameMapping = new HashMap<String, Integer>();
        this.iterableTeams = new Queue<String>();

        String team;
        for (int i = 0; i < numTeams; i++) {
            team = input.readString();
            this.teams[i] = team;
            this.nameMapping.put(team, i);
            this.iterableTeams.enqueue(team);

            this.wins[i] = input.readInt();
            this.loss[i] = input.readInt();
            this.remaining[i] = input.readInt();
            for (int j = 0; j < numTeams; j++) {
                this.games[i][j] = input.readInt();
            }
        }
    }

    public int numberOfTeams() {
        return this.numTeams;
    }

    public Iterable<String> teams() {
        return this.iterableTeams;
    }

    public int wins(String team) {
        if (team == null || !this.nameMapping.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        return this.wins[this.nameMapping.get(team)];
    }

    public int losses(String team) {
        if (team == null || !this.nameMapping.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        return this.loss[this.nameMapping.get(team)];
    }

    public int remaining(String team) {
        if (team == null || !this.nameMapping.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        return this.remaining[this.nameMapping.get(team)];
    }

    public int against(String team1, String team2) {
        if (team1 == null || team2 == null ||
                !this.nameMapping.containsKey(team1) ||
                !this.nameMapping.containsKey(team2)) {
            throw new IllegalArgumentException();
        }
        return this.games[this.nameMapping.get(team1)][this.nameMapping.get(team2)];
    }

    private FlowNetwork buildNetwork(int teamIndex) {
        int numGameVertices = (this.numTeams - 1) * (this.numTeams - 2) / 2;
        int numNodes = numGameVertices + this.numTeams + 2;

        FlowNetwork network = new FlowNetwork(numNodes);
        int firstTeamVertexInd = numGameVertices + 1;

        // add vertices
        int gameVertexInd = 1;
        FlowEdge edge;
        for (int i = 0; i < this.numTeams - 1; i++) {
            for (int j = i + 1; j < this.numTeams; j++) {
                if (i == teamIndex || j == teamIndex) {
                    continue;
                }
                // add vertice from source to first layer
                edge = new FlowEdge(0, gameVertexInd, this.games[i][j]);
                network.addEdge(edge);

                // add vertices from first layer to second layer
                edge = new FlowEdge(gameVertexInd, firstTeamVertexInd + i,
                                    Double.POSITIVE_INFINITY);
                network.addEdge(edge);
                edge = new FlowEdge(gameVertexInd, firstTeamVertexInd + j,
                                    Double.POSITIVE_INFINITY);
                network.addEdge(edge);

                gameVertexInd++;
            }
        }

        // add vertices from second layer to last layer
        int capacity;
        for (int i = 0; i < this.numTeams; i++) {
            if (i == teamIndex) {
                continue;
            }
            capacity = this.wins[teamIndex] + this.remaining[teamIndex] - this.wins[i];
            if (capacity < 0) {
                capacity = 0;
            }
            edge = new FlowEdge(firstTeamVertexInd + i, numNodes - 1, capacity);
            network.addEdge(edge);
        }
        return network;
    }

    private Queue<String> buildIterable(FordFulkerson minCut) {
        int numGameVertices = (this.numTeams - 1) * (this.numTeams - 2) / 2;
        int firstTeamVertexInd = numGameVertices + 1;

        Queue<String> teamIterable = new Queue<String>();
        for (int i = 0; i < this.numTeams; i++) {
            if (minCut.inCut(firstTeamVertexInd + i)) {
                teamIterable.enqueue(this.teams[i]);
            }
        }
        return teamIterable;
    }

    public boolean isEliminated(String team) {
        if (team == null || !this.nameMapping.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        int teamIndex = this.nameMapping.get(team);
        int maxWins = this.wins[teamIndex] + this.remaining[teamIndex];
        for (int i = 0; i < this.numTeams; i++) {
            if (i != teamIndex && maxWins < wins[i]) {
                return true;
            }
        }
        FlowNetwork network = buildNetwork(teamIndex);
        int numGameVertices = (this.numTeams - 1) * (this.numTeams - 2) / 2;
        int numNodes = numGameVertices + this.numTeams + 2;

        FordFulkerson minCut = new FordFulkerson(network, 0, numNodes - 1);
        Queue<String> teamIterable = this.buildIterable(minCut);

        String[] teamsInCut = new String[teamIterable.size()];
        int ind = 0;
        int collectiveGames = 0;
        for (String teamInCut : teamIterable) {
            collectiveGames += this.wins(teamInCut);
            teamsInCut[ind++] = teamInCut;
        }

        for (int i = 0; i < teamsInCut.length - 1; i++) {
            for (int j = i + 1; j < teamsInCut.length; j++) {
                collectiveGames += this.against(teamsInCut[i], teamsInCut[j]);
            }
        }
        return (Math.ceil(collectiveGames / (double) teamsInCut.length) > maxWins);
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (team == null || !this.nameMapping.containsKey(team)) {
            throw new IllegalArgumentException();
        }
        int teamIndex = this.nameMapping.get(team);
        int maxWins = this.wins[teamIndex] + this.remaining[teamIndex];

        Queue<String> teamIterable;
        for (int i = 0; i < this.numTeams; i++) {
            if (i != teamIndex && maxWins < wins[i]) {
                teamIterable = new Queue<String>();
                teamIterable.enqueue(teams[i]);
                return teamIterable;
            }
        }

        FlowNetwork network = buildNetwork(teamIndex);
        int numGameVertices = (this.numTeams - 1) * (this.numTeams - 2) / 2;
        int numNodes = numGameVertices + this.numTeams + 2;

        FordFulkerson minCut = new FordFulkerson(network, 0, numNodes - 1);
        teamIterable = this.buildIterable(minCut);

        if (!teamIterable.isEmpty()) {
            return teamIterable;
        }
        else {
            return null;
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
