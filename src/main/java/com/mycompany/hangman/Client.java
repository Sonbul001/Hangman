package com.mycompany.hangman;

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
            while(true){
                out = input.nextLine();
                if(out.equalsIgnoreCase("1") || out.equalsIgnoreCase("2"))
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
            if(in.equalsIgnoreCase("401 UNAUTHORIZED") || in.equalsIgnoreCase("404 NOT FOUND") || in.equalsIgnoreCase("Username already exists")){
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
            while(true){
                out = input.nextLine();
                if(out.equalsIgnoreCase("1") || out.equalsIgnoreCase("2"))
                    break;
                else
                    System.out.println("Wrong input");
            }
            writer.write(out);
            writer.newLine();
            writer.flush();            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
