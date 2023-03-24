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

            // reads the menu
            in = reader.readLine();
            System.out.println(in);
            in = reader.readLine();
            System.out.println(in);
            in = reader.readLine();
            System.out.println(in);
            in = reader.readLine();
            System.out.println(in);

            // types in response 1 for login 2 for register
            while (true) {
                out = input.nextLine();
                if (out.equalsIgnoreCase("1") || out.equalsIgnoreCase("2"))
                    break;
                else
                    System.out.println("Wrong input");
            }
            writer.write(out);
            writer.newLine();
            writer.flush();

            // reads response for either login or register
            in = reader.readLine();
            System.out.println(in);
            out = input.nextLine();
            writer.write(out);
            writer.newLine();
            writer.flush();

            // types in credentials
            in = reader.readLine();
            System.out.println(in);
            if (in.equalsIgnoreCase("401 UNAUTHORIZED") || in.equalsIgnoreCase("404 NOT FOUND") || in.equalsIgnoreCase("Username already exists")) {
                System.exit(0);
            }

            // reads the menu
            in = reader.readLine();
            System.out.println(in);
            in = reader.readLine();
            System.out.println(in);
            in = reader.readLine();
            System.out.println(in);

            // choose singleplayer or multiplayer
            while (true) {
                out = input.nextLine();
                if (out.equalsIgnoreCase("1") || out.equalsIgnoreCase("2"))
                    break;
                else
                    System.out.println("Wrong input");
            }
            writer.write(out);
            writer.newLine();
            writer.flush();

            //single game
            if (out.equalsIgnoreCase("1")) {
                //reads first input 
                in = reader.readLine();
                System.out.println(in);
                in = reader.readLine();
                System.out.println(in);
                in = reader.readLine();
                System.out.println(in);
                
                while (!(in.equalsIgnoreCase("You win!")) && !(in.equalsIgnoreCase("You lose!"))) {
                    //enter the character
                    out = input.nextLine();
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                    
                    //check if character is correct or not
                    in = reader.readLine();
                    System.out.println(in);
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
           
            //multiplayer game
            } else {
                //reads menu of create or join game
                in = reader.readLine();
                System.out.println(in);

                in = reader.readLine();
                System.out.println(in);

                in = reader.readLine();
                System.out.println(in);

                //responds to the menu
                out = input.nextLine();
                writer.write(out);
                writer.newLine();
                writer.flush();
                
                //create team
                if(out.equalsIgnoreCase("1")) {
                
                    //enter name of the team
                    in = reader.readLine();
                    System.out.println(in);

                    out = input.nextLine();
                    writer.write(out);
                    writer.newLine();
                    writer.flush();

                    //enter size of team
                    in = reader.readLine();
                    System.out.println(in);

                    out = input.nextLine();
                    writer.write(out);
                    writer.newLine();
                    writer.flush();

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

                   
                }

                in = reader.readLine();
                System.out.println(in);

                in = reader.readLine();
                System.out.println(in);


                while (true)
                {
                    boolean yourturn = false;
                    in = reader.readLine();
                    System.out.println(in);

                    if(in.contains("You"))
                        yourturn = true;

                    if(in.equalsIgnoreCase("GAME OVER"))
                        break;

                    in = reader.readLine();
                    System.out.println(in);

                    if(yourturn)
                    {
                        out = input.nextLine();
                        writer.write(out);
                        writer.newLine();
                        writer.flush();

                        in = reader.readLine();
                        System.out.println(in);
                    }

                    in = reader.readLine();
                    System.out.println(in);

                }

                in = reader.readLine();
                System.out.println(in);


            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
