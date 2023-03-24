package com.mycompany.hangman.Game;


import java.util.ArrayList;

public class HangmanSingle extends Hangman{
    private Player player;
    private String word;
    private String hiddenWord;
    private int incorrectGuesses;
    private int correctGuesses;
    private ArrayList<Character> guessedLetters;



    public HangmanSingle(Player player, String word)
    {
        this.player = player;
        this.word = word;
        this.incorrectGuesses = 3;
        this.correctGuesses = 0;
        this.hiddenWord = "";
        for(int i = 0; i < word.length(); i++)
        {
            if(word.charAt(i) == ' ')
                this.hiddenWord += " ";
            else
                this.hiddenWord += "_";
        }
    }

    public HangmanSingle(Player player, String word, int incorrectGuesses, int correctGuesses)
    {
        this.player = player;
        this.word = word;
        this.incorrectGuesses = incorrectGuesses;
        this.correctGuesses = correctGuesses;
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @param player the player to set
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * @return the word
     */
    public String getWord() {
        return word;
    }

    /**
     * @param word the word to set
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * @return the incorrectGuesses
     */
    public int getIncorrectGuesses() {
        return incorrectGuesses;
    }

    /**
     * @return the correctGuesses
     */
    public int getCorrectGuesses() {
        return correctGuesses;
    }

    /**
     * @return the hiddenWord
     */
    public String getHiddenWord() {
        return hiddenWord;
    }

    public boolean checkGuess(char guess) {
        if (this.word.contains(Character.toString(guess))) {
            for (int i = 0; i < this.word.length(); i++) {
                if (this.word.charAt(i) == guess) {
                    this.hiddenWord = this.hiddenWord.substring(0, i) + guess + this.hiddenWord.substring(i + 1);
                }
            }
            this.correctGuesses++;
            return true;
        } else {
            this.incorrectGuesses--;
            return false;
        }
    }

    
}
