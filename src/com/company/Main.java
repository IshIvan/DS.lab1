// Ishmametyev lab1 DS
// November 18, 2017

package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            UDPClient client = new UDPClient();
            UDPServer server = new UDPServer();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
