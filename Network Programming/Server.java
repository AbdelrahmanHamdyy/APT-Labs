package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    private static final Hospital hospital = new Hospital();
    public static final int MAKE_APPOINTMENT_PORT = 6666;
    public static final int CANCEL_APPOINTMENT_PORT = 6667;
    public static int clientNumber = 0;

    public static void main(String[] args) throws IOException {
        System.out.println("------- Server started.. -------");
        // Make Appointment
        new Thread(() -> {
            try {
                ServerSocket ss = new ServerSocket(MAKE_APPOINTMENT_PORT);
                while (true) {
                    new Appointment(ss.accept(), clientNumber).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        
        // Cancel Appointment
        new Thread(() -> {
            try {
                ServerSocket ss = new ServerSocket(CANCEL_APPOINTMENT_PORT);
                while (true) {
                    new Appointment(ss.accept(), clientNumber++).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static class Appointment extends Thread {
        Socket socket;
        int clientNo;

        public Appointment(Socket s, int clientNo) {
            this.socket = s;
            this.clientNo = clientNo;
            System.out.println("Connection with Client #" + clientNo + " at socket " + socket);
        }

        public void run() {
            try {
                Scanner Reader = new Scanner(this.socket.getInputStream());
                PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);

                String patientName = null;
                patientName = Reader.nextLine();
                while (true) {
                    int choice = Reader.nextInt();
                    if (choice == 3)
                        break;
                    int id = Reader.nextInt();
                    int timeSlotIndex = Reader.nextInt();
                    // System.out.println("Choice: " + choice + " | ID: " + id + " | Time: " + timeSlotIndex);
                    if (this.socket.getLocalPort() == MAKE_APPOINTMENT_PORT && choice == 1) {
                        int action = hospital.MakeAppointment(id, timeSlotIndex, patientName);
                        if (action == 0)
                            out.println("Making the appointment is done successfully (Success)");
                        else if (action == 1)
                            out.println("The timeslot index is out of boundary (Failure)");
                        else if (action == 2)
                            out.println("The doctor is already busy at this timeslot (Failure)");
                        else
                            out.println("The doctor id is not found in the hospital (Failure)");
                        hospital.print();
                    }
                    else if (this.socket.getLocalPort() == CANCEL_APPOINTMENT_PORT && choice == 2) {
                        int action = hospital.CancelAppointment(id, timeSlotIndex, patientName);
                        if (action == 0)
                            out.println("Cancelling the appointment is done successfully (Success)");
                        else if (action == 1)
                            out.println("The timeslot index is out of boundary (Failure)");
                        else if (action == 2)
                            out.println("The doctor has an appointment to a different patient name at this timeslot (Failure)");
                        else if (action == 3)
                            out.println("The doctor doesn’t have an appointment at this timeslot (Failure)");
                        else
                            out.println("The doctor id is not found in hospital (Failure)");
                        hospital.print();
                    }
                }
                out.close();
                Reader.close();
            } catch (IOException e) {
                System.out.println("Error handling client# " + this.clientNo + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Couldn't close a socket, what's going on?");
                }
                System.out.println("Connection with Client# " + this.clientNo + " at socket " + socket + " closed..");
            }
        }
    }
}
