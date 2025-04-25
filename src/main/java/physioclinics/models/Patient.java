package physioclinics.models;

import java.util.ArrayList;
import java.util.List;

public class Patient {
    private String id;
    private String name;
    private String address; // New field for patient's address
    private String telephoneNumber; // New field for patient's phone number
    private List<TreatmentAppointment> appointments;

    // Constructor
    public Patient(String id, String name, String address, String telephoneNumber) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
        this.appointments = new ArrayList<>();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    // Methods
    public void bookAppointment(TreatmentAppointment appointment) {
        if (!appointments.contains(appointment)) {
            appointments.add(appointment);
        } else {
            System.out.println("This appointment is already booked by the patient.");
        }
    }

    public void cancelAppointment(TreatmentAppointment appointment) {
        if (appointments.contains(appointment)) {
            appointments.remove(appointment);
        } else {
            System.out.println("This appointment was not found in the patient's records.");
        }
    }

    @Override
    public String toString() {
        return "Patient{" +
                "ID='" + id + '\'' +
                ", Name='" + name + '\'' +
                ", Address='" + address + '\'' +
                ", Telephone='" + telephoneNumber + '\'' +
                '}';
    }
}