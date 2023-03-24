package com.mycompany.hangman.Game;

import java.util.ArrayList;


public class Team {
   private String teamName;
   private ArrayList<Player> players;
   private static int codeIncrementer = 0;
   private int code;
   private int maxPlayers;
   private int currentPlayersJoined;

   private boolean ready;
   
   public Team(){}
   
   public Team(String teamName, Player player, int maxPlayers){
       this.teamName = teamName;
       this.players = new ArrayList<>();
       this.players.add(player);
       
       this.code = ++codeIncrementer;
       this.maxPlayers = maxPlayers;
       this.currentPlayersJoined = 1;
       this.ready = false;
   }
   
   public String joinTeam(Player player, int code){
       if (code == code){
           if(getCurrentPlayersJoined() < getMaxPlayers()){
                getPlayers().add(player);
                setCurrentPlayersJoined(getCurrentPlayersJoined() + 1);
               return "You have joined the team successfully";
           }
           else{
               return "The team is full!";
           }
       }
       else{
           return "Wrong Code Entered";
       }
   }

    /**
     * @return the players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * @param players the players to set
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the maxPlayers
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * @param maxPlayers the maxPlayers to set
     */
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    /**
     * @return the currentPlayersJoined
     */
    public int getCurrentPlayersJoined() {
        return currentPlayersJoined;
    }

    /**
     * @param currentPlayersJoined the currentPlayersJoined to set
     */
    public void setCurrentPlayersJoined(int currentPlayersJoined) {
        this.currentPlayersJoined = currentPlayersJoined;
    }

    /**
     * @return the teamName
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * @param teamName the teamName to set
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public boolean isReady(){
        return ready;
    }

    public void setReady(boolean ready){
        this.ready = ready;
    }

}
