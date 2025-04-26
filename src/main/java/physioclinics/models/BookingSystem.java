package physioclinics.models;

import java.util.ArrayList;
import java.util.List;

public class BookingSystem {
    // Lists to store patients, physiotherapists, and treatment appointments
    private List<physioclinics.models.Patient> patients;
    private List<physioclinics.models.Physiotherapist> physiotherapists;
    private List<physioclinics.models.TreatmentAppointment> appointments;

    public BookingSystem() {
        this.patients = new ArrayList<>();
        this.physiotherapists = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    public List<physioclinics.models.Patient> getPatients() {
        return patients;
    }

    public List<physioclinics.models.Physiotherapist> getPhysiotherapists() {
        return physiotherapists;
    }

    public List<physioclinics.models.TreatmentAppointment> getAppointments() {
        return appointments;
    }

    public void addPatient(physioclinics.models.Patient patient) {
        patients.add(patient);
    }

    public void addPhysiotherapist(physioclinics.models.Physiotherapist physiotherapist) {
        physiotherapists.add(physiotherapist);
    }
    // Book an available appointment by ID, associating it with the patient and updating the physiotherapist's schedule.
    public boolean bookExistingAppointment(String appointmentId, physioclinics.models.Patient patient) {
        for (physioclinics.models.TreatmentAppointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId) && appointment.getStatus().equals("available")) {
                appointment.setStatus("booked");
                appointment.setPatient(patient);
                patient.bookAppointment(appointment);
                appointment.getPhysiotherapist().removeAvailableAppointment(appointment);
                return true;
            }
        }
        System.out.println("Appointment not found or already booked!");
        return false;
    }
    // Cancel a booked appointment by ID, updating its status and the patient's record.

    public boolean cancelAppointment(String appointmentId, physioclinics.models.Patient patient) {
        physioclinics.models.TreatmentAppointment appointmentToCancel = null;

        // Locate the appointment
        for (physioclinics.models.TreatmentAppointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                appointmentToCancel = appointment;
                break;
            }
        }

        // Check if the appointment exists and is booked
        if (appointmentToCancel == null || !"booked".equals(appointmentToCancel.getStatus())) {
            return false;
        }
        appointmentToCancel.setStatus("cancelled");
        patient.cancelAppointment(appointmentToCancel);

        // Remove from the physiotherapist's available list if still present
        if (appointmentToCancel.getPhysiotherapist().getAvailableAppointments().contains(appointmentToCancel)) {
            appointmentToCancel.getPhysiotherapist().removeAvailableAppointment(appointmentToCancel);
        }
        return true;
    }

    // Change an appointment booking: cancel the old one and book a new appointment with a different physiotherapist.
    public boolean changeBooking(String oldBookingId, String newPhysioId, String newBookingId, physioclinics.models.Patient patient) {
        // Cancel the current appointment
        boolean cancelled = cancelAppointment(oldBookingId, patient);
        if (!cancelled) {
            System.out.println("Error: Could not cancel the old appointment.");
            return false;
        }

        // Find the new physiotherapist
        physioclinics.models.Physiotherapist newPhysio = findPhysiotherapistById(newPhysioId);
        if (newPhysio == null) {
            System.out.println("Error: No physiotherapist found with ID " + newPhysioId + "!");
            return false;
        }

        // Search for the new available appointment in the physiotherapist's list
        physioclinics.models.TreatmentAppointment newAppointment = null;
        for (physioclinics.models.TreatmentAppointment app : newPhysio.getAvailableAppointments()) {
            if (app.getAppointmentId().equalsIgnoreCase(newBookingId)) {
                newAppointment = app;
                break;
            }
        }
        if (newAppointment == null) {
            System.out.println("Error: The new appointment (" + newBookingId + ") is not available for physiotherapist " + newPhysio.getName() + ".");
            return false;
        }

        // Book the new appointment
        newAppointment.setStatus("booked");
        newAppointment.setPatient(patient);
        patient.bookAppointment(newAppointment);
        newPhysio.removeAvailableAppointment(newAppointment);
        return true;
    }


    // Helper method: find and return a physiotherapist by ID.
    physioclinics.models.Physiotherapist findPhysiotherapistById(String physioId) {
        for (physioclinics.models.Physiotherapist physio : physiotherapists) {
            if (physio.getId().equalsIgnoreCase(physioId)) {
                return physio;
            }
        }
        return null;
    }

    // Generate a detailed report of treatment appointments and rank physiotherapists by attended sessions.
    public void generateReport() {
        System.out.println("==== Treatment Appointments Report ====\n");

        // Report each physiotherapist's appointments
        for (physioclinics.models.Physiotherapist physio : physiotherapists) {
            System.out.println("Physiotherapist: " + physio.getName() +
                    " (Expertise: " + physio.getExpertise() + ")");
            boolean hasAppointment = false;
            for (physioclinics.models.TreatmentAppointment appointment : appointments) {
                if (appointment.getPhysiotherapist().getId().equalsIgnoreCase(physio.getId())) {
                    hasAppointment = true;
                    String treatmentName = physio.getExpertise();
                    String patientName = (appointment.getPatient() != null) ? appointment.getPatient().getName() : "Not Booked";

                    System.out.println("  Appointment ID: " + appointment.getAppointmentId());
                    System.out.println("    Treatment Name: " + appointment.getTreatmentName());
                    System.out.println("    Patient: " + patientName);
                    System.out.println("    Date/Time: " + appointment.getDateTime());
                    System.out.println("    Status: " + appointment.getStatus());
                    System.out.println("-------------------------------------------------");
                }
            }
            if (!hasAppointment) {
                System.out.println("  No appointments scheduled for this physiotherapist.");
            }
            System.out.println(); // Blank line for separation
        }

        // Sort and rank physiotherapists based on attended appointment count
        List<physioclinics.models.Physiotherapist> sortedPhysios = new ArrayList<>(physiotherapists);
        sortedPhysios.sort((p1, p2) -> {
            int count1 = (int) appointments.stream()
                    .filter(a -> a.getPhysiotherapist().getId().equalsIgnoreCase(p1.getId())
                            && "attended".equalsIgnoreCase(a.getStatus()))
                    .count();
            int count2 = (int) appointments.stream()
                    .filter(a -> a.getPhysiotherapist().getId().equalsIgnoreCase(p2.getId())
                            && "attended".equalsIgnoreCase(a.getStatus()))
                    .count();
            return Integer.compare(count2, count1);
        });

        System.out.println("==== Physiotherapists Ranked by Attended Appointments ====");
        int rank = 1;
        for (physioclinics.models.Physiotherapist physio : sortedPhysios) {
            int attendedCount = (int) appointments.stream()
                    .filter(a -> a.getPhysiotherapist().getId().equalsIgnoreCase(physio.getId())
                            && "attended".equalsIgnoreCase(a.getStatus()))
                    .count();
            System.out.println(rank + ". " + physio.getName() + " - " + attendedCount + " attended appointments");
            rank++;
        }
        System.out.println("===========================================================\n");
    }
}