package physioclinics.models;

import java.util.ArrayList;
import java.util.List;

public class BookingSystem {
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

    public boolean bookExistingAppointment(String appointmentId, physioclinics.models.Patient patient) {
        for (physioclinics.models.TreatmentAppointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId) && appointment.getStatus().equals("available")) {
                appointment.setStatus("booked");
                appointment.setPatient(patient);

                patient.bookAppointment(appointment);
                appointment.getPhysiotherapist().removeAvailableAppointment(appointment);

                //System.out.println("Appointment " + appointmentId + " booked successfully!");
                return true;
            }
        }
        System.out.println("Appointment not found or already booked!");
        return false;
    }

    public boolean cancelAppointment(String appointmentId, physioclinics.models.Patient patient) {
        physioclinics.models.TreatmentAppointment appointmentToCancel = null;

        // Find the appointment by ID.
        for (physioclinics.models.TreatmentAppointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                appointmentToCancel = appointment;
                break;
            }
        }

        // Check if the appointment exists and is in a booked status.
        if (appointmentToCancel == null || !"booked".equals(appointmentToCancel.getStatus())) {
            //System.out.println("Appointment not found or cannot be canceled.");
            return false;
        }

        // Mark the appointment as cancelled.
        appointmentToCancel.setStatus("cancelled");

        // Remove the appointment from the global list.
        //appointments.remove(appointmentToCancel);

        // Update the patient's records (assumes the patient passed in is the one who booked it).
        patient.cancelAppointment(appointmentToCancel);

        // Remove from the physiotherapist's available appointments, if present.
        if (appointmentToCancel.getPhysiotherapist().getAvailableAppointments().contains(appointmentToCancel)) {
            appointmentToCancel.getPhysiotherapist().removeAvailableAppointment(appointmentToCancel);
        }

        //System.out.println("Appointment canceled successfully!");
        return true;
    }

    // In BookingSystem.java

    public boolean changeBooking(String oldBookingId, String newPhysioId, String newBookingId, physioclinics.models.Patient patient) {
        // Step 1: Cancel the original appointment.
        boolean cancelled = cancelAppointment(oldBookingId, patient);
        if (!cancelled) {
            System.out.println("Error: Could not cancel the old appointment.");
            return false;
        }

        // Step 2: Find the new Physiotherapist by their ID.
        physioclinics.models.Physiotherapist newPhysio = findPhysiotherapistById(newPhysioId);
        if (newPhysio == null) {
            System.out.println("Error: No physiotherapist found with ID " + newPhysioId + "!");
            return false;
        }

        // Step 3: Look through the new physiotherapist's available appointments to find the one with the new booking ID.
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

        // Step 4: Book the new appointment.
        newAppointment.setStatus("booked");
        newAppointment.setPatient(patient);
        patient.bookAppointment(newAppointment);
        newPhysio.removeAvailableAppointment(newAppointment);

        // Step 5: Confirm the change.
        //System.out.println("Booking changed successfully!");
        return true;
    }


    // Helper method to find a physiotherapist by ID - add this in the same BookingSystem class.
    physioclinics.models.Physiotherapist findPhysiotherapistById(String physioId) {
        // Assuming you have a list of physiotherapists in your BookingSystem, e.g., physiotherapistList.
        for (physioclinics.models.Physiotherapist physio : physiotherapists) {
            if (physio.getId().equalsIgnoreCase(physioId)) {
                return physio;
            }
        }
        return null;
    }

    public void generateReport() {
        System.out.println("==== Treatment Appointments Report ====\n");

        // Detailed listing: For each physiotherapist, show all related appointments.
        for (physioclinics.models.Physiotherapist physio : physiotherapists) {
            System.out.println("Physiotherapist: " + physio.getName() +
                    " (Expertise: " + physio.getExpertise() + ")");
            boolean hasAppointment = false;

            // Iterate over the global appointments list.
            for (physioclinics.models.TreatmentAppointment appointment : appointments) {
                // Match the appointment with the current physiotherapist.
                if (appointment.getPhysiotherapist().getId().equalsIgnoreCase(physio.getId())) {
                    hasAppointment = true;
                    // Use physiotherapist's expertise as a placeholder for treatment name.
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
            System.out.println(); // Blank line for better separation
        }

        // Ranking section: sort physiotherapists by the count of attended appointments.
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