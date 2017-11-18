package com.company;

// UDP Server

import java.io.*;

import java.net.*;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class UDPServer {
    private BufferedReader in = null;
    private String str = null;
    private byte[] buffer;
    private DatagramPacket packet;
    private InetAddress address;
    private DatagramSocket socket;
    /**
     * Константа таймера на 10 минут.
     */
    private Integer TIMER_DELAY = 10 * 60 * 1000;
//    private Integer TIMER_DELAY = 3 * 1000;

    /**
     * Создаем сокет
     *
     * @throws IOException - ошибочка
     */
    public UDPServer() throws IOException {
        System.out.println("Sending messages");
        socket = new DatagramSocket();
        createTimer();
    }

    /**
     * Отправляем сообщение всем клиентам, которые зарегистировались
     */
    private void transmit(DateMessage message) {
        try {
            in = new BufferedReader(new InputStreamReader(System.in));
            buffer = message.toString().getBytes();
            address = InetAddress.getByName("233.0.0.1");

            packet = new DatagramPacket(
                    buffer,
                    buffer.length,
                    address,
                    1502);
            socket.send(packet);
        } catch (Exception e) {
            try {
                in.close();
                socket.close();
            } catch (Exception error) {
                error.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void writeWeather() {
        DateMessage dateMessage = new DateMessage(
                Calendar.getInstance().getTime(),
                "Погода хорошая в");
        transmit(dateMessage);
    }

    private void createTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                writeWeather();
                System.out.println("Отправил погодку");
            }
        }, 0, TIMER_DELAY);
    }

    /**
     * Запускаем сервер
     *
     * @throws Exception - ошибочки
     */
    public static void main(String arg[]) throws Exception {
        new UDPServer();
    }
}