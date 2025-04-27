package physioclinics.models;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Scanner;
import java.util.List;

public class Main {

    static Scanner input = new Scanner(System.in);
    static physioclinics.models.BookingSystem bookingSystem = new physioclinics.models.BookingSystem();

    public static void main(String[] args) {
        // Initialize hardcoded data
        preRegisterData();

        boolean programRunning = true;
        while (programRunning) {
            System.out.println("\n--- Boost Physio Clinic System ---");
            System.out.println("Select an option:");
            System.out.println("1. Add a new patient");
            System.out.println("2. Remove a patient");
            System.out.println("3. Search physiotherapist by name");
            System.out.println("4. Cancel an appointment");
            System.out.println("5. Change a booking");
            System.out.println("6. Mark appointment as attended");
            System.out.println("7. Generate clinic report");
            System.out.println("8. Exit");

            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1:
                    addNewPatient();
                    break;
                case 2:
                    removePatient();
                    break;
                case 3:
                    searchPhysiotherapistByName();
                    break;
                case 4:
                    cancelAppointment();
                    break;
                case 5:
                    // Display all booked appointments along with their associated Patient IDs.
                    List<physioclinics.models.TreatmentAppointment> bookedAppointments = bookingSystem.getAppointments();
                    boolean foundBooked = false;
                    System.out.println("Booked Appointments:");
                    for (physioclinics.models.TreatmentAppointment appointment : bookedAppointments) {
                        if ("booked".equalsIgnoreCase(appointment.getStatus())) {
                            String bookPatientId = (appointment.getPatient() != null) ? appointment.getPatient().getId() : "N/A";
                            String physioId = (appointment.getPhysiotherapist() != null) ? appointment.getPhysiotherapist().getId() : "N/A";
                            System.out.println("-Appointment ID: " + appointment.getAppointmentId() + ", Patient ID: " + bookPatientId + ", Physiotherapist ID: " + physioId);
                            foundBooked = true;
                        }
                    }
                    if (!foundBooked) {
                        System.out.println("No booked appointments available for change.");
                        break;
                    }

                    // Prompt for the appointment ID to change.
                    System.out.println("Enter Appointment ID to change (or 0 to cancel):");
                    String oldBookingId = input.nextLine().trim();
                    if ("0".equals(oldBookingId)) {
                        System.out.println("Returning to the main menu...");
                        break;
                    }

                    // Prompt for the new physiotherapist's ID.
                    System.out.println("Enter new Physiotherapist ID (or 0 to cancel):");
                    String newPhysioId = input.nextLine().trim();
                    if ("0".equals(newPhysioId)) {
                        System.out.println("Returning to the main menu...");
                        break;
                    }

                    // Retrieve the new physiotherapist using the helper method.
                    physioclinics.models.Physiotherapist newPhysio = bookingSystem.findPhysiotherapistById(newPhysioId);
                    if (newPhysio == null) {
                        System.out.println("No physiotherapist found with ID " + newPhysioId);
                        break;
                    }

                    // Display available appointments for the new physiotherapist.
                    System.out.println("Available appointments for " + newPhysio.getName() + ":");
                    for (physioclinics.models.TreatmentAppointment app : newPhysio.getAvailableAppointments()) {
                        System.out.println(app.getAppointmentId() + " at " + app.getDateTime());
                    }

                    // Ask for the new booking ID from the displayed list.
                    System.out.println("Enter new Booking ID (choose from the above list) (or 0 to cancel):");
                    String newBookingId = input.nextLine().trim();
                    if ("0".equals(newBookingId)) {
                        System.out.println("Returning to the main menu...");
                        break;
                    }

                    // Prompt for Patient ID and lookup the corresponding Patient.
                    System.out.println("Enter your Patient ID (or 0 to cancel):");
                    String patientId = input.nextLine().trim();
                    if ("0".equals(patientId)) {
                        System.out.println("Returning to the main menu...");
                        break;
                    }

                    physioclinics.models.Patient currentPatient = null;
                    for (physioclinics.models.Patient p : bookingSystem.getPatients()) {
                        if (p.getId().equalsIgnoreCase(patientId)) {
                            currentPatient = p;
                            break;
                        }
                    }
                    if (currentPatient == null) {
                        System.out.println("Patient not found!");
                        break;
                    }

                    // Call changeBooking to update the booking details.
                    boolean changed = bookingSystem.changeBooking(oldBookingId, newPhysioId, newBookingId, currentPatient);
                    if (changed) {
                        System.out.println("Booking changed successfully!");
                    } else {
                        System.out.println("Booking change failed. Please check your inputs.");
                    }
                    break;
                case 6:
                    markAppointmentAsAttended();
                    break;
                case 7:
                    bookingSystem.generateReport();
                    break;
                case 8:
                    System.out.println("Goodbye! Exiting the system...");
                    programRunning = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        System.exit(0);
    }

    private static void preRegisterData() {
        // Pre-registered physiotherapists
        physioclinics.models.Physiotherapist physio1 = new physioclinics.models.Physiotherapist("PH001", "Dr. Stephen", "34 Harley Street, London", "+44 7074861111", "Physiotherapy");
        physioclinics.models.Physiotherapist physio2 = new physioclinics.models.Physiotherapist("PH002", "Dr. Victor", "56 Kings Road, London", "+44 7076292222", "Rehabilitation");
        physioclinics.models.Physiotherapist physio3 = new physioclinics.models.Physiotherapist("PH003", "Dr. Henry", "89 Queen Victoria Street, London", "+44 7076493333", "Osteopathy");
        // Adding physiotherapists to the system
        bookingSystem.addPhysiotherapist(physio1);
        bookingSystem.addPhysiotherapist(physio2);
        bookingSystem.addPhysiotherapist(physio3);

        // Pre-registered patients
        physioclinics.models.Patient patient1 = new physioclinics.models.Patient("P001", "Matt Murdock", "123 Oxford Street, London", "+44 7079460123");
        physioclinics.models.Patient patient2 = new physioclinics.models.Patient("P002", "Thomas Shelby", "456 Regent Street, London", "+44 7081234567");
        physioclinics.models.Patient patient3 = new physioclinics.models.Patient("P003", "Daryl Dixon", "789 Piccadilly Circus, London", "+44 7071239876");
        physioclinics.models.Patient patient4 = new physioclinics.models.Patient("P004", "Carol Peletier", "321 Baker Street, London", "+44 7072241234");
        physioclinics.models.Patient patient5 = new physioclinics.models.Patient("P005", "Rick Grimes", "12 Downing Street, London", "+44 7072807654");
        physioclinics.models.Patient patient6 = new physioclinics.models.Patient("P006", "Maggie Greene", "85 Fleet Street, London", "+44 7073244563");
        physioclinics.models.Patient patient7 = new physioclinics.models.Patient("P007", "Gandhi Mahaan", "22 Leicester Square, London", "+44 7074407890");
        physioclinics.models.Patient patient8 = new physioclinics.models.Patient("P008", "Walter White", "99 Strand, London", "+44 7074845647");
        physioclinics.models.Patient patient9 = new physioclinics.models.Patient("P009", "Billy Butcher", "50 Covent Garden, London", "+44 7076128754");
        physioclinics.models.Patient patient10 = new physioclinics.models.Patient("P010", "Sarah Connor", "15 Kensington High Street, London", "+44 7078513214");
        physioclinics.models.Patient patient11 = new physioclinics.models.Patient("P011", "John Durairaj", "18 Borough Market, London", "+44 7079736542");
        physioclinics.models.Patient patient12 = new physioclinics.models.Patient("P012", "Leo Das", "72 Camden Town, London", "+44 7075942345");
        // Adding patients to the system
        bookingSystem.addPatient(patient1);
        bookingSystem.addPatient(patient2);
        bookingSystem.addPatient(patient3);
        bookingSystem.addPatient(patient4);
        bookingSystem.addPatient(patient5);
        bookingSystem.addPatient(patient6);
        bookingSystem.addPatient(patient7);
        bookingSystem.addPatient(patient8);
        bookingSystem.addPatient(patient9);
        bookingSystem.addPatient(patient10);
        bookingSystem.addPatient(patient11);
        bookingSystem.addPatient(patient12);

        // Appointments for Physiotherapist 1 (physio1)
        physioclinics.models.TreatmentAppointment appointmentA001 = new physioclinics.models.TreatmentAppointment("A001", LocalDateTime.of(2025, 5, 1, 10, 0), "available", null, physio1, "Neural mobilisation");
        physio1.addAvailableAppointment(appointmentA001);
        bookingSystem.getAppointments().add(appointmentA001);
        physioclinics.models.TreatmentAppointment appointmentA002 = new physioclinics.models.TreatmentAppointment("A002", LocalDateTime.of(2025, 5, 2, 11, 0), "available", null, physio1, "Acupuncture");
        physio1.addAvailableAppointment(appointmentA002);
        bookingSystem.getAppointments().add(appointmentA002);
        physioclinics.models.TreatmentAppointment appointmentA003 = new physioclinics.models.TreatmentAppointment("A003", LocalDateTime.of(2025, 5, 8, 10, 0), "available", null, physio1, "Massage");
        physio1.addAvailableAppointment(appointmentA003);
        bookingSystem.getAppointments().add(appointmentA003);
        physioclinics.models.TreatmentAppointment appointmentA004 = new physioclinics.models.TreatmentAppointment("A004", LocalDateTime.of(2025, 5, 9, 14, 0), "available", null, physio1, "Mobilisation of the spine and joints");
        physio1.addAvailableAppointment(appointmentA004);
        bookingSystem.getAppointments().add(appointmentA004);
        physioclinics.models.TreatmentAppointment appointmentA005 = new physioclinics.models.TreatmentAppointment("A005", LocalDateTime.of(2025, 5, 15, 10, 0), "available", null, physio1, "Pool rehabilitation");
        physio1.addAvailableAppointment(appointmentA005);
        bookingSystem.getAppointments().add(appointmentA005);
        physioclinics.models.TreatmentAppointment appointmentA006 = new physioclinics.models.TreatmentAppointment("A006", LocalDateTime.of(2025, 5, 16, 11, 0), "available", null, physio1,"Acupuncture");
        physio1.addAvailableAppointment(appointmentA006);
        bookingSystem.getAppointments().add(appointmentA006);
        physioclinics.models.TreatmentAppointment appointmentA007 = new physioclinics.models.TreatmentAppointment("A007", LocalDateTime.of(2025, 5, 22, 10, 0), "available", null, physio1,"Mobilisation of the spine and joints");
        physio1.addAvailableAppointment(appointmentA007);
        bookingSystem.getAppointments().add(appointmentA007);
        physioclinics.models.TreatmentAppointment appointmentA008 = new physioclinics.models.TreatmentAppointment("A008", LocalDateTime.of(2025, 5, 23, 14, 0), "available", null, physio1,"Massage");
        physio1.addAvailableAppointment(appointmentA008);
        bookingSystem.getAppointments().add(appointmentA008);
        // Appointments for Physiotherapist 2 (physio2)
        physioclinics.models.TreatmentAppointment appointmentB001 = new physioclinics.models.TreatmentAppointment("B001", LocalDateTime.of(2025, 5, 1, 12, 0), "available", null, physio2, "Massage");
        physio2.addAvailableAppointment(appointmentB001);
        bookingSystem.getAppointments().add(appointmentB001);
        physioclinics.models.TreatmentAppointment appointmentB002 = new physioclinics.models.TreatmentAppointment("B002", LocalDateTime.of(2025, 5, 2, 13, 0), "available", null, physio2, "Neural mobilisation");
        physio2.addAvailableAppointment(appointmentB002);
        bookingSystem.getAppointments().add(appointmentB002);
        physioclinics.models.TreatmentAppointment appointmentB003 = new physioclinics.models.TreatmentAppointment("B003", LocalDateTime.of(2025, 5, 5, 9, 0), "available", null, physio2, "Pool Rehabilitation");
        physio2.addAvailableAppointment(appointmentB003);
        bookingSystem.getAppointments().add(appointmentB003);
        physioclinics.models.TreatmentAppointment appointmentB004 = new physioclinics.models.TreatmentAppointment("B004", LocalDateTime.of(2025, 5, 7, 15, 0), "available", null, physio2, "Acupuncture");
        physio2.addAvailableAppointment(appointmentB004);
        bookingSystem.getAppointments().add(appointmentB004);
        physioclinics.models.TreatmentAppointment appointmentB005 = new physioclinics.models.TreatmentAppointment("B005", LocalDateTime.of(2025, 5, 10, 11, 0), "available", null, physio2, "Mobilisation of the spine and joints");
        physio2.addAvailableAppointment(appointmentB005);
        bookingSystem.getAppointments().add(appointmentB005);
        physioclinics.models.TreatmentAppointment appointmentB006 = new physioclinics.models.TreatmentAppointment("B006", LocalDateTime.of(2025, 5, 12, 16, 0), "available", null, physio2, "Neural mobilisation");
        physio2.addAvailableAppointment(appointmentB006);
        bookingSystem.getAppointments().add(appointmentB006);
        physioclinics.models.TreatmentAppointment appointmentB007 = new physioclinics.models.TreatmentAppointment("B007", LocalDateTime.of(2025, 5, 18, 14, 0), "available", null, physio2, "Acupuncture");
        physio2.addAvailableAppointment(appointmentB007);
        bookingSystem.getAppointments().add(appointmentB007);
        physioclinics.models.TreatmentAppointment appointmentB008 = new physioclinics.models.TreatmentAppointment("B008", LocalDateTime.of(2025, 5, 20, 10, 0), "available", null, physio2, "Mobilisation of the spine and joints");
        physio2.addAvailableAppointment(appointmentB008);
        bookingSystem.getAppointments().add(appointmentB008);
        // Appointments for Physiotherapist 3 (physio3)
        physioclinics.models.TreatmentAppointment appointmentC001 = new physioclinics.models.TreatmentAppointment("C001", LocalDateTime.of(2025, 5, 3, 13, 0), "available", null, physio3, "Mobilisation of the spine and joints");
        physio3.addAvailableAppointment(appointmentC001);
        bookingSystem.getAppointments().add(appointmentC001);
        physioclinics.models.TreatmentAppointment appointmentC002 = new physioclinics.models.TreatmentAppointment("C002", LocalDateTime.of(2025, 5, 4, 14, 0), "available", null, physio3, "Pool Rehabilitation");
        physio3.addAvailableAppointment(appointmentC002);
        bookingSystem.getAppointments().add(appointmentC002);
        physioclinics.models.TreatmentAppointment appointmentC003 = new physioclinics.models.TreatmentAppointment("C003", LocalDateTime.of(2025, 5, 6, 8, 0), "available", null, physio3, "Neural mobilisation");
        physio3.addAvailableAppointment(appointmentC003);
        bookingSystem.getAppointments().add(appointmentC003);
        physioclinics.models.TreatmentAppointment appointmentC004 = new physioclinics.models.TreatmentAppointment("C004", LocalDateTime.of(2025, 5, 10, 17, 0), "available", null, physio3, "Massage");
        physio3.addAvailableAppointment(appointmentC004);
        bookingSystem.getAppointments().add(appointmentC004);
        physioclinics.models.TreatmentAppointment appointmentC005 = new physioclinics.models.TreatmentAppointment("C005", LocalDateTime.of(2025, 5, 12, 9, 0), "available", null, physio3, "Acupuncture");
        physio3.addAvailableAppointment(appointmentC005);
        bookingSystem.getAppointments().add(appointmentC005);
        physioclinics.models.TreatmentAppointment appointmentC006 = new physioclinics.models.TreatmentAppointment("C006", LocalDateTime.of(2025, 5, 14, 16, 0), "available", null, physio3, "Massage");
        physio3.addAvailableAppointment(appointmentC006);
        bookingSystem.getAppointments().add(appointmentC006);
        physioclinics.models.TreatmentAppointment appointmentC007 = new physioclinics.models.TreatmentAppointment("C007", LocalDateTime.of(2025, 5, 19, 11, 0), "available", null, physio3, "Pool Rehabilitation");
        physio3.addAvailableAppointment(appointmentC007);
        bookingSystem.getAppointments().add(appointmentC007);
        physioclinics.models.TreatmentAppointment appointmentC008 = new physioclinics.models.TreatmentAppointment("C008", LocalDateTime.of(2025, 5, 21, 12, 0), "available", null, physio3, "Neural mobilisation");
        physio3.addAvailableAppointment(appointmentC008);
        bookingSystem.getAppointments().add(appointmentC008);
    }

    private static void addNewPatient() {
        System.out.println("Enter Patient ID (or 0 to cancel):");
        String id = input.nextLine();
        if (checkForExitOption(id)) {
            return;
        }

        // Check if the patient ID already exists
        for (physioclinics.models.Patient patient : bookingSystem.getPatients()) {
            if (patient.getId().equalsIgnoreCase(id)) {
                System.out.println("Error: Patient ID " + id + " already exists!");
                return;
            }
        }

        System.out.println("Enter Patient Name (or 0 to cancel):");
        String name = input.nextLine();
        if (checkForExitOption(name)) {
            return;
        }

        System.out.println("Enter Patient Address (or 0 to cancel):");
        String address = input.nextLine();
        if (checkForExitOption(address)) {
            return;
        }

        System.out.println("Enter Patient Telephone Number (or 0 to cancel):");
        String phone = input.nextLine();
        if (checkForExitOption(phone)) {
            return;
        }

        physioclinics.models.Patient newPatient = new physioclinics.models.Patient(id, name, address, phone);
        bookingSystem.addPatient(newPatient);
        System.out.println("Patient added successfully!");
    }

    private static void removePatient() {
        // List all available patients with details.
        System.out.println("Available Patients (ID | Name | Address | Telephone):");
        for (physioclinics.models.Patient patient : bookingSystem.getPatients()) {
            System.out.println("- " + patient.getId() + " | "
                    + patient.getName() + " | "
                    + patient.getAddress() + " | "
                    + patient.getTelephoneNumber());
        }
        System.out.println();

        // Prompt the user to enter the Patient ID to remove.
        System.out.println("Enter Patient ID to remove (or 0 to cancel):");
        String patientId = input.nextLine();
        if (checkForExitOption(patientId)) {
            return;
        }

        // Safely remove the patient using an iterator.
        boolean removed = false;
        Iterator<physioclinics.models.Patient> iterator = bookingSystem.getPatients().iterator();
        while (iterator.hasNext()) {
            physioclinics.models.Patient patient = iterator.next();
            if (patient.getId().equalsIgnoreCase(patientId)) {
                iterator.remove();
                removed = true;
                break;
            }
        }

        if (removed) {
            System.out.println("Patient removed successfully!");
        } else {
            System.out.println("Patient not found!");
        }
    }

    // Check if the input is "0". If so, print a message and return to main menu.
    private static boolean checkForExitOption(String input) {
        if ("0".equals(input)) {
            System.out.println("Returning to the main menu...");
            return true;
        }
        return false;
    }

    private static void searchPhysiotherapistByName() {
        // List all available physiotherapists with full details.
        System.out.println("Available Physiotherapists (ID | Name | Expertise | Address | Telephone):");
        for (physioclinics.models.Physiotherapist physio : bookingSystem.getPhysiotherapists()) {
            System.out.println("- "
                    + physio.getId() + " | "
                    + physio.getName() + " | "
                    + physio.getExpertise() + " | "
                    + physio.getAddress() + " | "
                    + physio.getTelephoneNumber());
        }
        System.out.println();

        // Prompt the user with an extra note about the exit option.
        System.out.println("Enter physiotherapist's name to search (or 0 to return to main menu):");
        String name = input.nextLine();
        if (checkForExitOption(name)) {
            return;
        }

        // Find the physiotherapist by name.
        physioclinics.models.Physiotherapist selectedPhysiotherapist = null;
        for (physioclinics.models.Physiotherapist physiotherapist : bookingSystem.getPhysiotherapists()) {
            if (physiotherapist.getName().equalsIgnoreCase(name)) {
                selectedPhysiotherapist = physiotherapist;
                break;
            }
        }

        if (selectedPhysiotherapist == null) {
            System.out.println("Physiotherapist not found!");
            return;
        }

        System.out.println("\nAvailable Treatment Appointments for "
                + selectedPhysiotherapist.getName() + " ("
                + selectedPhysiotherapist.getExpertise() + "):");

        // Retrieve the list of available appointments.
        List<physioclinics.models.TreatmentAppointment> appointments = selectedPhysiotherapist.getAvailableAppointments();

        if (appointments.isEmpty()) {
            System.out.println("No available appointments.");
            return;
        }

        // Display appointments (grouped by weeks, etc.).
        int weekNumber = 1;
        int appointmentCount = 1;
        LocalDateTime startDate = appointments.get(0).getDateTime().minusDays(
                appointments.get(0).getDateTime().getDayOfWeek().getValue());
        System.out.println("\nWeek " + weekNumber + ":");

        for (physioclinics.models.TreatmentAppointment appointment : appointments) {
            LocalDateTime appointmentDate = appointment.getDateTime();
            long weeksBetween = java.time.temporal.ChronoUnit.WEEKS.between(startDate, appointmentDate);

            if (weeksBetween > weekNumber - 1) {
                weekNumber++;
                System.out.println("\nWeek " + weekNumber + ":");
            }

            System.out.println(appointmentCount + ". Booking ID: " + appointment.getAppointmentId());
            System.out.println("   Treatment: " + appointment.getTreatmentName());
            System.out.println("   Date: " + appointmentDate.toLocalDate());
            System.out.println("   Time: " + appointmentDate.toLocalTime() + " - " +
                    appointmentDate.plusHours(1).toLocalTime());
            System.out.println();
            appointmentCount++;
        }

        // Get the user's selection for the appointment.
        System.out.println("Please enter the number to select an appointment (or 0 to cancel):");
        String selectedAppointmentInput = input.nextLine();
        if (checkForExitOption(selectedAppointmentInput)) {
            return;
        }
        int selectedIndex;
        try {
            selectedIndex = Integer.parseInt(selectedAppointmentInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid selection. Returning to menu.");
            return;
        }

        if (selectedIndex < 1 || selectedIndex > appointments.size()) {
            System.out.println("Invalid selection. Returning to menu.");
            return;
        }

        // Retrieve the selected appointment.
        physioclinics.models.TreatmentAppointment selectedAppointment = appointments.get(selectedIndex - 1);
        String appointmentId = selectedAppointment.getAppointmentId();

        // List patients before prompting.
        System.out.println("Available Patients (ID | Name | Address | Telephone):");
        for (physioclinics.models.Patient patient : bookingSystem.getPatients()) {
            System.out.println("- "
                    + patient.getId() + " | "
                    + patient.getName() + " | "
                    + patient.getAddress() + " | "
                    + patient.getTelephoneNumber());
        }
        System.out.println();

        // Ask for the Patient ID for booking.
        System.out.println("Enter Patient ID for booking (or 0 to cancel):");
        String patientId = input.nextLine();
        if (checkForExitOption(patientId)) {
            return;
        }

        // Lookup the patient by ID.
        physioclinics.models.Patient selectedPatient = null;
        for (physioclinics.models.Patient patient : bookingSystem.getPatients()) {
            if (patient.getId().equalsIgnoreCase(patientId)) {
                selectedPatient = patient;
                break;
            }
        }

        if (selectedPatient == null) {
            System.out.println("Patient not found! Returning to menu.");
            return;
        }

        // Book the appointment.
        boolean bookingSuccess = bookingSystem.bookExistingAppointment(appointmentId, selectedPatient);
        if (bookingSuccess) {
            System.out.println("Appointment " + appointmentId + " booked successfully!");
        } else {
            System.out.println("Appointment not found or already booked.");
        }
    }

    private static void cancelAppointment() {
        // Display all booked appointments with their Patient IDs.
        List<physioclinics.models.TreatmentAppointment> appointments = bookingSystem.getAppointments();
        boolean foundBooked = false;

        System.out.println("Booked Appointments:");
        for (physioclinics.models.TreatmentAppointment appt : appointments) {
            // Check if the appointment status is "booked"
            if ("booked".equalsIgnoreCase(appt.getStatus())) {
                String patientId = (appt.getPatient() != null) ? appt.getPatient().getId() : "N/A";
                System.out.println("-Appointment ID: " + appt.getAppointmentId() + ", Patient ID: " + patientId);
                foundBooked = true;
            }
        }

        if (!foundBooked) {
            System.out.println("No booked appointments available.");
            return;
        }

        // Continue with the existing cancellation process.
        System.out.println("Enter Appointment ID to cancel (or 0 to cancel):");
        String appointmentId = input.nextLine();
        if (checkForExitOption(appointmentId)) {
            return;
        }

        // Find the appointment in the booking system
        physioclinics.models.TreatmentAppointment appointmentToCancel = null;
        for (physioclinics.models.TreatmentAppointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                appointmentToCancel = appointment;
                break;
            }
        }

        // Validate if appointment was found
        if (appointmentToCancel == null) {
            System.out.println("Appointment not found!");
            return;
        }

        // Prompt for Patient ID with cancel option
        System.out.println("Enter Patient ID for cancellation (or 0 to cancel):");
        String patientId = input.nextLine();
        if (checkForExitOption(patientId)) {
            return;
        }

        physioclinics.models.Patient selectedPatient = null;
        for (physioclinics.models.Patient patient : bookingSystem.getPatients()) {
            if (patient.getId().equalsIgnoreCase(patientId)) {
                selectedPatient = patient;
                break;
            }
        }

        if (selectedPatient == null) {
            System.out.println("Patient not found!");
            return;
        }

        // Cancel the appointment using the obtained patient.
        boolean result = bookingSystem.cancelAppointment(appointmentToCancel.getAppointmentId(), selectedPatient);
        if (result) {
            System.out.println("Appointment " + appointmentToCancel.getAppointmentId() + " cancelled successfully!");
        } else {
            System.out.println("Appointment " + appointmentToCancel.getAppointmentId() + " not found or cannot be canceled.");
        }
    }

    private static void markAppointmentAsAttended() {
        // Display all booked appointment IDs
        List<physioclinics.models.TreatmentAppointment> appointments = bookingSystem.getAppointments();
        boolean foundBooked = false;
        System.out.println("Booked Appointments:");
        for (physioclinics.models.TreatmentAppointment appointment : appointments) {
            if ("booked".equalsIgnoreCase(appointment.getStatus())) {
                System.out.println("-Appointment ID: " + appointment.getAppointmentId());
                foundBooked = true;
            }
        }
        if (!foundBooked) {
            System.out.println("No booked appointments available.");
            return;
        }

        // Prompt for the Appointment ID and provide a cancel option.
        System.out.println("Enter Appointment ID to mark as attended (or 0 to cancel):");
        String appointmentId = input.nextLine().trim();
        if (checkForExitOption(appointmentId)) {
            return;
        }

        // Attempt to find the appointment from the booking system.
        physioclinics.models.TreatmentAppointment appointmentToAttend = null;
        for (physioclinics.models.TreatmentAppointment appointment : bookingSystem.getAppointments()) {
            if (appointment.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                appointmentToAttend = appointment;
                break;
            }
        }

        // If the appointment is not found, inform the user and exit.
        if (appointmentToAttend == null) {
            System.out.println("Appointment not found!");
            return;
        }

        // Ensure the appointment is booked before marking it as attended.
        if (!"booked".equalsIgnoreCase(appointmentToAttend.getStatus())) {
            System.out.println("Only booked appointments can be marked as attended.");
            return;
        }

        // Mark the appointment as attended.
        appointmentToAttend.setStatus("attended");
        System.out.println("Appointment " + appointmentToAttend.getAppointmentId() + " has been marked as attended.");
    }
}