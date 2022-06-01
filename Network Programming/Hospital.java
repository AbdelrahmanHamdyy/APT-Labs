package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Hospital {

    private List<Doctor> doctors;

    public Hospital() {
        ReadDoctors();
    }

    public int MakeAppointment(int id, int timeSlotIndex, String patientName) {
        for (Doctor doc : doctors) {
            if (id == doc.getId()) {
                int available = doc.checkAvailability(timeSlotIndex);
                if (available == 0) {
                    doc.bookApp(timeSlotIndex, patientName);
                    return 0;
                }
                else
                    return available;
            }
        }
        return 3;
    }

    public int CancelAppointment(int id, int timeSlotIndex, String patientName) {
        for (Doctor doc : doctors) {
            if (id == doc.getId()) {
                int available = doc.checkAvailability(timeSlotIndex);
                if (available == 2) {
                    if (doc.removeApp(timeSlotIndex, patientName))
                        return 0; // Canceled
                    else
                        return 2; // Wrong patient name
                }
                else if (available == 0)
                    return 3;
                else if (available == 1)
                    return 1;
            }
        }
        return 4; // ID not found
    }

    public void print() {
        System.out.println("Doctors in the hospital:");
        for (Doctor doc : doctors) {
            doc.print();
            System.out.println("------------------------------");
        }
    }

    public void ReadDoctors() {
        try {
            File file = new File("doctors.txt");
            Scanner Reader = new Scanner(file);
            doctors = new ArrayList<>();
            while (Reader.hasNextLine()) {
                String doctor = Reader.nextLine();
                String[] s = doctor.split(" ");
                Doctor D = new Doctor();
                boolean[] timeSlots = new boolean[Integer.parseInt(s[1])];
                Arrays.fill(timeSlots, true);
                String[] patients = new String[Integer.parseInt(s[1])];
                D.setId(Integer.parseInt(s[0]));
                D.setPatients(patients);
                D.setTimeslots(timeSlots);
                StringBuilder name = new StringBuilder();
                for (int i = 2; i < s.length; i++) {
                    if (i != s.length - 1)
                        name.append(s[i] + " ");
                    else
                        name.append(s[i]);
                }
                D.setName(name.toString());
                doctors.add(D);
            }
            Reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error!");
            e.printStackTrace();
        }
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }
}
