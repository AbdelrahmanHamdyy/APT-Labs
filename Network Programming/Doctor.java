package com.company;

public class Doctor {

    private int id;
    private String name;
    private boolean[] timeslots;
    private String[] patients;

    public int checkAvailability(int index) {
        if (index >= timeslots.length)
            return 1;
        if (!timeslots[index])
            return 2;
        return 0;
    }

    public void bookApp(int index, String name) {
        timeslots[index] = false;
        patients[index] = name;
    }

    public boolean removeApp(int index, String name) {
        if (patients[index].equals(name)) {
            timeslots[index] = true;
            patients[index] = "";
            return true;
        }
        return false;
    }

    public void print() {
        System.out.println("ID -> " + id);
        System.out.println("Name -> " + name);
        System.out.println("Patients:");
        for (int i = 0; i < timeslots.length; i++) {
            if (!timeslots[i])
                System.out.println("Patient: " + patients[i] + " | Time: " + i);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean[] getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(boolean[] timeslots) {
        this.timeslots = timeslots;
    }

    public String[] getPatients() {
        return patients;
    }

    public void setPatients(String[] patients) {
        this.patients = patients;
    }

}
