package com.company;

//TCP Server

import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * Класс сервера (выполняется в отдельном процессе)
 */

public class TCPServer extends Thread {
    ServerSocket serverSocket = null;
    /**
     * Последние 5 или менее строк.
     */
    private List<String> items;

    /**
     * Конструктор по умолчанию
     */
    public TCPServer() {
        try {
            serverSocket = new ServerSocket(1500);
            System.out.println("Starting the server ");
            start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Получаем последние 5 элементов
     * @param list - лист, из которого получить 5 элементов
     * @return - 5 или менее элементов
     */
    private List<String> getLastFiveElements(List<String> list) {
        Integer size = list.size();
        return list.subList(Math.max(size - 5, 0), size);
    }

    /**
     * Получить лист
     */
    public List<String> getItems() {
        return items;
    }

    /**
     * Задаем лист из 5 или менее строк
     * @param list - данные от сервера
     */
    public void setItems(List<String> list) {
        items = getLastFiveElements(list);
    }

    /**
     * Отправляем погоду на клиент
     * @param clientSocket - сокет клиента
     */
    private void pushWeather(Socket clientSocket) {
        try {
            ObjectOutputStream out =
                    new ObjectOutputStream(
                            clientSocket.getOutputStream());
            String[] array = getItems().toArray(new String[getItems().size()]);
            out.writeObject(array);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Запуск процесса.
     * Обработка всех клиентов.
     */
    public void run() {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection accepted from " + clientSocket.getInetAddress().getHostAddress());
                pushWeather(clientSocket);
                clientSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}