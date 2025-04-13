package MiniProject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class TimetableGenerator {

    private static final String[] MORNING_SLOTS = {"9:00-9:50", "9:50-10:40", "10:50-11:40", "11:40-12:30"};
    private static final String LUNCH_BREAK = "12:30-1:30";
    private static final String BREAK = "10:40-10:50";
    private static final String AFTERNOON_SLOT = "2:00-4:00";
    private static final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

    public void generateAndDisplayTimetable(Connection connection) {
        try {
            String classQuery = "SELECT ClassroomID, ClassName FROM Classrooms";
            PreparedStatement classStmt = connection.prepareStatement(classQuery);
            ResultSet classResults = classStmt.executeQuery();

            System.out.println("Generating class-wise timetable...\n");

            while (classResults.next()) {
                int classroomID = classResults.getInt("ClassroomID");
                String className = classResults.getString("ClassName");

                System.out.println("Timetable for Class: " + className);

                Map<Integer, Integer> weeklyAssignments = new HashMap<>();
                Map<Integer, Integer> weeklyCredits = new HashMap<>();
                Map<Integer, String> courseNames = new HashMap<>();
                Map<Integer, String> teacherAssignments = new HashMap<>();
                List<Integer> labCourses = new ArrayList<>();
                List<Integer> nonLabCourses = new ArrayList<>();
                List<String> availableTeachers = new ArrayList<>();

                String facultyQuery = "SELECT FacultyName FROM Faculty";
                PreparedStatement facultyStmt = connection.prepareStatement(facultyQuery);
                ResultSet facultyResults = facultyStmt.executeQuery();

                while (facultyResults.next()) {
                    availableTeachers.add(facultyResults.getString("FacultyName"));
                }

                String courseQuery = "SELECT CourseID, CourseName, Credits FROM Course WHERE BranchID = ?";
                PreparedStatement courseStmt = connection.prepareStatement(courseQuery);
                courseStmt.setInt(1, classroomID);
                ResultSet courseResults = courseStmt.executeQuery();

                Random random = new Random();

                while (courseResults.next()) {
                    int courseID = courseResults.getInt("CourseID");
                    String courseName = courseResults.getString("CourseName");
                    int credits = courseResults.getInt("Credits");

                    weeklyAssignments.put(courseID, 0);
                    weeklyCredits.put(courseID, credits);
                    courseNames.put(courseID, courseName);

                    String randomTeacher = availableTeachers.get(random.nextInt(availableTeachers.size()));
                    teacherAssignments.put(courseID, randomTeacher);

                    if (courseName.toLowerCase().contains("lab")) {
                        labCourses.add(courseID);
                    } else {
                        nonLabCourses.add(courseID);
                    }
                }

                Collections.shuffle(labCourses, random);

                for (String day : DAYS) {
                    System.out.println("  " + day + ":");
                    int morningIndex = 0;
                    boolean labAssigned = false;
                    Set<Integer> coursesAssignedToday = new HashSet<>();

                    Collections.shuffle(nonLabCourses, random);

                    for (int courseID : nonLabCourses) {
                        int maxCredits = weeklyCredits.get(courseID);
                        int assignmentsSoFar = weeklyAssignments.get(courseID);

                        if (day.equals("Friday") || (!day.equals("Friday") && !coursesAssignedToday.contains(courseID))) {
                            if (assignmentsSoFar < maxCredits && morningIndex < MORNING_SLOTS.length) {
                                assignSlot(connection, classroomID, courseID, MORNING_SLOTS[morningIndex], day);
                                System.out.println("    - " + courseNames.get(courseID) + " | Time: " + MORNING_SLOTS[morningIndex]);
                                morningIndex++;
                                weeklyAssignments.put(courseID, assignmentsSoFar + 1);
                                coursesAssignedToday.add(courseID);
                            }
                        }

                        if (morningIndex == 2) {
                            System.out.println("    - Break | Time: " + BREAK);
                        }
                    }

                    System.out.println("    - Lunch Break | Time: " + LUNCH_BREAK);

                    if (!labAssigned && Arrays.asList("Monday", "Wednesday", "Friday").contains(day)) {
                        for (int courseID : labCourses) {
                            int maxCredits = weeklyCredits.get(courseID);
                            int assignmentsSoFar = weeklyAssignments.get(courseID);

                            if (assignmentsSoFar < maxCredits && !coursesAssignedToday.contains(courseID)) {
                                assignSlot(connection, classroomID, courseID, AFTERNOON_SLOT, day);
                                System.out.println("    - " + courseNames.get(courseID) + " | Time: " + AFTERNOON_SLOT);
                                weeklyAssignments.put(courseID, assignmentsSoFar + 1);
                                labAssigned = true;
                                coursesAssignedToday.add(courseID);
                                break;
                            }
                        }
                    }
                    System.out.println();
                }

                System.out.println("Teacher Assignments for Class: " + className);
                System.out.println("-------------------------------------");
                System.out.println("Subject                | Teacher");
                System.out.println("-------------------------------------");
                for (Map.Entry<Integer, String> entry : teacherAssignments.entrySet()) {
                    System.out.printf("%-20s | %s%n", courseNames.get(entry.getKey()), entry.getValue());
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void assignSlot(Connection connection, int classroomID, int courseID, String timeslot, String day) throws Exception {
        String insertQuery = "INSERT INTO Schedules (ClassroomID, CourseID, Day, TimeSlot) VALUES (?, ?, ?, ?)";
        PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
        insertStmt.setInt(1, classroomID);
        insertStmt.setInt(2, courseID);
        insertStmt.setString(3, day);
        insertStmt.setString(4, timeslot);
        insertStmt.executeUpdate();
    }

    public static void main(String[] args) {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            TimetableGenerator generator = new TimetableGenerator();
            generator.generateAndDisplayTimetable(connection);
        } else {
            System.out.println("Failed to connect to the database.");
        }
    }
}