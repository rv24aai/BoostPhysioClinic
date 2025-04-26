package physioclinics.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

class TreatmentAppointmentTest {

    private TreatmentAppointment appointment;
    private Physiotherapist physiotherapist;

    @BeforeEach
    void setUp() {
        // Create a dummy physiotherapist to associate with the appointment.
        physiotherapist = new Physiotherapist("PH100", "Dr. Jane", "456 Some Rd", "555-5678", "Physical Therapy");

        // Instantiate a TreatmentAppointment with initial status "available".
        appointment = new TreatmentAppointment(
                "A100",
                LocalDateTime.now().plusDays(1),
                "available",    // initial status set to "available"
                null,           // no patient assigned yet
                physiotherapist,
                "Therapy Session"
        );
    }

    @Test
    void testSetStatus() throws Exception {
        // Assert that the initial status is "available".
        assertEquals("available", getStatus(appointment), "Initial status should be 'available'.");

        // Change the status to "booked" using the setStatus method.
        appointment.setStatus("booked");

        // Verify that the status has updated to "booked".
        assertEquals("booked", getStatus(appointment), "Status should be updated to 'booked'.");
    }

    private String getStatus(TreatmentAppointment appointment) throws Exception {
        Field field = TreatmentAppointment.class.getDeclaredField("status");
        field.setAccessible(true);
        return (String) field.get(appointment);
    }
}