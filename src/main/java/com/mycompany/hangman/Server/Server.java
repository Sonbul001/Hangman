package com.mycompany.hangman.Server;

import com.mycompany.hangman.Database.Database;
import com.mycompany.hangman.Game.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server implements Runnable {

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

    //protected boolean teamFull = false;
    protected Player loggedinPlayer = null;


    

    public static void loadFiles() {
        creds = new File("credentials.txt");
        history = new File("history.txt");
        config = new File("config.txt");
        lookup = new File("lookup.txt");
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

    public static String register(String username, String password, String name) {
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
                return "Username already exists";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Welcome " + name;
    }

    public static String login(String username, String password) {
        try {
            Scanner reader = new Scanner(creds);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                String[] parsed = data.split(" ");
                if (parsed[0].equals(username)) {
                    if (parsed[1].equals(password)) {
                        return "Welcome " + parsed[2];
                    } else {
                        return "401 UNAUTHORIZED";
                    }
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "404 NOT FOUND";
    }

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

    public void MultiplayerGame(){
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String out, in;

            out = "Please choose one of the following:";
            writer.write(out);
            writer.newLine();
            writer.flush();

            HangmanMulti game = null;

            out = "1. Create Game";
            writer.write(out);
            writer.newLine();
            writer.flush();

            out = "2. Join Game";
            writer.write(out);
            writer.newLine();
            writer.flush();

            in = reader.readLine();
            if (in.equalsIgnoreCase("1")) {
                out = "Please enter the name of the team";
                writer.write(out);
                writer.newLine();
                writer.flush();

                in = reader.readLine();
                String teamName = in;

                out = "Please enter the size of the team";
                writer.write(out);
                writer.newLine();
                writer.flush();

                in = reader.readLine();
                int teamSize = Integer.parseInt(in);

                Team team = new Team(teamName,loggedinPlayer, teamSize);
                Database.addTeam(team);

                out = "Team Code is: " + team.getCode();
                writer.write(out);
                writer.newLine();
                writer.flush();

                while (team.getPlayers().size()!=teamSize) {
                    System.out.print("");
                }

                out = "Team is full";
                writer.write(out);
                writer.newLine();
                writer.flush();

                out="Please enter ready to search for opponent";
                writer.write(out);
                writer.newLine();
                writer.flush();

                in = reader.readLine();
                if(in.equalsIgnoreCase("ready")){
                    String word = Database.getWord();
                    game = Database.createMultiplayerGame(teamSize,team,word);

                    out = "Game has been created";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();

                    out = "Please wait for opponent to join";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                }
            } else if (in.equalsIgnoreCase("2")) {
                out = "Please enter the code of the team";
                writer.write(out);
                writer.newLine();
                writer.flush();

                in = reader.readLine();
                int code = Integer.parseInt(in);
                Team team = Database.joinTeam(loggedinPlayer,code);

                out = "Please wait for Leader to start the game";
                writer.write(out);
                writer.newLine();
                writer.flush();

                while(Database.getMultiGamePlayer(loggedinPlayer)==null){System.out.print("");}
                game = Database.getMultiGamePlayer(loggedinPlayer);

            }
 
            while(!gameStarted){System.out.print("");}

            out = "Game has started";
            writer.write(out);
            writer.newLine();
            writer.flush();

            out = "The hidden word is: " + game.getHiddenWord();
            writer.write(out);
            writer.newLine();
            writer.flush();


            while(!game.isGameOver()){
                while (!turn) {
                    if(game.isGameOver()) {
                        break;
                    }
                    String name;
                    if(game.getTeamTurn()==1) {
                        name = game.getTeam1().getPlayers().get(game.getTurn()-1).getName();
                        out = "it's " + name + "'s  turn";
                        writer.write(out);
                        writer.newLine();
                        writer.flush();
                    }else{
                        name = game.getTeam2().getPlayers().get(game.getTurn()-1).getName();
                        out = "it's " + name + "'s  turn";
                        writer.write(out);
                        writer.newLine();
                        writer.flush();
                    }
                    while(!playerPlayed){System.out.print("");}
                    playerPlayed = false;
                    out = name + " has guessed " + game.getGuessedLetters().get(game.getGuessedLetters().size()-1);
                    writer.write(out);
                    writer.newLine();
                    writer.flush();

                    out = "The hidden word is: " + game.getHiddenWord();
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                }
                if(game.getTeamTurn()==1)
                    out = "You have " + game.getTeam1IncorrectGuesses() + " incorrect guesses left";
                else
                    out = "You have " + game.getTeam2IncorrectGuesses() + " incorrect guesses left";

                writer.write(out);
                writer.newLine();
                writer.flush();

                out = "Please enter a letter";
                writer.write(out);
                writer.newLine();
                writer.flush();

                in = reader.readLine();
                char letter = in.charAt(0);

                if(game.checkGuess(letter,loggedinPlayer))
                {
                    out = "Correct";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                }

                else
                {
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
            out = "GAME OVER";
            writer.write(out);
            writer.newLine();
            writer.flush();

            out = "TEAM " + game.getWinner().getTeamName() + " HAS WON";
            writer.write(out);
            writer.newLine();
            writer.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void run() {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String out, in;

            out = "Welcome to hangman";
            writer.write(out);
            writer.newLine();
            writer.flush();

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

            in = reader.readLine();
            if (in.equalsIgnoreCase("1")) {
                out = "Please enter your username and password separated by a space";
                writer.write(out);
                writer.newLine();
                writer.flush();

                // input format should be (username password)
                in = reader.readLine();
                String[] info = in.split(" ");
                System.out.println(info[0]);
                System.out.println(info[1]);
                Player result = Database.getPlayer(info[0], info[1]);
                if(result == null){
                    writer.write("Wrong Username or Password");
                    writer.newLine();
                    writer.flush();
                }else{
                    writer.write("Welcome " + result.getName());
                    writer.newLine();
                    writer.flush();
                    loggedinPlayer = result;
                    loggedinPlayer.setServer(this);
                }
            } else if (in.equalsIgnoreCase("2")) {
                out = "Please enter your username, password, and name separated by a space";
                writer.write(out);
                writer.newLine();
                writer.flush();

                // input format should be (username password name)
                in = reader.readLine();
                String[] info = in.split(" ");
                System.out.println(info[0]);
                System.out.println(info[1]);
                String result = register(info[0], info[1], info[2]);
                writer.write(result);
                writer.newLine();
                writer.flush();
            }else{
                
            }
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
            
            in = reader.readLine();
            if(in.equalsIgnoreCase("1")){
                String word = Database.getWord();
                HangmanSingle game = new HangmanSingle(loggedinPlayer, word);
                out = "Your word is " + game.getHiddenWord();
                writer.write(out);
                writer.newLine();
                writer.flush();

                out = "You have " + incorrectGuesses + " remaining guesses";
                writer.write(out);
                writer.newLine();
                writer.flush();


                while(game.getIncorrectGuesses()!=0&&!game.getHiddenWord().equalsIgnoreCase(word)) {
                    out = "Please guess a letter";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                    in = reader.readLine();

                    char guessCharacter = in.charAt(0);

                    if(game.checkGuess(guessCharacter)){
                        out = "Correct!";
                        writer.write(out);
                        writer.newLine();
                        writer.flush();
                    }else{
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
                if(game.getHiddenWord().equalsIgnoreCase(word)) {
                    out = "You win!";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                }else{
                    out = "You lose!";
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                }

                out = "Your score is " + game.getCorrectGuesses();
                writer.write(out);
                writer.newLine();
                writer.flush();

                loggedinPlayer.addToSinglePlayerGamesHistory(game.getCorrectGuesses());
                Database.addGameToHistory(loggedinPlayer.getUsername(),"single" ,game.getCorrectGuesses());

            } else if(in.equalsIgnoreCase("2")){
                MultiplayerGame();
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

    public static void main(String[] args) throws Exception {
        int port = 6766;
        System.out.println("Server is running...");
        ServerSocket serverSocket = new ServerSocket(port);
        loadFiles();
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                Server server = new Server();
                server.setSocket(socket);
                t = new Thread(server);
                t.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
