class SrmStudent {

    static String collegeName;
    static String academicYear;

    String studentName;

    static {
        collegeName = "SRM Institute of Science and Technology";
        academicYear = "2026-2027";
        System.out.println("College info loaded");
    }

    SrmStudent(String studentName) {
        this.studentName = studentName;
    }

    void printConfirmation() {
        System.out.println(
            "Student record created: " + studentName
        );
    }
}

public class M4 {

    public static void main(String[] args) {

        String[] names = {
            "Ravi",
            "Meera",
            "Karthik",
            "Divya",
            "Anitha"
        };

        for (int i = 0; i < names.length; i++) {

            SrmStudent student =
                new SrmStudent(names[i]);

            student.printConfirmation();
        }
    }
}