package com.mycompany.hangman.Game;

import com.mycompany.hangman.Server.Server;

import java.util.ArrayList;

public class Player {
    private String name;
    private String username;
    private String password;
    private ArrayList<Integer> singlePlayerGamesHistory;
    private ArrayList<Integer> MultiPlayerGamesHistory;
    private Server server;
    
    public Player(){}
    
    public Player(String name, String username, String password)
    {
        this.name = name;
        this.username = username;
        this.password = password;
        
        this.singlePlayerGamesHistory = new ArrayList<>();
        this.MultiPlayerGamesHistory = new ArrayList<>();

    }
    
    public Player(String name, String username, String password, ArrayList<Integer> singlePlayerGamesHistory, ArrayList<Integer> MultiPlayerGamesHistory)
    {
        this.name = name;
        this.username = username;
        this.password = password;
        
        this.singlePlayerGamesHistory = singlePlayerGamesHistory;
        this.MultiPlayerGamesHistory = MultiPlayerGamesHistory;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the singlePlayerGamesHistory
     */
    public ArrayList<Integer> getSinglePlayerGamesHistory() {
        return singlePlayerGamesHistory;
    }
    
    public void addToSinglePlayerGamesHistory(int gameScore) {
        singlePlayerGamesHistory.add(gameScore);
    }

    /**
     * @return the MultiPlayerGamesHistory
     */
    public ArrayList<Integer> getMultiPlayerGamesHistory() {
        return MultiPlayerGamesHistory;
    }
    
    public void addToMultiPlayerGamesHistory(int gameScore) {
        MultiPlayerGamesHistory.add(gameScore);
    }

    public void setServer(Server server)
    {
        this.server = server;
    }

    public Server getServer()
    {
        return this.server;
    }
    
}
