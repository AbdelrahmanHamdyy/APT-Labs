package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final int MAKE_APPOINTMENT_PORT = 6666;
    private static final int CANCEL_APPOINTMENT_PORT = 6667;

    public static void main(String[] args) throws IOException {
        System.out.println("------- CLIENT -------");
        System.out.print("Name: ");
        Scanner Input = new Scanner(System.in);
        String name = Input.nextLine();

        Socket makeSocket = new Socket("localhost", MAKE_APPOINTMENT_PORT);
        Socket cancelSocket = new Socket("localhost", CANCEL_APPOINTMENT_PORT);

        PrintWriter makeOut = new PrintWriter(makeSocket.getOutputStream(), true);
        PrintWriter cancelOut = new PrintWriter(cancelSocket.getOutputStream(), true);

        BufferedReader makeIn = new BufferedReader(new InputStreamReader(makeSocket.getInputStream()));
        BufferedReader cancelIn = new BufferedReader(new InputStreamReader(cancelSocket.getInputStream()));

        makeOut.println(name);
        cancelOut.println(name);

        while (true) {
            String response = null;
            System.out.println("--------------------------------");
            System.out.println("Type 1 to make an appointment   |");
            System.out.println("Type 2 to cancel an appointment |");
            System.out.println("Type 3 to exit                  |");
            System.out.println("--------------------------------");
            System.out.print("Choice: ");
            int choice = Input.nextInt();
            if (choice == 3) {
                makeOut.println(choice);
                cancelOut.println(choice);
                System.out.println("Exiting..");
                break;
            }
            if (choice > 2 || choice < 1) {
                System.out.println("Invalid Choice! Please Try Again..");
                continue;
            }
            System.out.print("Doctor ID: ");
            int id = Input.nextInt();
            System.out.print("Time Slot index: ");
            int timeSlot = Input.nextInt();

            if (choice == 1) {
                makeOut.println(choice);
                makeOut.println(id);
                makeOut.println(timeSlot);
                System.out.println("----- MAKE [CLIENT] -----");
                if ((response = makeIn.readLine()) != null) {
                    System.out.println("Response message: " + response);
                }
            } else if (choice == 2) {
                cancelOut.println(choice);
                cancelOut.println(id);
                cancelOut.println(timeSlot);
                System.out.println("----- CANCEL [CLIENT] -----");
                if ((response = cancelIn.readLine()) != null) {
                    System.out.println("Response message: " + response);
                }
            }
        }

        makeOut.close();
        makeIn.close();
        makeSocket.close();

        cancelOut.close();
        cancelIn.close();
        cancelSocket.close();
    }
}
