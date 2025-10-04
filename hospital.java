import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class hospital {
    enum DiseaseCategory {
        // Cardiovascular
        HYPERTENSION("Hypertension", "Cardiologist"),
        CORONARY_ARTERY_DISEASE("Coronary Artery Disease", "Cardiologist"),
        HEART_ATTACK("Heart Attack", "Cardiologist"),

        // Neurological
        EPILEPSY("Epilepsy", "Neurologist"),
        STROKE("Stroke", "Neurologist"),
        MIGRAINE("Migraine", "Neurologist"),

        // Musculoskeletal
        ARTHRITIS("Arthritis", "Orthopedic"),
        OSTEOPOROSIS("Osteoporosis", "Orthopedic"),
        BONE_FRACTURE("Bone Fracture", "Orthopedic"),

        // Dental
        TOOTH_DECAY("Tooth Decay", "Dentist"),
        GUM_DISEASE("Gum Disease", "Dentist"),

        // Skin
        ACNE("Acne", "Dermatologist"),
        ECZEMA("Eczema", "Dermatologist"),
        PSORIASIS("Psoriasis", "Dermatologist"),

        // Respiratory
        ASTHMA("Asthma", "Pulmonologist"),
        BRONCHITIS("Bronchitis", "Pulmonologist"),
        TUBERCULOSIS("Tuberculosis", "Pulmonologist"),

        // Digestive
        GERD("Acid Reflux (GERD)", "Gastroenterologist"),
        ULCER("Ulcer", "Gastroenterologist"),
        HEPATITIS("Hepatitis", "Gastroenterologist"),

        // Endocrine
        DIABETES("Diabetes", "Endocrinologist"),
        HYPOTHYROIDISM("Hypothyroidism", "Endocrinologist"),
        PCOS("PCOS", "Endocrinologist"),

        // Infectious
        COVID19("COVID-19", "Infectious Disease Specialist"),
        MALARIA("Malaria", "Infectious Disease Specialist"),
        DENGUE("Dengue", "Infectious Disease Specialist"),

        // Pediatrics
        CHICKENPOX("Chickenpox", "Pediatrician"),
        MEASLES("Measles", "Pediatrician"),

        // Eye
        CATARACT("Cataract", "Ophthalmologist"),
        GLAUCOMA("Glaucoma", "Ophthalmologist"),

        // ENT
        SINUSITIS("Sinusitis", "ENT Specialist"),
        TONSILLITIS("Tonsillitis", "ENT Specialist"),

        // Mental Health
        DEPRESSION("Depression", "Psychiatrist"),
        ANXIETY("Anxiety", "Psychiatrist");
        final String disease, specialization;
        DiseaseCategory(String disease, String specialization) {
            this.disease = disease;
            this.specialization = specialization;
        }
        public static String getSpecialist(String disease) {
            for (DiseaseCategory d : values())
                if (d.disease.equalsIgnoreCase(disease)) return d.specialization;
            return null;
        }
    }

    static final String PATIENT_FILE = "patients.csv";
    static final String DOCTOR_FILE = "doctors.csv";
    static final String APPOINTMENT_FILE = "appointments.csv";
    static final String BILL_FILE = "bills.csv";

    static CardLayout layout = new CardLayout();
    static JPanel mainPanel = new JPanel(layout);
    static List<String[]> patients = new ArrayList<>();
    static List<String[]> doctors = new ArrayList<>();
    static List<String[]> appointments = new ArrayList<>();
    static List<String[]> bills = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Hospital Management");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 500);

            patients.addAll(loadData(PATIENT_FILE));
            doctors.addAll(loadData(DOCTOR_FILE));
            appointments.addAll(loadData(APPOINTMENT_FILE));
            bills.addAll(loadData(BILL_FILE));

            mainPanel.add(backgroundPanel(loginPanel(frame)), "login");
            mainPanel.add(backgroundPanel(dashboardPanel()), "dashboard");
            mainPanel.add(backgroundPanel(patientPanel()), "patient");
            mainPanel.add(backgroundPanel(doctorPanel()), "doctor");
            mainPanel.add(backgroundPanel(appointmentPanel()), "appointment");
            mainPanel.add(backgroundPanel(billingPanel()), "billing");

            frame.add(mainPanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    static JPanel backgroundPanel(JPanel content) {
        return new JPanel() {
            Image bg = new ImageIcon("logo_make_11_06_2023_377.jpg").getImage();
            {
                setLayout(new BorderLayout());
                content.setOpaque(false);
                add(content, BorderLayout.CENTER);
            }
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 700, 500, getWidth(), getHeight(), this);
            }
        };
    }

    static JPanel loginPanel(JFrame frame) {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 150, 50, 150));
        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();
        JButton login = new JButton("Login");

        panel.add(new JLabel("Username:")); panel.add(user);
        panel.add(new JLabel("Password:")); panel.add(pass);
        panel.add(login);

        login.addActionListener(e -> {
            if (user.getText().equals("admin") && new String(pass.getPassword()).equals("admin123"))
                layout.show(mainPanel, "dashboard");
            else
                JOptionPane.showMessageDialog(frame, "Incorrect credentials");
            user.setText("");
            pass.setText("");
        });

        return panel;
    }

    static JPanel dashboardPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        JButton patient = new JButton("Patient Module");
        JButton doctor = new JButton("Doctor Module");
        JButton appointment = new JButton("Appointment Module");
        JButton billing = new JButton("Billing Module");
        JButton logout = new JButton("Logout");

        panel.add(new JLabel("Dashboard", SwingConstants.CENTER));
        panel.add(patient); panel.add(doctor);
        panel.add(appointment); panel.add(billing); panel.add(logout);

        patient.addActionListener(e -> layout.show(mainPanel, "patient"));
        doctor.addActionListener(e -> layout.show(mainPanel, "doctor"));
        appointment.addActionListener(e -> layout.show(mainPanel, "appointment"));
        billing.addActionListener(e -> layout.show(mainPanel, "billing"));
        logout.addActionListener(e -> layout.show(mainPanel, "login"));

        return panel;
    }

    static JPanel patientPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(7, 2));
        JTextField id = new JTextField(), name = new JTextField(), gender = new JTextField(), disease = new JTextField(), contact = new JTextField();
        JButton add = new JButton("Add"), view = new JButton("View"), search = new JButton("Search"), remove = new JButton("Remove"), back = new JButton("Back");

        form.add(new JLabel("ID:")); form.add(id);
        form.add(new JLabel("Name:")); form.add(name);
        form.add(new JLabel("Gender:")); form.add(gender);
        form.add(new JLabel("Disease:")); form.add(disease);
        form.add(new JLabel("Contact:")); form.add(contact);
        form.add(add); form.add(view);
        form.add(search); form.add(remove);

        panel.add(form, BorderLayout.CENTER);
        panel.add(back, BorderLayout.SOUTH);

        add.addActionListener(e -> {
            if (id.getText().isEmpty() || name.getText().isEmpty() || disease.getText().isEmpty() || contact.getText().length() != 10) {
                JOptionPane.showMessageDialog(panel, "Fill all fields correctly. Contact must be 10 digits.");
                return;
            }
            String specialization = DiseaseCategory.getSpecialist(disease.getText());
            if (specialization == null) {
                JOptionPane.showMessageDialog(panel, "No matching specialist found.");
            } else {
                Optional<String[]> assignedDoctor = doctors.stream().filter(d -> d[2].equalsIgnoreCase(specialization)).findFirst();
                if (assignedDoctor.isPresent()) {
                    patients.add(new String[]{id.getText(), name.getText(), gender.getText(), disease.getText(), contact.getText(), assignedDoctor.get()[1]});
                    saveData(PATIENT_FILE, patients);
                    JOptionPane.showMessageDialog(panel, "Patient added. Assigned Doctor: " + assignedDoctor.get()[1]);
                    id.setText(""); name.setText(""); gender.setText(""); disease.setText(""); contact.setText("");
                } else {
                    JOptionPane.showMessageDialog(panel, "No doctor available for specialization: " + specialization);
                }
            }
        });

        view.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("ID | Name | Gender | Disease | Contact | Doctor\n");
            for (String[] p : patients) sb.append(String.join(" | ", p)).append("\n");
            showScrollText(panel, sb.toString());
        });

        search.addActionListener(e -> {
            String searchId = JOptionPane.showInputDialog("Enter Patient ID:");
            patients.stream().filter(p -> p[0].equals(searchId)).findFirst().ifPresentOrElse(
                    p -> JOptionPane.showMessageDialog(panel, "Found: " + String.join(" | ", p)),
                    () -> JOptionPane.showMessageDialog(panel, "Not found")
            );
        });

        remove.addActionListener(e -> {
            String removeId = JOptionPane.showInputDialog("Enter Patient ID to remove:");
            boolean removed = patients.removeIf(p -> p[0].equals(removeId));
            if (removed) saveData(PATIENT_FILE, patients);
            JOptionPane.showMessageDialog(panel, removed ? "Removed" : "Not found");
        });

        back.addActionListener(e -> layout.show(mainPanel, "dashboard"));
        return panel;
    }

    static JPanel doctorPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(6, 2));
        JTextField id = new JTextField(), name = new JTextField(), spec = new JTextField(), contact = new JTextField();
        JButton add = new JButton("Add"), view = new JButton("View"), search = new JButton("Search"), remove = new JButton("Remove"), back = new JButton("Back");

        form.add(new JLabel("ID:")); form.add(id);
        form.add(new JLabel("Name:")); form.add(name);
        form.add(new JLabel("Specialization:")); form.add(spec);
        form.add(new JLabel("Contact:")); form.add(contact);
        form.add(add); form.add(view);
        form.add(search); form.add(remove);

        panel.add(form, BorderLayout.CENTER);
        panel.add(back, BorderLayout.SOUTH);

        add.addActionListener(e -> {
            if (contact.getText().length() != 10) {
                JOptionPane.showMessageDialog(panel, "Contact must be 10 digits.");
                return;
            }
            doctors.add(new String[]{id.getText(), name.getText(), spec.getText(), contact.getText()});
            saveData(DOCTOR_FILE, doctors);
            JOptionPane.showMessageDialog(panel, "Doctor added");
            id.setText(""); name.setText(""); spec.setText(""); contact.setText("");
        });

        view.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("ID | Name | Spec | Contact\n");
            for (String[] d : doctors) sb.append(String.join(" | ", d)).append("\n");
            showScrollText(panel, sb.toString());
        });

        search.addActionListener(e -> {
            String searchId = JOptionPane.showInputDialog("Enter Doctor ID:");
            doctors.stream().filter(d -> d[0].equals(searchId)).findFirst().ifPresentOrElse(
                    d -> JOptionPane.showMessageDialog(panel, "Found: " + String.join(" | ", d)),
                    () -> JOptionPane.showMessageDialog(panel, "Not found")
            );
        });

        remove.addActionListener(e -> {
            String removeId = JOptionPane.showInputDialog("Enter Doctor ID to remove:");
            boolean removed = doctors.removeIf(d -> d[0].equals(removeId));
            if (removed) saveData(DOCTOR_FILE, doctors);
            JOptionPane.showMessageDialog(panel, removed ? "Removed" : "Not found");
        });

        back.addActionListener(e -> layout.show(mainPanel, "dashboard"));
        return panel;
    }

    static JPanel appointmentPanel() {
        return modulePanel("Appointment", appointments, APPOINTMENT_FILE, "ID", "PatientID", "DoctorID", "Date", "Time");
    }

    static JPanel billingPanel() {
        return modulePanel("Billing", bills, BILL_FILE, "BillID", "PatientID", "Amount");
    }

    static JPanel modulePanel(String title, List<String[]> list, String file, String... fields) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(fields.length + 2, 2));
        List<JTextField> inputs = new ArrayList<>();
        for (String field : fields) {
            JTextField tf = new JTextField();
            inputs.add(tf);
            form.add(new JLabel(field + ":")); form.add(tf);
        }

        JButton add = new JButton("Add"), view = new JButton("View"), search = new JButton("Search"), remove = new JButton("Remove"), back = new JButton("Back");
        form.add(add); form.add(view);
        form.add(search); form.add(remove);

        panel.add(form, BorderLayout.CENTER);
        panel.add(back, BorderLayout.SOUTH);

        add.addActionListener(e -> {
            String[] record = inputs.stream().map(JTextField::getText).toArray(String[]::new);
            list.add(record);
            saveData(file, list);
            JOptionPane.showMessageDialog(panel, title + " added");
            inputs.forEach(tf -> tf.setText(""));
        });

        view.addActionListener(e -> {
            StringBuilder sb = new StringBuilder(String.join(" | ", fields) + "\n");
            for (String[] r : list) sb.append(String.join(" | ", r)).append("\n");
            showScrollText(panel, sb.toString());
        });

        search.addActionListener(e -> {
            String searchId = JOptionPane.showInputDialog("Enter " + fields[0] + " to search:");
            list.stream().filter(r -> r[0].equals(searchId)).findFirst().ifPresentOrElse(
                    r -> JOptionPane.showMessageDialog(panel, "Found: " + String.join(" | ", r)),
                    () -> JOptionPane.showMessageDialog(panel, "Not found")
            );
        });

        remove.addActionListener(e -> {
            String removeId = JOptionPane.showInputDialog("Enter " + fields[0] + " to remove:");
            boolean removed = list.removeIf(r -> r[0].equals(removeId));
            if (removed) saveData(file, list);
            JOptionPane.showMessageDialog(panel, removed ? "Removed" : "Not found");
        });

        back.addActionListener(e -> layout.show(mainPanel, "dashboard"));
        return panel;
    }

    static void showScrollText(Component parent, String text) {
        JTextArea area = new JTextArea(20, 40);
        area.setText(text); area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        JOptionPane.showMessageDialog(parent, scroll, "Details", JOptionPane.INFORMATION_MESSAGE);
    }

    static List<String[]> loadData(String filename) {
        List<String[]> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line.split(","));
            }
        } catch (IOException e) {
            System.out.println("No file: " + filename);
        }
        return list;
    }

    static void saveData(String filename, List<String[]> list) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (String[] record : list) {
                writer.println(String.join(",", record));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

