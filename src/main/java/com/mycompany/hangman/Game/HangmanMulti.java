/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.hangman.Game;

import java.util.ArrayList;

/**
 *
 * @author Lenovo
 */
public class HangmanMulti {
    private Team team1;
    private Team team2;
    private String word;
    private String hiddenWord;
    private int Team1IncorrectGuesses;

    private int Team2IncorrectGuesses;
    private int Team1CorrectGuesses;
    private int Team2CorrectGuesses;
    private ArrayList<Character> guessedLetters;
    private int turn;
    private int teamTurn;

    private boolean started;

    public HangmanMulti(Team team1, Team team2, String word) {
        this.team1 = team1;
        this.team2 = team2;
        this.word = word;
        this.guessedLetters = new ArrayList<>();
        this.Team1IncorrectGuesses = 6;
        this.Team2IncorrectGuesses = 6;
        this.Team1CorrectGuesses = 0;
        this.Team2CorrectGuesses = 0;
        this.hiddenWord = "";
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == ' ')
                this.hiddenWord += " ";
            else
                this.hiddenWord += "_";
        }

        start();
    }

    public HangmanMulti(Team team1, String word) {
        this.team1 = team1;
        this.word = word;
        this.guessedLetters = new ArrayList<>();
        this.Team1IncorrectGuesses = 6;
        this.Team2IncorrectGuesses = 6;
        this.Team1CorrectGuesses = 0;
        this.Team2CorrectGuesses = 0;
        this.hiddenWord = "";
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == ' ')
                this.hiddenWord += " ";
            else
                this.hiddenWord += "_";
        }
    }


    public void joinGame(Team team) {
        team2 = team;
        start();
    }

    public void start() {
        started = true;
        turn = 1;
        teamTurn = 1;
        for (Team team : new Team[]{team1, team2}) {
            for (Player player : team.getPlayers()) {
                player.getServer().setGameStarted(true);
            }
        }
        team1.getPlayers().get(turn - 1).getServer().setTurn(true);
    }

    public boolean checkGuess(char guess, Player player) {
        guessedLetters.add(guess);
        boolean correct = false;
        Team[] teams = new Team[]{team1, team2};
        teams[teamTurn - 1].getPlayers().get(turn - 1).getServer().setTurn(false);
        if (this.word.contains(Character.toString(guess))) {
            for (int i = 0; i < this.word.length(); i++) {
                if (this.word.charAt(i) == guess) {
                    this.hiddenWord = this.hiddenWord.substring(0, i) + guess + this.hiddenWord.substring(i + 1);
                }
            }
            if (teamTurn == 1)
                this.Team1CorrectGuesses++;
            else
                this.Team2CorrectGuesses++;

            correct = true;

        } else {
            if (teamTurn == 1)
                this.Team1IncorrectGuesses--;
            else
                this.Team2IncorrectGuesses--;
            correct = false;
        }

        if (teamTurn == 2) {

            if (turn == team2.getPlayers().size())
                turn = 1;
            else
                turn++;
        }
        for (Team team : teams) {
            for (Player p : team.getPlayers()) {
                if (p != teams[teamTurn - 1].getPlayers().get(turn - 1))
                    p.getServer().setPlayerPlayed(true);
            }
        }
        teamTurn = (teamTurn == 1) ? 2 : 1;
        teams[teamTurn - 1].getPlayers().get(turn - 1).getServer().setTurn(true);

        return correct;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public String getHiddenWord() {
        return hiddenWord;
    }

    public void setHiddenWord(String hiddenWord) {
        this.hiddenWord = hiddenWord;
    }

    public int getTeam1IncorrectGuesses() {
        return Team1IncorrectGuesses;
    }

    public void setTeam1IncorrectGuesses(int team1IncorrectGuesses) {
        Team1IncorrectGuesses = team1IncorrectGuesses;
    }

    public int getTeam2IncorrectGuesses() {
        return Team2IncorrectGuesses;
    }

    public void setTeam2IncorrectGuesses(int team2IncorrectGuesses) {
        Team2IncorrectGuesses = team2IncorrectGuesses;
    }

    public int getTeam1CorrectGuesses() {
        return Team1CorrectGuesses;
    }

    public void setTeam1CorrectGuesses(int team1CorrectGuesses) {
        Team1CorrectGuesses = team1CorrectGuesses;
    }

    public int getTeam2CorrectGuesses() {
        return Team2CorrectGuesses;
    }

    public void setTeam2CorrectGuesses(int team2CorrectGuesses) {
        Team2CorrectGuesses = team2CorrectGuesses;
    }

    public ArrayList<Character> getGuessedLetters() {
        return guessedLetters;
    }

    public void setGuessedLetters(ArrayList<Character> guessedLetters) {
        this.guessedLetters = guessedLetters;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getTeamTurn() {
        return teamTurn;
    }

    public void setTeamTurn(int teamTurn) {
        this.teamTurn = teamTurn;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isGameOver() {
        return Team1IncorrectGuesses == 0 || Team2IncorrectGuesses == 0 || hiddenWord.equalsIgnoreCase(word);
    }

    public Team getWinner() {
        if(Team2IncorrectGuesses == 0)
            return team1;
        else if(Team1IncorrectGuesses == 0)
            return team2;
        else if (Team1CorrectGuesses > Team2CorrectGuesses)
            return team1;
        else if (Team2CorrectGuesses > Team1CorrectGuesses)
            return team2;
        else
            return null;
    }
}
