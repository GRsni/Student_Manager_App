package uca.esi.dni.handlers;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import processing.data.JSONObject;
import uca.esi.dni.types.Student;

import java.util.Set;

public interface EmailHandlerI {

    static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    void sendSecretKeyEmails(Set<Student> students);

    void sendBackupEmail(String filepath);

    void loadSettings(JSONObject settingsObject);
}
