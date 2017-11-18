package com.company;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

public class UDPClient {
    private static InetAddress address;
    private static byte[] buffer;
    private static DatagramPacket packet;
    private static String str;
    private static MulticastSocket socket;
    private static String FILE_NAME = "storage.txt";
    private static TCPServer server = new TCPServer();

    /**
     * Открываем файл на чтение и получаем все строки как лист.
     * Важно вначале получить и закрыть файл на чтение, иначе может произойти конфликт.
     *
     * @return - лист строк, записанных в файл ранее.
     */
    private static List<String> readFromStorage() {
        List<String> lines = new ArrayList<String>();
        try {
            String line = null;
            FileReader fileReader =
                    new FileReader(FILE_NAME);

            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }

            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            FILE_NAME + "'");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + FILE_NAME + "'");
        }
        return lines;
    }

    /**
     * Записываем на новую строку текст
     * @param bufferedWriter - записывальщик
     * @param message        - сообщение строки
     */
    private static void writeln(BufferedWriter bufferedWriter, String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
        } catch (IOException ex) {
            System.out.println(
                    "Error writing to file '"
                            + FILE_NAME + "'");
        }
    }

    /**
     * Получаем весь файл.
     * Записываем заново в него.
     * Записываем новую строку.
     *
     * @param message - сообщение от сервера.
     */
    private static void writeToStorage(String message) {
        try {
            List<String> list = readFromStorage();
            server.setItems(list);
            if (list.get(list.size() - 1).equals(message)) {
                System.out.println("Уже сообщали погоду :(");
                return;
            }

            FileWriter fileWriter =
                    new FileWriter(FILE_NAME);
            BufferedWriter bufferedWriter =
                    new BufferedWriter(fileWriter);

            list.add(message);
            for (String item : list) {
                writeln(bufferedWriter, item);
            }
            server.setItems(list);
            bufferedWriter.close();
        } catch (IOException ex) {
            System.out.println(
                    "Error writing to file '"
                            + FILE_NAME + "'");
        }
    }

    public static void main(String arg[]) throws Exception {
        System.out.println("Ожидание сообщения от сервера");
        try {
            socket = new MulticastSocket(1502);
            address = InetAddress.getByName("233.0.0.1");
            socket.joinGroup(address);

            while (true) {
                buffer = new byte[256];
                packet = new DatagramPacket(
                        buffer, buffer.length);

                socket.receive(packet);
                str = new String(packet.getData());
                System.out.println(
                        "Получено сообщение: " + str.trim());
                writeToStorage(str.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.leaveGroup(address);
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}