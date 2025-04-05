package com.voting.kura.util;

import com.voting.kura.exception.VotingException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CourseCodeParser {

    /**
     * Parses a course code and extracts its components
     *
     * @param admissionNumber The admission number to parse (e.g., EB1/75122/24)
     * @return A map containing the parsed components:
     *         - facultyCode: The faculty identifier (e.g., "EB")
     *         - departmentCode: The department identifier (e.g., "1")
     *         - sequentialNumber: The sequential enrollment number (e.g., "75122")
     *         - admissionYear: The admission year (e.g., "24")
     * @throws VotingException if the course code is null, empty, or doesn't match the expected format
     */
    public static Map<String, String> parse(String admissionNumber) {
        Map<String, String> result = new HashMap<>();
        
        if (admissionNumber == null || admissionNumber.trim().isEmpty()) {
            throw new VotingException("Admission number cannot be null or empty");
        }
        
        // Example: EB1/75122/24
        String[] parts = admissionNumber.split("/");
        if (parts.length != 3) {
            throw new VotingException("Invalid admission number format");
        }

        // Extract faculty code (e.g., "EB")
        result.put("facultyCode", parts[0].replaceAll("\\d", ""));
        
        // Extract department code (e.g., "1")
        result.put("departmentCode", parts[0].replaceAll("\\D", ""));
        
        // Extract sequential number (e.g., "75122")
        result.put("sequentialNumber", parts[1]);
        
        // Extract admission year (e.g., "24")
        result.put("admissionYear", parts[2]);

        return result;
    }

    /**
     * Checks if two users belong to the same class based on their course codes
     * Same class means same faculty and department only
     *
     * @param courseCode1 First course code
     * @param courseCode2 Second course code
     * @return true if both users belong to the same faculty/department
     */
    public static boolean isSameClass(String courseCode1, String courseCode2) {
        Map<String, String> parsed1 = parse(courseCode1);
        Map<String, String> parsed2 = parse(courseCode2);

        return parsed1.get("facultyCode").equals(parsed2.get("facultyCode")) &&
               parsed1.get("departmentCode").equals(parsed2.get("departmentCode"));
        // Removed admissionYear check
    }

    /**
     * Checks if two users belong to the same faculty based on their course codes
     *
     * @param courseCode1 First course code
     * @param courseCode2 Second course code
     * @return true if both users belong to the same faculty
     */
    public static boolean isSameFaculty(String courseCode1, String courseCode2) {
        Map<String, String> parsed1 = parse(courseCode1);
        Map<String, String> parsed2 = parse(courseCode2);

        return parsed1.get("facultyCode").equals(parsed2.get("facultyCode"));
    }

    /**
     * Extracts faculty code from a course code
     *
     * @param courseCode The course code to parse
     * @return The faculty code
     */
    public static String extractFacultyCode(String courseCode) {
        return parse(courseCode).get("facultyCode");
    }

    /**
     * Extracts department code from a course code
     *
     * @param courseCode The course code to parse
     * @return The department code
     */
    public static String extractDepartmentCode(String courseCode) {
        return parse(courseCode).get("departmentCode");
    }

    /**
     * Extracts admission year from a course code
     *
     * @param courseCode The course code to parse
     * @return The admission year
     */
    public static String extractAdmissionYear(String courseCode) {
        return parse(courseCode).get("admissionYear");
    }
}
