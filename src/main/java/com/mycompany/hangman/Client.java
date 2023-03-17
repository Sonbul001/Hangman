package com.mycompany.hangman;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String out, in;
            Scanner input = new Scanner(System.in);

            in = reader.readLine();
            System.out.println(in);
            in = reader.readLine();
            System.out.println(in);
            in = reader.readLine();
            System.out.println(in);
            in = reader.readLine();
            System.out.println(in);
            
            out = input.nextLine();
            writer.write(out);
            writer.newLine();
            writer.flush();

            in = reader.readLine();
            System.out.println(in);
            out = input.nextLine();
            writer.write(out);
            writer.newLine();
            writer.flush();

            in = reader.readLine();
            System.out.println(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
