package com.mycompany.hangman.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {


    
    public static void main(String[] args) {
        String host = "localhost";
        int port = 6766;
        try {
            Socket socket = new Socket(host, port);
            System.out.println("Connection Successful");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String out, in;
            Scanner input = new Scanner(System.in);

            // reads the welcome message
            in = reader.readLine();
            System.out.println(in);
            while (true) {
                // reads the menu
                in = reader.readLine();
                System.out.println(in);
                in = reader.readLine();
                System.out.println(in);
                in = reader.readLine();
                System.out.println(in);

                // types in response 1 for login 2 for register
                out = input.nextLine();
                writer.write(out);
                writer.newLine();
                writer.flush();

                if (out.equalsIgnoreCase("1") || out.equalsIgnoreCase("2")) {
                    break;
                }

                in = reader.readLine();
                System.out.println(in);
            }


            while(!in.contains("Welcome")) {
                // reads response for either login or register
                in = reader.readLine();
                System.out.println(in);

                // types in credentials
                out = input.nextLine();
                writer.write(out);
                writer.newLine();
                writer.flush();

                //receives response upon login or register
                in = reader.readLine();
                System.out.println(in);
            }
            //for continue playing
            while (true) {

                //for validating input
                while (true) {
                    // reads the menu
                    in = reader.readLine();
                    System.out.println(in);

                    //1. Singleplayer
                    in = reader.readLine();
                    System.out.println(in);

                    //2. Multiplayer
                    in = reader.readLine();
                    System.out.println(in);

                    //3. Score History
                    in = reader.readLine();
                    System.out.println(in);

                    //4. Exit
                    in = reader.readLine();
                    System.out.println(in);

                    // choose singleplayer or multiplayer or score history or exit
                    out = input.nextLine();
                    writer.write(out);
                    writer.newLine();
                    writer.flush();

                    if (out.equalsIgnoreCase("1") || out.equalsIgnoreCase("2") || out.equalsIgnoreCase("3") || out.equalsIgnoreCase("4"))
                        break;

                    in = reader.readLine();
                    System.out.println(in);
                }

                //single game
                if (out.equalsIgnoreCase("1")) {
                    //reads first input
                    in = reader.readLine();
                    System.out.println(in);
                    in = reader.readLine();
                    System.out.println(in);
                    in = reader.readLine();
                    System.out.println(in);

                    boolean exit = false;
                    while (!(in.equalsIgnoreCase("You win!")) && !(in.equalsIgnoreCase("You lose!") && !(in.equalsIgnoreCase("You exited the game")))) {
                        //enter the character
                        out = input.nextLine();
                        writer.write(out);
                        writer.newLine();
                        writer.flush();

                        //check if character is correct or not
                        in = reader.readLine();
                        System.out.println(in);
                        //check if user wants to exit the game
                        if (in.equalsIgnoreCase("You exited the game")) {
                            break;
                        }
                        in = reader.readLine();
                        System.out.println(in);
                        in = reader.readLine();
                        System.out.println(in);
                        in = reader.readLine();
                        System.out.println(in);
                    }
                    //print score
                    in = reader.readLine();
                    System.out.println(in);

                }

                //multiplayer game
                else if (out.equalsIgnoreCase("2")) {
                    while (true) {
                    //reads menu of create or join team
                        in = reader.readLine();
                        System.out.println(in);

                        in = reader.readLine();
                        System.out.println(in);

                        in = reader.readLine();
                        System.out.println(in);

                    // choose create or join team

                        out = input.nextLine();
                        writer.write(out);
                        writer.newLine();
                        writer.flush();

                        if (out.equalsIgnoreCase("1") || out.equalsIgnoreCase("2")) {
                            break;
                        }

                        in = reader.readLine();
                        System.out.println(in);


                    }


                    //create team
                    if (out.equalsIgnoreCase("1")) {

                        while (true) {
                            //enter name of the team
                            in = reader.readLine();
                            System.out.println(in);

                            out = input.nextLine();
                            writer.write(out);
                            writer.newLine();
                            writer.flush();

                            //check if team name is valid
                            in = reader.readLine();
                            System.out.println(in);
                            if (in.equalsIgnoreCase("Team name is valid"))
                                break;
                        }

                        while (true) {
                            //enter size of team
                            in = reader.readLine();
                            System.out.println(in);

                            out = input.nextLine();
                            writer.write(out);
                            writer.newLine();
                            writer.flush();

                            //check if size is valid
                            in = reader.readLine();
                            System.out.println(in);
                            if (in.equalsIgnoreCase("Team size is valid"))
                                break;
                        }

                        //wait to teammates to join the team
                        in = reader.readLine();
                        System.out.println(in);

                        //teammates joined the team
                        in = reader.readLine();
                        System.out.println(in);

                        //enter ready to start searching for opponent
                        in = reader.readLine();
                        System.out.println(in);

                        out = input.nextLine();
                        writer.write(out);
                        writer.newLine();
                        writer.flush();


                        //game created
                        in = reader.readLine();
                        System.out.println(in);

                        //search for opponent
                        in = reader.readLine();
                        System.out.println(in);
                    }

                    //join team
                    else {
                        while (true) {
                            //enter team code
                            in = reader.readLine();
                            System.out.println(in);

                            out = input.nextLine();
                            writer.write(out);
                            writer.newLine();
                            writer.flush();

                            //waiting game to start
                            in = reader.readLine();
                            System.out.println(in);

                            if(in.equalsIgnoreCase("Please wait for Leader to start the game"))
                                break;
                        }

                    }
                    //game started
                    in = reader.readLine();
                    System.out.println(in);

                    //read the hidden word
                    in = reader.readLine();
                    System.out.println(in);

                    boolean exit = false;
                    while (true) {
                        boolean yourturn = false;

                        //state whose turn
                        in = reader.readLine();
                        System.out.println(in);

                        if (in.contains("You"))
                            yourturn = true;

                        if (in.equalsIgnoreCase("GAME OVER"))
                            break;

                        //state player guesses
                        in = reader.readLine();
                        System.out.println(in);

                        if (in.equalsIgnoreCase("Game has been exited")) {
                            exit = true;
                            break;
                        }

                        if (yourturn) {
                            out = input.nextLine();
                            writer.write(out);
                            writer.newLine();
                            writer.flush();

                            in = reader.readLine();
                            System.out.println(in);

                            if (in.equalsIgnoreCase("Game has been exited")) {
                                exit = true;
                                break;
                            }
                        }
                        if (exit)
                            break;

                        in = reader.readLine();
                        System.out.println(in);

                    }

                    in = reader.readLine();
                    System.out.println(in);

                    in = reader.readLine();
                    System.out.println(in);
                }

                //score history
                else if (out.equalsIgnoreCase("3")) {
                    in = reader.readLine();
                    System.out.println(in);

                    while (!in.equalsIgnoreCase("You have not played any games yet")) {
                        in = reader.readLine();
                        if (in.equalsIgnoreCase("end"))
                            break;
                        System.out.println(in);
                    }
                }

                //exit
                else if (out.equalsIgnoreCase("4")) {
                    in = reader.readLine();
                    System.out.println(in);
                    break;
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
