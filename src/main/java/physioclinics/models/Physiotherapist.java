package physioclinics.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Physiotherapist within the PhysioClinicSystem.
 */
public class Physiotherapist {
    private String id;
    private String name;
    private String address;         // New field for address
    private String telephoneNumber; // New field for telephone number
    private String expertise;       // Area of expertise
    private List<TreatmentAppointment> availableAppointments;

    // Updated Constructor including address and telephone
    public Physiotherapist(String id, String name, String address, String telephoneNumber, String expertise) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.telephoneNumber = telephoneNumber;
        this.expertise = expertise;
        this.availableAppointments = new ArrayList<>();
    }

    // Getters and Setters for new fields
    public String getAddress() {
        return address;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    // Existing getters, setters and methods
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExpertise() {
        return expertise;
    }

    public List<TreatmentAppointment> getAvailableAppointments() {
        return availableAppointments;
    }

    public void addAvailableAppointment(TreatmentAppointment appointment) {
        if (!availableAppointments.contains(appointment)) {
            availableAppointments.add(appointment);
        } else {
            System.out.println("This appointment is already added for the physiotherapist.");
        }
    }

    public void removeAvailableAppointment(TreatmentAppointment appointment) {
        if (availableAppointments.contains(appointment)) {
            availableAppointments.remove(appointment);
        } else {
            System.out.println("This appointment was not found in the physiotherapist's records.");
        }
    }

    @Override
    public String toString() {
        return "Physiotherapist{" +
                "ID='" + id + '\'' +
                ", Name='" + name + '\'' +
                ", Address='" + address + '\'' +
                ", Telephone='" + telephoneNumber + '\'' +
                ", Expertise='" + expertise + '\'' +
                '}';
    }
}