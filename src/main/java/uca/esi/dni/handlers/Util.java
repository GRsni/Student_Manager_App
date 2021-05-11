package uca.esi.dni.handlers;

import com.google.common.hash.Hashing;
import org.jetbrains.annotations.NotNull;
import processing.core.PApplet;
import processing.data.JSONObject;
import uca.esi.dni.types.Student;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    private static final Pattern idPattern = Pattern.compile("u[0-9xXyYzZ][0-9]{7}");

    private Util() {
    }

    public static boolean checkId(@NotNull String id) {
        Matcher m = idPattern.matcher(id);
        return m.matches();
    }

    public static String extractId(@NotNull String line) {

        Matcher m = idPattern.matcher(line);
        String match = "";

        // if we find a match, get the group
        if (m.find()) {
            match = m.group(0);
        }
        return match;
    }

    public static boolean checkFileExtension(String file, String ext) {
        String fileExtension = PApplet.checkExtension(file);
        if (fileExtension == null) {
            return false;
        } else {
            return fileExtension.equals(ext);
        }
    }

    @NotNull
    public static String getSHA256HashedString(String plain) {
        return Hashing.sha256().hashString(plain, StandardCharsets.UTF_8).toString();
    }

    public static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }


    public static JSONObject getStudentAttributeJSONObject(Set<Student> students, String attribute) {
        JSONObject jsonObject = new JSONObject();
        for (Student student : students) {
            if (attribute != null) {
                jsonObject.setString(student.getId(), student.getAttributeFromStudent(attribute));
            } else {
                jsonObject.put(student.getId(), JSONObject.NULL);
            }
        }
        return jsonObject;
    }

    public static Set<String> studentSetToStringSet(@NotNull Set<Student> students) throws NullPointerException {
        Set<String> stringSet = new HashSet<>();
        for (Student s : students) {
            stringSet.add(s.getId());
        }
        return stringSet;
    }

    public static Set<Student> getUniqueStudentSet(Set<Student> set1, Set<Student> set2) throws NullPointerException {
        if (set1 == null || set2 == null) {
            throw new NullPointerException("Student set cannot be null");
        } else {
            Set<Student> unique = new HashSet<>();
            for (Student s : set1) {
                boolean found = false;
                for (Student s2 : set2) {
                    if (s.getId().equals(s2.getId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    unique.add(s);
                }
            }
            return unique;
        }
    }

    public static Set<Student> getIntersectionOfStudentSets(Set<Student> set1, Set<Student> set2) throws NullPointerException {
        if (set1 == null || set2 == null) {
            throw new NullPointerException("Student set cannot be null");
        } else {
            Set<Student> coincident = new HashSet<>();
            for (Student s : set1) {
                for (Student s2 : set2) {
                    if (s.getId().equals(s2.getId())) {
                        coincident.add(s2);
                        break;
                    }
                }
            }
            return coincident;
        }
    }
}