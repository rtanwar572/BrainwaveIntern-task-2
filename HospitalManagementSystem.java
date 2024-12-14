import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class HospitalManagementSystem {

    static Scanner scanner = new Scanner(System.in);
    static int invoiceCounter=0;

    // Models
    static class User {
        String username, password, role;

        public User(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }
    }

    static class Patient {
        String id;
        String name;
        int age;
        String contact;
        String diagnosis;
        List<String> medicalHistory = new ArrayList<>();
        List<String> testReports = new ArrayList<>();

        Patient(String id, String name, int age, String contact, String diagnosis) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.contact = contact;
            this.diagnosis = diagnosis;
        }

        @Override
        public String toString() {
            return "Patient ID: " + id + ", Name: " + name + ", Age: " + age + ", Contact: " + contact + ", Diagnosis: " + diagnosis;
        }
    }

    static class Appointment {
        String appointmentId;
        String patientId;
        String doctorName;
        String date;

        Appointment(String appointmentId, String patientId, String doctorName, String date) {
            this.appointmentId = appointmentId;
            this.patientId = patientId;
            this.doctorName = doctorName;
            this.date = date;
        }

        @Override
        public String toString() {
            return "Appointment ID: " + appointmentId + ", Patient ID: " + patientId + ", Doctor: " + doctorName + ", Date: " + date;
        }
    }

    static class Staff {
        String staffId;
        String name;
        String role;
        String specialization;

        Staff(String staffId, String name, String role, String specialization) {
            this.staffId = staffId;
            this.name = name;
            this.role = role;
            this.specialization = specialization;
        }

        @Override
        public String toString() {
            return "Staff ID: " + staffId + ", Name: " + name + ", Role: " + role + ", Specialization: " + specialization;
        }
    }
    static class Billing {
        Patient patient;
        double consultationFee;
        double testFee;
        double medicineFe;
        Billing(Patient patient, double consultationFee, double testFee, double medicineFee){
            this.patient=patient;
            this.consultationFee=consultationFee;
            this.testFee=testFee;
            this.medicineFe=medicineFee;
        }

    }

    static class Invoice {
        String invoiceId;
        String patientId;
        double Amount;

        Invoice(String invoiceCounter, String patientId,double bill) {
            this.invoiceId = invoiceCounter;
            this.patientId = patientId;
            Amount=bill;
        }

        @Override
        public String toString() {
            return "Invoice ID: " + invoiceId + ", Patient ID: " + patientId + ", Amount: " + Amount;
        }
    }

    static class MedicalSupply {
        String itemId;
        String name;
        int quantity;

        MedicalSupply(String itemId, String name, int quantity) {
            this.itemId = itemId;
            this.name = name;
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return "Item ID: " + itemId + ", Name: " + name + ", Quantity: " + quantity;
        }
    }

    static class EHR {
        String recordId;
        String patientId;
        String diagnosis;
        String treatment;

        EHR(String recordId, String patientId, String diagnosis, String treatment) {
            this.recordId = recordId;
            this.patientId = patientId;
            this.diagnosis = diagnosis;
            this.treatment = treatment;
        }

        @Override
        public String toString() {
            return "Record ID: " + recordId + ", Patient ID: " + patientId + ", Diagnosis: " + diagnosis + ", Treatment: " + treatment;
        }
    }

    static List<User> users = new ArrayList<>();
    static List<Patient> patients = new ArrayList<>();
    static List<Appointment> appointments = new ArrayList<>();
    static List<Staff> staffList = new ArrayList<>();
    static List<Invoice> invoices = new ArrayList<>();
    static List<Billing> billings = new ArrayList<>();
    static List<MedicalSupply> supplies = new ArrayList<>();
    static List<EHR> records = new ArrayList<>();
        static User currentUser = null;
    
        public static void main(String[] args) {
            // Default Admin UserName & Pass
            users.add(new User("admin", "admin123", "Admin"));
            login();
    
            while (true) {
                System.out.println("\nHospital Management System");
                System.out.println("1. Patient Management (Register/View Medical History/Test Reports)");
                System.out.println("2. Appointment Management (Schedule/View)");
                System.out.println("3. Staff Management (Add/View)");
                System.out.println("4. Invoice Management");
                System.out.println("5. Manage Supply or Invtory");
                System.out.println("6. Manage EHR Records");
                System.out.println("7. View/Search (Doctors/Patients)");
                System.out.println("8. Notifications and Availability");
                System.out.println("9. Backup Data");
                System.out.println("10. Logout");
                System.out.println("11. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
    
                switch (choice) {
                    case 1:
                        patientManagement();
                        break;
                    case 2:
                        appointmentManagement();
                        break;
                    case 3:
                        if (isAdmin()) staffManagement();
                        else System.out.println("Access Denied! Admin only.");
                        break;
                    case 4:
                        if (isAdmin()) invoiceManagement();
                        else System.out.println("Access Denied! Admin only.");
                        break;
                    case 5:
                        // manageSupplies();
                        if (isAdmin()) manageSupplies();
                        else System.out.println("Access Denied! Admin only.");
                        break;
                    case 6:
                        // manageSupplies();
                        if (isAuthentic()) manageEHR();
                        else System.out.println("Access Denied! Admin only.");
                        break;
                    case 7:
                        searchAndView();
                        break;
                    case 8:
                        notificationsAndAvailability();
                        break;
                    case 9:
                        if (isAdmin()) backupData();
                        else System.out.println("Access Denied! Admin only.");
                        break;
                    case 10:
                        logout();
                        break;
                    case 11:
                        System.out.println("Exiting... Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice! Try again.");
                }
            }
        }
    
        static void login() {
            while (currentUser == null) {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
    
                for (User user : users) {
                    if (user.username.equals(username) && user.password.equals(password)) {
                        currentUser = user;
                        System.out.println("Login successful! Welcome, " + user.username);
                        return;
                    }
                }
    
                System.out.println("Invalid credentials. Try again.");
            }
        }
    
        static void logout() {
            currentUser = null;
            System.out.println("Logged out successfully.");
            login();
        }
        static void backupData() {
            System.out.println("Backing up data...");
            backupToTextFile();
            // backupToExcelFile();
            System.out.println("Backup completed.");
    }

    static void backupToTextFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("backup.txt"))) {
            writer.write("Patients:\n");
            for (Patient patient : patients) {
                writer.write(patient.toString() + "\n");
            }

            writer.write("Appointment:\n");
            for (Appointment appoint : appointments) {
                writer.write(appoint.toString() + "\n");
            }

            writer.write("Electical Health Records:\n");
            for (EHR ehr : records) {
                writer.write(ehr.toString() + "\n");
            }

            writer.write("\nStaff:\n");
            for (Staff staff : staffList) {
                writer.write(staff.toString() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to text file: " + e.getMessage());
        }
    }
    
        static boolean isAdmin() {
            return currentUser != null && currentUser.role.equalsIgnoreCase("Admin");
        }
        static boolean isAuthentic() {
            return currentUser != null && (currentUser.role.equalsIgnoreCase("Admin") || currentUser.role.equalsIgnoreCase("Doctor"));
        }
    
        static void patientManagement() {
            System.out.println("1. Register Patient");
            System.out.println("2. View Patients");
            System.out.println("3. Manage Test Reports");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
    
            switch (choice) {
                case 1:
                    registerPatient();
                    break;
                case 2:
                    viewPatients();
                    break;
                case 3:
                    manageTestReports();
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    
        static void appointmentManagement() {
            System.out.println("1. Schedule Appointment");
            System.out.println("2. View Appointments");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
    
            switch (choice) {
                case 1:
                    scheduleAppointment();
                    break;
                case 2:
                    viewAppointments();
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    
        static void staffManagement() {
            System.out.print("Enter Staff ID: ");
            String staffId = scanner.nextLine();
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Role: ");
            String role = scanner.nextLine();
            System.out.print("Enter Specialization: ");
            String specialization = scanner.nextLine();
    
            staffList.add(new Staff(staffId, name, role, specialization));
            System.out.println("Staff added successfully!");
        }
        //only tha admin can send invoice// or doctor and admin can generate bills
        static void invoiceManagement(){
            System.out.println("1. Generate Bills");
            System.out.println("2. Send Invoice");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    generateBill(1);
                    break;
                case 2:
                    if (isAuthentic()) generateBill(2);
                    else System.out.println("Access Denied! Admin only.");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }
     

        static void invoiceSender(String patientId,double consultationFee,double medicineFee,double
        testFee) {
            Patient patien=getPatientById(patientId);
            double totalBill = consultationFee + testFee + medicineFee;
            System.out.println("Bill for Patient: " + patien.name);
            System.out.println("Consultation Fee: $" + consultationFee);
            System.out.println("Test Fee: $" + testFee);
            System.out.println("Medicine Fee: $" + medicineFee);
            System.out.println("Total Bill: $" + totalBill);
            invoices.add(new Invoice(invoiceCounter+"", patientId, totalBill));
            invoiceCounter++;
            System.out.println("Invoice generated successfully!");
        }
        public static Patient getPatientById(String patientId) {
                    for (Patient patient : patients) {
                        if (patient.id.equals(patientId)) {
                    return patient;
                }
            }
            return null;
        }
        private static void generateBill(int isBill) {
            System.out.print("Enter Patient ID: ");
            String patientId = scanner.nextLine();
            // scanner.nextLine();
            Patient patient =null;
            for (Patient pat : patients) {
                if (pat.id.equals(patientId)) {
                    patient= pat;
                    break;
                }
            }
            if (patient == null) {
                System.out.println("Patient not found.");
                return;
            }
    
            System.out.print("Enter Consultation Fee: ");
            double consultationFee = scanner.nextDouble();
            System.out.print("Enter Test Fee: ");
            double testFee = scanner.nextDouble();
            System.out.print("Enter Medicine Fee: ");
            double medicineFee = scanner.nextDouble();
            Billing billing= new Billing(patient, consultationFee, testFee, medicineFee);
            for (Billing bill : billings) {
                if(bill.equals(billing)){
                    return;
                }
            }
            if(isBill==1){
                billings.add(billing);
                System.out.println("Bill generetd SuccessFully !!");
            return ;
            }
            
            // return billing;
            invoiceSender(patient.id,billing.consultationFee,billing.medicineFe,billing.testFee);
    }


    static void searchAndView() {
        System.out.println("1. Search Doctor");
        System.out.println("2. Search Patient");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                System.out.print("Enter Doctor Name: ");
                String doctorName = scanner.nextLine();
                for (Staff staff : staffList) {
                    if (staff.role.equalsIgnoreCase("Doctor") && staff.name.equalsIgnoreCase(doctorName)) {
                        System.out.println(staff);
                        return;
                    }
                }
                System.out.println("Doctor not found.");
                break;
            case 2:
                System.out.print("Enter Patient Name: ");
                String patientName = scanner.nextLine();
                for (Patient patient : patients) {
                    if (patient.name.equalsIgnoreCase(patientName)) {
                        System.out.println(patient);
                        return;
                    }
                }
                System.out.println("Patient not found.");
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    static void notificationsAndAvailability() {
        System.out.println("1. View Doctors Available Today");
        System.out.println("2. Send Notifications to Doctors");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                viewDoctorsAvailableToday();
                break;
            case 2:
                sendNotificationsToDoctors();
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    static void registerPatient() {
        System.out.print("Enter Patient ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Contact: ");
        String contact = scanner.nextLine();
        System.out.print("Enter Diagnosis: ");
        String diagnosis = scanner.nextLine();

        patients.add(new Patient(id, name, age, contact, diagnosis));
        System.out.println("Patient registered successfully!");
    }

    static void viewPatients() {
        if (patients.isEmpty()) {
            System.out.println("No patients registered.");
        } else {
            System.out.println("\nRegistered Patients:");
            for (Patient patient : patients) {
                System.out.println(patient);
            }
        }
    }

    static void manageSupplies() {
        System.out.println("\nInventory Management:");
        System.out.println("1. Add Supply");
        System.out.println("2. View Supplies");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                addSupply();
                break;
            case 2:
                viewSupplies();
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    static void addSupply() {
        System.out.print("Enter Item ID: ");
        String itemId = scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        supplies.add(new MedicalSupply(itemId, name, quantity));
        System.out.println("Supply added successfully!");
    }

    static void viewSupplies() {
        if (supplies.isEmpty()) {
            System.out.println("No supplies available.");
        } else {
            System.out.println("\nMedical Supplies:");
            for (MedicalSupply supply : supplies) {
                System.out.println(supply);
            }
        }
    }


    static void manageEHR() {
        System.out.println("\nElectronic Health Records:");
        System.out.println("1. Add Record");
        System.out.println("2. View Records");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                addRecord();
                break;
            case 2:
                viewRecords();
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    static void addRecord() {
        System.out.print("Enter Record ID: ");
        String recordId = scanner.nextLine();
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        System.out.print("Enter Diagnosis: ");
        String diagnosis = scanner.nextLine();
        System.out.print("Enter Treatment: ");
        String treatment = scanner.nextLine();

        records.add(new EHR(recordId, patientId, diagnosis, treatment));
        System.out.println("Record added successfully!");
    }

    static void viewRecords() {
        if (records.isEmpty()) {
            System.out.println("No records available.");
        } else {
            System.out.println("\nElectronic Health Records:");
            for (EHR record : records) {
                System.out.println(record);
            }
        }
    }

    static void scheduleAppointment() {
        System.out.print("Enter Appointment ID: ");
        String appointmentId = scanner.nextLine();
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        System.out.print("Enter Doctor Name: ");
        String doctorName = scanner.nextLine();
        System.out.print("Enter Date (dd/mm/yyyy): ");
        String date = scanner.nextLine();

        appointments.add(new Appointment(appointmentId, patientId, doctorName, date));
        System.out.println("Appointment scheduled successfully!");
    }

    static void viewAppointments() {
        if (appointments.isEmpty()) {
            System.out.println("No appointments scheduled.");
        } else {
            System.out.println("\nScheduled Appointments:");
            for (Appointment appointment : appointments) {
                System.out.println(appointment);
            }
        }
    }

    static void viewDoctorsAvailableToday() {
        System.out.println("\nDoctors Available Today:");
        for (Staff staff : staffList) {
            if (staff.role.equalsIgnoreCase("Doctor")) {
                System.out.println(staff);
            }
        }
    }

    static void sendNotificationsToDoctors() {
        System.out.println("\nSending notifications to doctors about their upcoming appointments:");
        for (Appointment appointment : appointments) {
            System.out.println("Notification sent to Dr. " + appointment.doctorName + " for appointment on " + appointment.date);
        }
    }

    static void manageTestReports() {
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        for (Patient patient : patients) {
            if (patient.id.equalsIgnoreCase(patientId)) {
                System.out.println("\n1 Add Test Report 2. Remove Test Report");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print(" Enter Test Report Details: ");
                        String report = scanner.nextLine();
                        patient.testReports.add(report);
                        System.out.println("Test report added successfully!");
                        break;
                    case 2:
                        if (patient.testReports.isEmpty()) {
                            System.out.println("No test reports available to remove.");
                        } else {
                            System.out.println("\nAvailable Test Reports:");
                            for (int i = 0; i < patient.testReports.size(); i++) {
                                System.out.println((i + 1) + ". " + patient.testReports.get(i));
                            }
                            System.out.print("Enter the number of the report to remove: ");
                            int reportIndex = scanner.nextInt() - 1;
                            scanner.nextLine();

                            if (reportIndex >= 0 && reportIndex < patient.testReports.size()) {
                                patient.testReports.remove(reportIndex);
                                System.out.println("Test report removed successfully!");
                            } else {
                                System.out.println("Invalid selection!");
                            }
                        }
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
                return;
            }
        }
        System.out.println("Patient not found.");
    }
}

