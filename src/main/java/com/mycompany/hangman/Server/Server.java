package com.mycompany.hangman.Server;

import com.mycompany.hangman.Database.Database;
import com.mycompany.hangman.Game.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    BufferedWriter writer;
    BufferedReader reader;

    protected Socket socket;
    protected static Thread t;
    protected static File creds;
    protected static File history;
    protected static File config;
    protected static File lookup;
    protected static int incorrectGuesses;
    protected static int minPlayers;
    protected static int maxPlayers;
    protected boolean turn = false;
    protected boolean gameStarted = false;

    protected boolean playerPlayed = false;

    protected boolean playerExited = false;


    protected boolean saveScore = false;

    //protected boolean teamFull = false;
    protected Player loggedinPlayer = null;

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setBufferedWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public void setBufferedReader(BufferedReader reader) {
        this.reader = reader;
    }

    public void setSaveScore(boolean saveScore) {
        this.saveScore = saveScore;
    }

    public boolean getSaveScore() {
        return saveScore;
    }



    public void login() {
        try {
            Player result = null;
            while (result == null) {
                String out, in;

                out = "Please enter your username and password separated by a space";
                writer.write(out);
                writer.newLine();
                writer.flush();

                // input format should be (username password)
                in = reader.readLine();
                String[] info = in.split(" ");
                System.out.println(info[0]);
                System.out.println(info[1]);
                if (!Database.checkUsername(info[0])) {
                    writer.write("404 Username not found");
                    writer.newLine();
                    writer.flush();
                } else if (!Database.checkPassword(info[0], info[1])) {
                    writer.write("401 unauthorized");
                    writer.newLine();
                    writer.flush();
                } else if (Database.checkLoggedIn(info[0])) {
                    writer.write("User already logged in");
                    writer.newLine();
                    writer.flush();
                } else {
                    result = Database.getPlayer(info[0], info[1]);
                    Database.addLoggedInPlayer(result);
                }

            }
            writer.write("Welcome " + result.getName());
            writer.newLine();
            writer.flush();
            loggedinPlayer = result;
            loggedinPlayer.setServer(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register() {
        try {
            String out, in;
            Player result = null;
            while (result == null) {
                out = "Please enter your username, password, and name separated by a space";
                writer.write(out);
                writer.newLine();
                writer.flush();

                // input format should be (username password name)
                in = reader.readLine();
                String[] info = in.split(" ");
                System.out.println(info[0]);
                System.out.println(info[1]);
                result = Database.addPlayer(info[0], info[1], info[2]);
                if (result == null) {
                    writer.write("Username already exists");
                    writer.newLine();
                    writer.flush();
                }
            }
            writer.write("Welcome " + result.getName());
            writer.newLine();
            writer.flush();
            loggedinPlayer = result;
            loggedinPlayer.setServer(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void singlePlayerGame(){
        try {
            String out, in;
            String word = Database.getWord();
            HangmanSingle game = new HangmanSingle(loggedinPlayer, word);
            out = "Your word is " + game.getHiddenWord();
            writer.write(out);
            writer.newLine();
            writer.flush();

            out = "You have " + game.getIncorrectGuesses() + " remaining guesses";
            writer.write(out);
            writer.newLine();
            writer.flush();

            boolean exited = false;
            while (game.getIncorrectGuesses() != 0 && !game.getHiddenWord().equalsIgnoreCase(word)) {
                out = "Please guess a letter";
                writer.write(out);
                writer.newLine();
                writer.flush();
                in = reader.readLine();

                char guessCharacter = in.charAt(0);

                if (guessCharacter == '-') {
                    exited = true;
                    break;
                }

                if (game.checkPreviousGuess(guessCharacter)) {
                    out = "You have already guessed this letter";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                } else if (game.checkGuess(guessCharacter)) {
                    out = "Correct!";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                } else {
                    out = "Incorrect!";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                }
                out = "Your word is " + game.getHiddenWord();
                writer.write(out);
                writer.newLine();
                writer.flush();

                out = "You have " + game.getIncorrectGuesses() + " remaining guesses";
                writer.write(out);
                writer.newLine();
                writer.flush();
            }
            if (exited) {
                out = "You exited the game";
                writer.write(out);
                writer.newLine();
                writer.flush();
            } else if (game.getHiddenWord().equalsIgnoreCase(word)) {
                out = "You win!";
                writer.write(out);
                writer.newLine();
                writer.flush();
            } else {
                out = "You lose!";
                writer.write(out);
                writer.newLine();
                writer.flush();
            }

            out = "Your score is " + game.getCorrectGuesses();
            writer.write(out);
            writer.newLine();
            writer.flush();

            if (!exited) {
                loggedinPlayer.addToSinglePlayerGamesHistory(game.getCorrectGuesses());
                Database.addGameToHistory(loggedinPlayer.getUsername(), "single", game.getCorrectGuesses());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public HangmanMulti createTeam() {
        try {
            String out, in;
            HangmanMulti game = null;
            String teamName = null;
            while (true) {
                out = "Please enter the name of the team";
                writer.write(out);
                writer.newLine();
                writer.flush();

                in = reader.readLine();
                teamName = in;

                if (!Database.checkTeamNames(teamName)) {
                    out = "Team name already exists, choose another name please";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                    continue;
                }
                else
                {
                    out = "Team name is valid";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                }
                break;
            }
            int teamSize = 0;
            while(true) {

                out = "Please enter the size of the team";
                writer.write(out);
                writer.newLine();
                writer.flush();

                in = reader.readLine();
                teamSize = Integer.parseInt(in);

                if(!Database.checkTeamSize(teamSize)) {
                    out = "Team size is invalid, Size must be between " + Database.getMinPlayers() + " and " + Database.getMaxPlayers() ;
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                    continue;
                }
                else
                {
                    out = "Team size is valid";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                }
                break;

            }

            Team team = new Team(teamName, loggedinPlayer, teamSize);
            Database.addTeam(team);

            out = "Team Code is: " + team.getCode();
            writer.write(out);
            writer.newLine();
            writer.flush();

            while (team.getPlayers().size() != teamSize) {
                System.out.print("");
            }

            out = "Team is full";
            writer.write(out);
            writer.newLine();
            writer.flush();

            while (true) {
                out = "Please enter ready to search for opponent";
                writer.write(out);
                writer.newLine();
                writer.flush();

                in = reader.readLine();
                if (in.equalsIgnoreCase("ready")) {
                    String word = Database.getWord();
                    game = Database.createMultiplayerGame(teamSize, team, word);

                    out = "Game has been created";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();

                    out = "Please wait for opponent to join";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                    break;
                } else {
                    out = "Invalid input";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                }
            }
            return game;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public HangmanMulti joinTeam() {
        try {
            HangmanMulti game = null;
            String out, in;



            int code = 0;
            while (true) {
                out = "Please enter the code of the team";
                writer.write(out);
                writer.newLine();
                writer.flush();

                in = reader.readLine();
                code = Integer.parseInt(in);

                if (!Database.checkTeamExists(code)) {
                    out = "Team code is invalid";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                    continue;
                } else if (Database.checkTeamFull(code)) {
                    out = "Team is full";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                    continue;
                }
                break;
            }
            Team team = Database.joinTeam(loggedinPlayer, code);

            out = "Please wait for Leader to start the game";
            writer.write(out);
            writer.newLine();
            writer.flush();

            while (Database.getMultiGamePlayer(loggedinPlayer) == null) {
                System.out.print("");
            }
            game = Database.getMultiGamePlayer(loggedinPlayer);
            return game;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void multiPlayerGame() {
        try {
            String out, in;
            HangmanMulti game = null;

            while (true) {
                out = "Please choose one of the following:";
                writer.write(out);
                writer.newLine();
                writer.flush();


                out = "1. Create Team";
                writer.write(out);
                writer.newLine();
                writer.flush();

                out = "2. Join Team";
                writer.write(out);
                writer.newLine();
                writer.flush();

                out= "Enter your choice: ";
                writer.write(out);
                writer.newLine();
                writer.flush();

                in = reader.readLine();
                if(Integer.parseInt(in) == 1 || Integer.parseInt(in) == 2) {
                    break;
                }
                else {
                    out = "Invalid input";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                }
            }
                if (in.equalsIgnoreCase("1")) {
                    game = createTeam();
            }
                else if (in.equalsIgnoreCase("2")) {
                    game = joinTeam();
            }

            while (!gameStarted) {
                System.out.print("");
            }

            out = "Game has started";
            writer.write(out);
            writer.newLine();
            writer.flush();

            out = "The hidden word is: " + game.getHiddenWord();
            writer.write(out);
            writer.newLine();
            writer.flush();
            while (!game.isGameOver()) {
                while (!turn) {
                    if (game.isGameOver()) {
                        break;
                    }
                    String name;
                    if (game.getTeamTurn() == 1) {
                        name = game.getTeam1().getPlayers().get(game.getTurn() - 1).getName();
                        out = "it's " + name + "'s  turn";
                        writer.write(out);
                        writer.newLine();
                        writer.flush();
                    } else {
                        name = game.getTeam2().getPlayers().get(game.getTurn() - 1).getName();
                        out = "it's " + name + "'s  turn";
                        writer.write(out);
                        writer.newLine();
                        writer.flush();
                    }
                    while (!playerPlayed) {
                        System.out.print("");
                    }
                    if(playerExited)
                    {
                        break;
                    }
                    playerPlayed = false;
                    out = name + " has guessed " + game.getGuessedLetters().get(game.getGuessedLetters().size() - 1);
                    writer.write(out);
                    writer.newLine();
                    writer.flush();

                    out = "The hidden word is: " + game.getHiddenWord();
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                }
                if(playerExited)
                {
                    break;
                }
                if (game.isGameOver()) {
                    break;
                }
                if (game.getTeamTurn() == 1) {
                    out = "You have " + game.getTeam1IncorrectGuesses() + " incorrect guesses left";
                } else {
                    out = "You have " + game.getTeam2IncorrectGuesses() + " incorrect guesses left";
                }

                writer.write(out);
                writer.newLine();
                writer.flush();

                out = "Please guess a letter";
                writer.write(out);
                writer.newLine();
                writer.flush();

                in = reader.readLine();
                char letter = in.charAt(0);

                if(letter == '-')
                {
                    playerExited = true;
                    game.endGame();
                    break;
                }
                else if(game.checkPreviousGuess(letter)){
                    out = "This letter is already guessed";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                }
                else if (game.checkGuess(letter, loggedinPlayer)) {
                    out = "Correct";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                } else {
                    out = "Incorrect";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                }

                out = "The hidden word is: " + game.getHiddenWord();
                writer.write(out);
                writer.newLine();
                writer.flush();
            }

            if(playerExited)
            {
                out = "Game has been exited";
                writer.write(out);
                writer.newLine();
                writer.flush();

                out = "Game score will not be saved";
                writer.write(out);
                writer.newLine();
                writer.flush();

            }
            else {
                out = "GAME OVER";
                writer.write(out);
                writer.newLine();
                writer.flush();

                if (game.getWinner() == null) {
                    out = "DRAW";
                } else {
                    out = "TEAM " + game.getWinner().getTeamName() + " HAS WON";
                }
                writer.write(out);
                writer.newLine();
                writer.flush();
            }


            if(game.getTeam1().getPlayers().contains(loggedinPlayer)) {
                out = "Your Team Score is: " + game.getTeam1CorrectGuesses();
                writer.write(out);
                writer.newLine();
                writer.flush();

                if(!playerExited) {
                    while (!saveScore) {
                        System.out.print("");
                    }


                    loggedinPlayer.addToMultiPlayerGamesHistory(game.getTeam1CorrectGuesses());
                    Database.addGameToHistory(loggedinPlayer.getUsername(), "multi", game.getTeam1CorrectGuesses());

                    game.saveScores();
                }
            }
            else{
                out = "Your Team Score is: " + game.getTeam2CorrectGuesses();
                writer.write(out);
                writer.newLine();
                writer.flush();
                if(!playerExited) {
                    while (!saveScore) {
                        System.out.print("");
                    }


                    loggedinPlayer.addToMultiPlayerGamesHistory(game.getTeam2CorrectGuesses());
                    Database.addGameToHistory(loggedinPlayer.getUsername(), "multi", game.getTeam2CorrectGuesses());

                    game.saveScores();
                }
                playerExited = false;
                playerPlayed = false;
                turn = false;
                gameStarted = false;
                saveScore = false;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void scoreHistory() {
        try {

            String out, in;

            if(loggedinPlayer.getMultiPlayerGamesHistory().size()==0 && loggedinPlayer.getSinglePlayerGamesHistory().size()==0){
                out = "You have not played any games yet";
                writer.write(out);
                writer.newLine();
                writer.flush();

            }
            else{
                if(loggedinPlayer.getSinglePlayerGamesHistory().size()>0) {
                    out = "Your single player games history is: ";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();

                    for (int i = 0; i < loggedinPlayer.getSinglePlayerGamesHistory().size(); i++) {
                        out = "Game " + (i + 1) + ": " + loggedinPlayer.getSinglePlayerGamesHistory().get(i).toString();
                        writer.write(out);
                        writer.newLine();
                        writer.flush();
                    }
                }

                if( loggedinPlayer.getMultiPlayerGamesHistory().size()>0) {
                    out = "Your multiplayer games history is: ";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();

                    for (int i = 0; i < loggedinPlayer.getMultiPlayerGamesHistory().size(); i++) {
                        out = "Game " + (i + 1) + ": " + loggedinPlayer.getMultiPlayerGamesHistory().get(i).toString();
                        writer.write(out);
                        writer.newLine();
                        writer.flush();
                    }
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            String out, in;

            out = "Welcome to hangman";
            writer.write(out);
            writer.newLine();
            writer.flush();

            while (true) {

                out = "Please choose one of the following:";
                writer.write(out);
                writer.newLine();
                writer.flush();

                out = "1. Login";
                writer.write(out);
                writer.newLine();
                writer.flush();

                out = "2. Register";
                writer.write(out);
                writer.newLine();
                writer.flush();

                out= "Enter your choice: ";
                writer.write(out);
                writer.newLine();
                writer.flush();

                in = reader.readLine();
                if(Integer.parseInt(in) == 1 || Integer.parseInt(in) == 2)
                {
                    break;
                }
                else {
                    out = "Invalid input";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                }
            }
            if (in.equalsIgnoreCase("1")) {
                login();
            } else if (in.equalsIgnoreCase("2")) {
                register();
            }

            //for continuous playing
            while (true) {

                //for valdating input
                while (true) {

                    out = "Please choose one of the following:";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();

                    out = "1. Singleplayer";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();

                    out = "2. Multiplayer";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();

                    out = "3. Score History";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();

                    out = "4. Logout";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();

                    out= "Enter your choice: ";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();

                    in = reader.readLine();

                    if(Integer.parseInt(in) == 1 || Integer.parseInt(in) == 2 || Integer.parseInt(in) == 3 || Integer.parseInt(in) == 4)
                    {
                        break;
                    }
                    else {
                        out = "Invalid input";
                        writer.write(out);
                        writer.newLine();
                        writer.flush();
                    }
                }
                if (in.equalsIgnoreCase("1")) {
                    singlePlayerGame();
                }
                else if (in.equalsIgnoreCase("2")) {
                    multiPlayerGame();
                }
                else if (in.equalsIgnoreCase("3")) {
                    scoreHistory();
                }
                else if (in.equalsIgnoreCase("4")) {
                    out = "Thank you for playing!";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();

                    Database.removeLoggedInPlayer(loggedinPlayer);

                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public boolean isPlayerPlayed() {
        return playerPlayed;
    }

    public void setPlayerPlayed(boolean playerPlayed) {
        this.playerPlayed = playerPlayed;
    }

    public boolean isPlayerExited() {
        return playerExited;
    }

    public void setPlayerExited(boolean playerExited) {
        this.playerExited = playerExited;
    }

    public static void main(String[] args) throws Exception {
        int port = 6766;
        System.out.println("Server is running...");
        ServerSocket serverSocket = new ServerSocket(port);
        Database.loadFiles();
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                Server server = new Server();
                server.setSocket(socket);
                server.setBufferedWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
                server.setBufferedReader(new BufferedReader(new InputStreamReader(socket.getInputStream())));
                t = new Thread(server);
                t.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
