import java.util.ArrayList;
import java.util.Scanner;

class Student {
    String name;
    int grade;

    Student(String name, int grade) {
        this.name = name;
        this.grade = grade;
    }
}

public class StudentGradeTracker {

    public static double calculateAverage(ArrayList<Student> students) {
        int sum = 0;

        for (Student s : students) {
            sum += s.grade;
        }

        return (double) sum / students.size();
    }

    public static int findHighest(ArrayList<Student> students) {
        int highest = students.get(0).grade;

        for (Student s : students) {
            if (s.grade > highest) {
                highest = s.grade;
            }
        }

        return highest;
    }

    public static int findLowest(ArrayList<Student> students) {
        int lowest = students.get(0).grade;

        for (Student s : students) {
            if (s.grade < lowest) {
                lowest = s.grade;
            }
        }

        return lowest;
    }

    public static void displayReport(ArrayList<Student> students) {

        System.out.println("\n===== STUDENT REPORT =====");

        for (Student s : students) {
            System.out.println(s.name + " : " + s.grade);
        }

        System.out.println("\nAverage Grade: " +
                calculateAverage(students));

        System.out.println("Highest Grade: " +
                findHighest(students));

        System.out.println("Lowest Grade: " +
                findLowest(students));
    }

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        ArrayList<Student> students = new ArrayList<>();

        System.out.print("Enter number of students: ");
        int n = input.nextInt();
        input.nextLine();

        for (int i = 1; i <= n; i++) {

            System.out.print("\nEnter student name: ");
            String name = input.nextLine();

            System.out.print("Enter grade: ");
            int grade = input.nextInt();
            input.nextLine();

            students.add(new Student(name, grade));
        }

        displayReport(students);

        input.close();
    }
}