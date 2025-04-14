package MiniProject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TimetableReader {

    private final Map<String, Map<String, List<String>>> classTimetables = new HashMap<>();
    private final Map<String, Map<String, String>> teacherAssignments = new HashMap<>();

    public void readTimetableOutput(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            String currentClass = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("Timetable for Class:")) {
                    currentClass = line.split(":")[1].trim();
                    classTimetables.put(currentClass, new HashMap<>());
                } else if (currentClass != null && Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday").contains(line.replace(":", ""))) {
                    String day = line.replace(":", "");
                    List<String> schedule = new ArrayList<>();

                    while ((line = reader.readLine()) != null && !line.trim().isEmpty() && !line.endsWith(":")) {
                        schedule.add(line.trim());
                    }

                    classTimetables.get(currentClass).put(day, schedule);
                } else if (line.startsWith("Teacher Assignments for Class:")) {
                    currentClass = line.split(":")[1].trim();
                    teacherAssignments.put(currentClass, new HashMap<>());
                } else if (currentClass != null && line.contains("|")) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 2) {
                        String subject = parts[0].trim();
                        String teacher = parts[1].trim();
                        teacherAssignments.get(currentClass).put(subject, teacher);
                    }
                }
            }
        } catch (IOException e) {
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
