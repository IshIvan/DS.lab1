package com.company;

// TCP Client

import java.io.ObjectInputStream;
import java.net.Socket;

public class TCPClient {
    public static void main(String args[]) {
        try {
            Socket clientSocket = new Socket("localhost", 1500);
            ObjectInputStream in =
                    new ObjectInputStream(clientSocket.getInputStream());
            String[] dateMessages =
                    (String[]) in.readObject();
            clientSocket.close();
            System.out.println("Погодка на ближайщее время:");
            for (String item : dateMessages) {
                System.out.println(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}