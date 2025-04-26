package physioclinics.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

class PhysiotherapistTest {

    private Physiotherapist physiotherapist;
    private TreatmentAppointment appointment;

    @BeforeEach
    void setUp() {
        // Initialize a Physiotherapist. Adjust the constructor parameters if needed.
        physiotherapist = new Physiotherapist("PH100", "Dr. Jane", "456 Some Rd", "555-5678", "Physical Therapy");

        // Prepare a dummy TreatmentAppointment associated with this physiotherapist.
        appointment = new TreatmentAppointment(
                "A200",
                LocalDateTime.now().plusDays(2),
                "available",  // initial status assumed to be "available"
                null,         // no patient assigned yet
                physiotherapist,  // set this physiotherapist as the one for the appointment
                "Massage Therapy"
        );
    }

    @Test
    void testAddAvailableAppointment() throws Exception {
        // Initially, the available appointments list should be empty.
        assertEquals(0, getAvailableAppointmentsSize(physiotherapist),
                "Available appointments list should be empty initially.");

        // Add the first appointment.
        physiotherapist.addAvailableAppointment(appointment);
        assertEquals(1, getAvailableAppointmentsSize(physiotherapist),
                "After adding one appointment, available appointments list should contain one entry.");

        // Add a second appointment to check further.
        TreatmentAppointment appointment2 = new TreatmentAppointment(
                "A201",
                LocalDateTime.now().plusDays(3),
                "available",
                null,
                physiotherapist,
                "Exercise Therapy"
        );
        physiotherapist.addAvailableAppointment(appointment2);
        assertEquals(2, getAvailableAppointmentsSize(physiotherapist),
                "After adding a second appointment, available appointments list should contain two entries.");
    }

    private int getAvailableAppointmentsSize(Physiotherapist physiotherapist) throws Exception {
        Field field = Physiotherapist.class.getDeclaredField("availableAppointments");
        field.setAccessible(true);
        List<?> appointments = (List<?>) field.get(physiotherapist);
        return appointments.size();
    }
}