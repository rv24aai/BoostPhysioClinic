package physioclinics.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import physioclinics.models.Patient;
import physioclinics.models.Physiotherapist;
import physioclinics.models.TreatmentAppointment;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

class PatientTest {

    private Patient patient;
    private TreatmentAppointment appointment;

    @BeforeEach
    void setUp() {
        // Create a new Patient. Adjust the constructor parameters if needed.
        patient = new Patient("P100", "John Doe", "123 Main St", "555-1234");

        // Create a dummy Physiotherapist required by TreatmentAppointment.
        Physiotherapist physio = new Physiotherapist("PH100", "Dr. Jane", "456 Some Rd", "555-5678", "Physical Therapy");

        // Create a TreatmentAppointment with a future date and "available" status.
        appointment = new TreatmentAppointment(
                "A100",
                LocalDateTime.now().plusDays(1),
                "available",  // assume appointment is "available" before booking
                null,         // no patient assigned prior to booking
                physio,
                "Therapy Session"
        );
    }

    @Test
    void testBookAppointment() throws Exception {
        // Initially, the patient's appointments list should be empty.
        assertEquals(0, getAppointmentsSize(patient), "Appointments list should be empty initially.");

        // Book the appointment.
        patient.bookAppointment(appointment);
        // Verify that the appointment has been added.
        assertEquals(1, getAppointmentsSize(patient), "Appointments list should contain one entry after booking.");

        // Attempt to book the same appointment again.
        patient.bookAppointment(appointment);
        // Verify that booking the same appointment twice does not add a duplicate.
        assertEquals(1, getAppointmentsSize(patient), "Booking the same appointment twice should not duplicate entries.");
    }

    private int getAppointmentsSize(Patient patient) throws Exception {
        Field field = Patient.class.getDeclaredField("appointments");
        field.setAccessible(true);
        List<?> appointments = (List<?>) field.get(patient);
        return appointments.size();
    }
}