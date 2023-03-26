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

            while (true) {
                in = reader.readLine();
                System.out.println(in);

                if(in.equalsIgnoreCase("Thank you for playing!"))
                    break;
                else if(in.toLowerCase().contains("enter")||in.toLowerCase().contains(" guess ")) {
                    out = input.nextLine();
                    writer.write(out);
                    writer.newLine();
                    writer.flush();
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
