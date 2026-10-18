import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * NotificationChannel Interface
 * Defines the contract for all notification delivery mechanisms.
 */
interface NotificationChannel {
    /**
     * Sends a notice to a specific student.
     *
     * @param studentName Name of the student receiving the notice.
     * @param noticeTitle Title of the notice being broadcast.
     */
    void send(String studentName, String noticeTitle);
}

/**
 * Concrete implementation of NotificationChannel for Email notifications.
 */
class EmailChannel implements NotificationChannel {
    @Override
    public void send(String studentName, String noticeTitle) {
        System.out.println("[Email → " + studentName + "] " + noticeTitle + ".");
    }
}

/**
 * Concrete implementation of NotificationChannel for SMS notifications.
 */
class SmsChannel implements NotificationChannel {
    @Override
    public void send(String studentName, String noticeTitle) {
        System.out.println("[SMS → " + studentName + "] " + noticeTitle + ".");
    }
}

/**
 * Concrete implementation of NotificationChannel for In-App notifications.
 */
class AppChannel implements NotificationChannel {
    @Override
    public void send(String studentName, String noticeTitle) {
        System.out.println("[App → " + studentName + "] " + noticeTitle + ".");
    }
}

/**
 * Student Class
 * Represents a campus student with department affiliation and preferred notification channels.
 */
class Student {
    private final String name;
    private final String department;
    private final List<NotificationChannel> preferredChannels;

    public Student(String name, String department) {
        this(name, department, new ArrayList<>());
    }

    public Student(String name, String department, List<NotificationChannel> preferredChannels) {
        this.name = name;
        this.department = department;
        this.preferredChannels = new ArrayList<>(preferredChannels != null ? preferredChannels : Collections.emptyList());
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public List<NotificationChannel> getPreferredChannels() {
        return Collections.unmodifiableList(preferredChannels);
    }

    public void addPreferredChannel(NotificationChannel channel) {
        if (channel != null && !preferredChannels.contains(channel)) {
            preferredChannels.add(channel);
        }
    }
}

/**
 * Notice Class
 * Encapsulates the notice title and the target departments for the broadcast.
 */
class Notice {
    private final String title;
    private final List<String> targetDepartments;

    public Notice(String title, List<String> targetDepartments) {
        this.title = title;
        this.targetDepartments = targetDepartments != null ? new ArrayList<>(targetDepartments) : new ArrayList<>();
    }

    public Notice(String title, String... departments) {
        this(title, departments != null ? Arrays.asList(departments) : Collections.emptyList());
    }

    public String getTitle() {
        return title;
    }

    public List<String> getTargetDepartments() {
        return Collections.unmodifiableList(targetDepartments);
    }
}

/**
 * NoticeBoard Class
 * Maintains registered students, validates notices before posting, filters target students
 * by department, and delivers notifications polymorphically through each student's preferred channels.
 */
class NoticeBoard {
    private final List<Student> students;

    public NoticeBoard() {
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        if (student != null) {
            students.add(student);
        }
    }

    /**
     * Validates whether a notice can be posted.
     * A notice must have a non-empty title and at least one target department.
     *
     * @param notice The notice to validate.
     * @return true if valid; false otherwise.
     */
    public boolean validateNotice(Notice notice) {
        if (notice == null) {
            System.out.println("Cannot post notice: Notice cannot be null.");
            return false;
        }
        if (notice.getTitle() == null || notice.getTitle().trim().isEmpty()) {
            System.out.println("Cannot post notice: Title is required.");
            return false;
        }
        if (notice.getTargetDepartments() == null || notice.getTargetDepartments().isEmpty()) {
            System.out.println("Cannot post notice: At least one target department is required.");
            return false;
        }
        return true;
    }

    /**
     * Posts a notice after validation, filters students belonging to the target departments,
     * and broadcasts the notice via their preferred notification channels.
     *
     * @param notice The notice to post.
     */
    public void postNotice(Notice notice) {
        if (!validateNotice(notice)) {
            return;
        }

        // Print header indicating notice has been posted to target departments
        String departments = String.join(", ", notice.getTargetDepartments());
        System.out.println("Notice '" + notice.getTitle() + "' posted to " + departments + ".");

        // Deliver notice to matching students via their preferred channels (Polymorphism)
        for (Student student : students) {
            if (isStudentInTargetDepartments(student, notice.getTargetDepartments())) {
                for (NotificationChannel channel : student.getPreferredChannels()) {
                    channel.send(student.getName(), notice.getTitle());
                }
            }
        }
    }

    private boolean isStudentInTargetDepartments(Student student, List<String> targetDepartments) {
        if (student.getDepartment() == null) {
            return false;
        }
        for (String dept : targetDepartments) {
            if (student.getDepartment().equalsIgnoreCase(dept)) {
                return true;
            }
        }
        return false;
    }
}

/**
 * Main application class demonstrating the Campus Notice Broadcaster.
 */
public class Q5_CampusNoticeBroadcaster {
    public static void main(String[] args) {
        // 1. Initialize notification channels
        NotificationChannel email = new EmailChannel();
        NotificationChannel sms = new SmsChannel();
        NotificationChannel app = new AppChannel();

        // 2. Create students with their department and preferred channels
        Student asha = new Student("Asha", "CSE", Arrays.asList(email, app));
        Student ravi = new Student("Ravi", "ECE", Arrays.asList(sms));

        // 3. Register students to the NoticeBoard
        NoticeBoard noticeBoard = new NoticeBoard();
        noticeBoard.addStudent(asha);
        noticeBoard.addStudent(ravi);

        // 4. Post Notice 1: Target department CSE
        Notice notice1 = new Notice("Lab Closed Tomorrow", Arrays.asList("CSE"));
        noticeBoard.postNotice(notice1);

        // 5. Post Notice 2: Target departments CSE and ECE
        Notice notice2 = new Notice("Fee Deadline Extended", Arrays.asList("CSE", "ECE"));
        noticeBoard.postNotice(notice2);

        // 6. Post Notice 3: Attempt to post invalid notice without target departments
        Notice notice3 = new Notice("Campus Maintenance", Collections.emptyList());
        noticeBoard.postNotice(notice3);
    }
}
