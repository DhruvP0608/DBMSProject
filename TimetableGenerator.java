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

    private final Map<String, Map<String, List<String>>> classTimetables = new HashMap<>();
    private final Map<String, Map<String, String>> teacherAssignments = new HashMap<>();

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

                Map<String, List<String>> dailyTimetable = new HashMap<>();
                Map<String, String> teacherMap = new HashMap<>();

                Map<Integer, Integer> weeklyAssignments = new HashMap<>();
                Map<Integer, Integer> weeklyCredits = new HashMap<>();
                Map<Integer, String> courseNames = new HashMap<>();
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
                    teacherMap.put(courseName, randomTeacher);

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
                    List<String> schedule = new ArrayList<>();

                    Collections.shuffle(nonLabCourses, random);

                    for (int courseID : nonLabCourses) {
                        int maxCredits = weeklyCredits.get(courseID);
                        int assignmentsSoFar = weeklyAssignments.get(courseID);

                        if (day.equals("Friday") || (!day.equals("Friday") && !coursesAssignedToday.contains(courseID))) {
                            if (assignmentsSoFar < maxCredits && morningIndex < MORNING_SLOTS.length) {
                                schedule.add(courseNames.get(courseID) + " | Time: " + MORNING_SLOTS[morningIndex]);
                                System.out.println("    - " + courseNames.get(courseID) + " | Time: " + MORNING_SLOTS[morningIndex]);
                                morningIndex++;
                                weeklyAssignments.put(courseID, assignmentsSoFar + 1);
                                coursesAssignedToday.add(courseID);
                            }
                        }

                        if (morningIndex == 2) {
                            schedule.add("Break | Time: " + BREAK);
                        }
                    }

                    schedule.add("Lunch Break | Time: " + LUNCH_BREAK);
                    System.out.println("    - Lunch Break | Time: " + LUNCH_BREAK);

                    if (!labAssigned && Arrays.asList("Monday", "Wednesday", "Friday").contains(day)) {
                        for (int courseID : labCourses) {
                            int maxCredits = weeklyCredits.get(courseID);
                            int assignmentsSoFar = weeklyAssignments.get(courseID);

                            if (assignmentsSoFar < maxCredits && !coursesAssignedToday.contains(courseID)) {
                                String labEntry = courseNames.get(courseID) + "| Time: " + AFTERNOON_SLOT;
                                schedule.add(labEntry); // Ensure the lab is added to the day's schedule
                                System.out.println("    - " + labEntry);
                                weeklyAssignments.put(courseID, assignmentsSoFar + 1);
                                labAssigned = true;
                                coursesAssignedToday.add(courseID);
                                break;
                            }
                        }
                    }

                    dailyTimetable.put(day, schedule); // Save day's schedule to dailyTimetable
                    System.out.println();
                }

                classTimetables.put(className, dailyTimetable);
                teacherAssignments.put(className, teacherMap);

                System.out.println("Teacher Assignments for Class: " + className);
                System.out.println("-------------------------------------");
                System.out.println("Subject                | Teacher");
                System.out.println("-------------------------------------");
                for (Map.Entry<String, String> entry : teacherMap.entrySet()) {
                    System.out.printf("%-20s | %s%n", entry.getKey(), entry.getValue());
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<String>> getTimetable(String className) {
        return classTimetables.getOrDefault(className, new HashMap<>());
    }

    public Map<String, String> getTeacherAssignments(String className) {
        return teacherAssignments.getOrDefault(className, new HashMap<>());
    }
}
