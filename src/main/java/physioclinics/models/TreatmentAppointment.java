package physioclinics.models;

import java.time.LocalDateTime;

public class TreatmentAppointment {
    private String appointmentId;
    private LocalDateTime dateTime;
    private String status; // "booked", "attended", or "cancelled"
    private physioclinics.models.Patient patient;
    private physioclinics.models.Physiotherapist physiotherapist;
    private String treatmentName; // New field for the treatment name

    // Updated Constructor with treatmentName parameter
    public TreatmentAppointment(String appointmentId, LocalDateTime dateTime, String status, physioclinics.models.Patient patient, physioclinics.models.Physiotherapist physiotherapist, String treatmentName) {
        this.appointmentId = appointmentId;
        this.dateTime = dateTime;
        this.status = status;
        this.patient = patient;
        this.physiotherapist = physiotherapist;
        this.treatmentName = treatmentName;
    }

    // Alternatively, you could add an overloaded constructor if you want to leave treatmentName unset sometimes.
    // Getters and setters
    public String getAppointmentId() {
        return appointmentId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status.equals("booked") || status.equals("attended") || status.equals("cancelled") || status.equals("available")) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Invalid status. Allowed values: booked, attended, cancelled, available.");
        }
    }

    public physioclinics.models.Patient getPatient() {
        return patient;
    }

    public void setPatient(physioclinics.models.Patient patient) {
        this.patient = patient;
    }

    public physioclinics.models.Physiotherapist getPhysiotherapist() {
        return physiotherapist;
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    @Override
    public String toString() {
        return "TreatmentAppointment{" +
                "ID='" + appointmentId + '\'' +
                ", DateTime=" + dateTime +
                ", Status='" + status + '\'' +
                ", Patient=" + (patient != null ? patient.getName() : "None") +
                ", Physiotherapist=" + (physiotherapist != null ? physiotherapist.getName() : "None") +
                ", TreatmentName='" + treatmentName + '\'' +
                '}';
    }
}