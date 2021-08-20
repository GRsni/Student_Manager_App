package uca.esi.dni.handlers.Email;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import processing.data.JSONObject;
import uca.esi.dni.types.Student;

import java.util.Set;

/**
 * The interface Email handler i.
 */
public interface EmailHandlerI {

    /**
     * Is valid email address boolean.
     *
     * @param email the email
     * @return the boolean
     */
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

    /**
     * Send secret key emails.
     *
     * @param students the students
     */
    void sendSecretKeyEmails(Set<Student> students);

    /**
     * Send backup email.
     *
     * @param filepath the filepath
     */
    void sendBackupEmail(String filepath);

    /**
     * Load settings.
     *
     * @param settingsObject the settings object
     */
    void loadSettings(JSONObject settingsObject);
}
