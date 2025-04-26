package physioclinics.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

public class BookingSystemTest {

    private int getPatientAppointmentsSize(Patient patient) throws Exception {
        Field field = Patient.class.getDeclaredField("appointments");
        field.setAccessible(true);
        List<?> apps = (List<?>) field.get(patient);
        return apps.size();
    }

    @Test
    void testBookExistingAppointment() throws Exception {
        BookingSystem system = new BookingSystem();
        Patient patient = new Patient("P001", "Test Patient", "123 Some St", "555-0101");
        Physiotherapist physio = new Physiotherapist("PH001", "Test Physio", "456 Other Rd", "555-0202", "Physio");

        // Create an appointment with status "available"
        TreatmentAppointment appointment = new TreatmentAppointment(
                "A001",
                LocalDateTime.now().plusDays(1),
                "available",
                null,
                physio,
                "Therapy Session"
        );

        // Add the appointment to the system and the physiotherapist's available appointments.
        system.getAppointments().add(appointment);
        physio.addAvailableAppointment(appointment);

        // Attempt to book the appointment.
        boolean result = system.bookExistingAppointment("A001", patient);
        assertTrue(result, "Booking should be successful.");
        assertEquals("booked", appointment.getStatus(), "Appointment status should be updated to 'booked'.");
        assertEquals(patient, appointment.getPatient(), "The appointment's patient should be set to the given patient.");
        assertFalse(physio.getAvailableAppointments().contains(appointment),
                "Booked appointment should be removed from the physiotherapist's available appointments.");
    }

    @Test
    void testCancelAppointment() throws Exception {
        BookingSystem system = new BookingSystem();
        Patient patient = new Patient("P002", "Alice", "789 Street", "555-0303");
        Physiotherapist physio = new Physiotherapist("PH002", "Dr. Bob", "101 Road", "555-0404", "Orthopedics");

        // Create an appointment that is already booked.
        TreatmentAppointment appointment = new TreatmentAppointment(
                "A002",
                LocalDateTime.now().plusDays(1),
                "booked",
                patient,
                physio,
                "Rehab Session"
        );
        system.getAppointments().add(appointment);

        // Simulate that the physiotherapist still has this appointment in their available list.
        physio.addAvailableAppointment(appointment);

        // Cancel the appointment.
        boolean cancelled = system.cancelAppointment("A002", patient);
        assertTrue(cancelled, "Cancellation should be successful.");
        assertEquals("cancelled", appointment.getStatus(), "Appointment status should be updated to 'cancelled'.");
        assertFalse(physio.getAvailableAppointments().contains(appointment),
                "Cancelled appointment should be removed from the physiotherapist's available appointments.");
        assertEquals(0, getPatientAppointmentsSize(patient),
                "Patient's appointments list should be empty after cancellation.");
    }
}