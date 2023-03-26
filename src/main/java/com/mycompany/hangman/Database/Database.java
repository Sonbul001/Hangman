package com.mycompany.hangman.Database;

import com.mycompany.hangman.Game.HangmanMulti;
import com.mycompany.hangman.Game.Player;
import com.mycompany.hangman.Game.Team;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Database {
    private static final File creds = new File("credentials.txt");
    private static File history = new File("history.txt");
    private static File config = new File("config.txt");
    private static File lookup = new File("lookup.txt");
    private static ArrayList<Player> players = new ArrayList<>();
    private static ArrayList<Team> teams = new ArrayList<>();
    private static ArrayList<HangmanMulti> multiplayerGames = new ArrayList<>();
    private static ArrayList<Player> loggedInPlayers = new ArrayList<>();

    private static int incorrectGuesses;
    private static int minPlayers;
    private static int maxPlayers;

    public static void loadFiles() {
        try {
            Scanner reader = new Scanner(config);
            String[] data = reader.nextLine().split("-");
            incorrectGuesses = Integer.parseInt(data[1]);
            data = reader.nextLine().split("-");
            minPlayers = Integer.parseInt(data[1]);
            data = reader.nextLine().split("-");
            maxPlayers = Integer.parseInt(data[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean checkUsername(String username) {
        try {
            Scanner reader = new Scanner(creds);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                String[] parsed = data.split(" ");
                if (parsed[0].equals(username)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkPassword(String username,String password){
        try {
            Scanner reader = new Scanner(creds);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                String[] parsed = data.split(" ");
                if (parsed[0].equals(username)) {
                    if(parsed[1].equals(password))
                        return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Player getPlayer(String username, String password) {
        try {
            Scanner reader = new Scanner(creds);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                String[] parsed = data.split(" ");
                if (parsed[0].equals(username)) {
                    if (parsed[1].equals(password)) {
                        String playerUsername = parsed[0];
                        String playerPassword = parsed[1];
                        String playerName = parsed[2];
                        ArrayList<Integer> singleGamesHistory = new ArrayList<>();
                        ArrayList<Integer> multiGamesHistory = new ArrayList<>();

                        reader = new Scanner(history);
                        while (reader.hasNextLine()) {
                            data = reader.nextLine();
                            parsed = data.split(" ");
                            if (parsed[0].equals(playerUsername)) {
                                if (parsed[1].equals("single")) {
                                    for (int i = 2; i < parsed.length; i++) {
                                        singleGamesHistory.add(Integer.parseInt(parsed[i]));
                                    }
                                } else if (parsed[1].equals("multi")) {
                                    for (int i = 2; i < parsed.length; i++) {
                                        multiGamesHistory.add(Integer.parseInt(parsed[i]));
                                    }
                                    break;
                                }
                            }
                        }
                        Player player = new Player(playerName, playerUsername, playerPassword, singleGamesHistory, multiGamesHistory);
                        return player;
                    } else {
                        return null;
                    }
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Player addPlayer(String username, String password, String name) {
        boolean exists = false;
        try {
            Scanner reader = new Scanner(creds);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                String[] parsed = data.split(" ");
                if (parsed[0].equals(username)) {
                    exists = true;
                    break;
                }
            }
            reader.close();
            if (!exists) {
                BufferedWriter writer = new BufferedWriter(new FileWriter("credentials.txt", true));
                writer.write(username + " " + password + " " + name);
                writer.newLine();
                writer.close();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Player(name, username, password);
    }

    public static void addGameToHistory(String username, String gameType, int score) {
        try {
            Scanner reader = new Scanner(history);
            int lineNum = 0;
            String data = "";
            boolean found = false;
            while (reader.hasNextLine()) {
                lineNum++;
                String line = reader.nextLine();
                data += line;
                String[] parsed = line.split(" ");
                if (parsed[0].equals(username)) {
                    if (parsed[1].equals(gameType)) {
                        found = true;
                        data += " " + score;
                    }
                }
                data += "\n";
            }
            if (!found) {
                data += username + " " + gameType + " " + score + "\n";
            }
            reader.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter(history, false));
            writer.write(data);
            writer.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getWord() {
        try {
            Scanner reader = new Scanner(lookup);
            int wordLine =(int) Math.round(Math.random()*19);
            for (int i = 0; i <= wordLine; i++) {
                reader.nextLine();
                if(i==wordLine-2) {
                    String word = reader.nextLine();
                    return word;
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addTeam(Team team) {
        teams.add(team);
    }

    public static void removeTeam(Team team) {
        teams.remove(team);
    }

    public static Team joinTeam(Player player, int code) {
        for (Team team : teams) {
            if (team.getCode() == code) {
                team.joinTeam(player, code);
                return team;
            }
        }
    return null;
    }

    public static boolean checkTeamExists(int code) {
        for (Team team : teams) {
            if (team.getCode() == code) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkTeamFull(int code) {
        for (Team team : teams) {
            if (team.getCode() == code) {
                if (team.getPlayers().size() == team.getMaxPlayers()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static HangmanMulti createMultiplayerGame(int size, Team team, String word) {
        for (HangmanMulti game : multiplayerGames) {
            if(!game.isStarted()) {
                if (game.getTeam1().getPlayers().size() == size) {
                    game.joinGame(team);
                    return game;
                }
            }
        }
        HangmanMulti game = new HangmanMulti(team, word, Database.getIncorrectGuesses());
        multiplayerGames.add(game);
        return game;
    }

    public static void removeMultiplayerGame(HangmanMulti game) {
        multiplayerGames.remove(game);
    }

    public static Team getTeam(int code) {
        for (Team team : teams) {
            if (team.getCode() == code) {
                return team;
            }
        }
        return null;
    }

    public static HangmanMulti getMultiGamePlayer(Player player) {
        for (HangmanMulti game : multiplayerGames) {
            if (game.getTeam1().getPlayers().contains(player)) {
                return game;
            } else if (game.getTeam2()==null) {
                continue;
            } else if (game.getTeam2().getPlayers().contains(player)) {
                return game;
            }
        }
        return null;
    }

    /***
     * Checks if the team name is already taken
     * @param teamName
     * @return true if the team name is not taken, false if it is
     */
    public static boolean checkTeamNames(String teamName) {
        for (Team team : teams) {
            if (team.getTeamName().equals(teamName)) {
                return false;
            }
        }
        return true;
    }

    /***
     * Checks if the team size is valid
     * @param size
     * @return true if the team size is valid, false if it is not
     */
    public static boolean checkTeamSize(int size) {
        if (size > maxPlayers || size< minPlayers) {
            return false;
        }
        return true;
    }

    public static int getIncorrectGuesses() {
        return incorrectGuesses;
    }

    public static void setIncorrectGuesses(int incorrectGuesses) {
        Database.incorrectGuesses = incorrectGuesses;
    }

    public static int getMinPlayers() {
        return minPlayers;
    }

    public static void setMinPlayers(int minPlayers) {
        Database.minPlayers = minPlayers;
    }

    public static int getMaxPlayers() {
        return maxPlayers;
    }

    public static void setMaxPlayers(int maxPlayers) {
        Database.maxPlayers = maxPlayers;
    }

    public static boolean checkLoggedIn(String username) {
        for (Player player : loggedInPlayers) {
            if (player.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static void addLoggedInPlayer(Player player) {
        loggedInPlayers.add(player);
    }

    public static void removeLoggedInPlayer(Player player) {
        loggedInPlayers.remove(player);
    }

}
