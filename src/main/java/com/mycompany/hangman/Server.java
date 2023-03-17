package com.mycompany.hangman;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server implements Runnable {

    protected Socket socket;

    public static String register(String username, String password, String name) {
        boolean exists = false;
        try {
            File creds = new File("credentials.txt");
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
                BufferedWriter writer = new BufferedWriter(new FileWriter("credentials.txt",true));
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
            File creds = new File("credentials.txt");
            Scanner reader = new Scanner(creds);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                String[] parsed = data.split(" ");
                if (parsed[0].equals(username) && parsed[1].equals(password)) {
                    return "Welcome " + parsed[2];
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "Incorrect Username Or Password";
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

                in = reader.readLine();
                String[] info = in.split(" ");
                System.out.println(info[0]);
                System.out.println(info[1]);
                String result = login(info[0], info[1]);
                writer.write(result);
                writer.newLine();
                writer.flush();
            }
            else if (in.equalsIgnoreCase("2")) {
                out = "Please enter your username and password and name separated by a space";
                writer.write(out);
                writer.newLine();
                writer.flush();
                
                in = reader.readLine();
                String[] info = in.split(" ");
                System.out.println(info[0]);
                System.out.println(info[1]);
                String result = register(info[0], info[1], info[2]);
                writer.write(result);
                writer.newLine();
                writer.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public static void main(String[] args) throws Exception {
        int port = 6766;
        System.out.println("Math Server is running...");
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                Server server = new Server();
                server.setSocket(socket);
                Thread t = new Thread(server);
                t.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
