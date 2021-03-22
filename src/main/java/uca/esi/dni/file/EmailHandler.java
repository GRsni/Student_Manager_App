package uca.esi.dni.file;

import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import processing.core.PApplet;
import processing.data.JSONObject;
import uca.esi.dni.data.Student;

import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

public class EmailHandler {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static PApplet parent;

    private static String sender;
    private static String username;
    private static String password;
    private static final String host = "smtp.gmail.com";

    private static final String subjectText = "Clave segura para aplicación de Manual de Laboratorio Escuela Superior de Ingeniería";
    private static final String contentText = "<h1>Clave de acceso al manual de laboratorio de la Escuela Superior de Ingeniería</h1>" +
            "<p>Has recibido este correo porque se te ha incluido en la lista de alumnos con acceso a la aplicación " +
            "Android de manual de laboratorio de Resistencia de Materiales de la Escuela Superior de Ingeniería.</p>" +
            "<p><b>Tu clave de acceso es: %s .</b></p>" +
            "<p>Esta clave, junto con tu identificador único de la UCA (%s), son necesarios para acceder a la aplicación.</p>" +
            "<p>Este mensaje ha sido generado de forma automática. Si has recibido este mensaje por error, puedes ignorarlo. Si tu identificador único de la UCA " +
            "no coincide con el mostrado en el mensaje, envía un correo electrónico a la dirección: %s .</p>";

    public EmailHandler(PApplet parent) {
        EmailHandler.parent = parent;
        loadSettings();
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public void sendSecretKeyEmails(Set<Student> students) {
        Session session = generateSessionObject();

        for (Student student : students) {
            String formattedMessage = String.format(contentText, student.getKey(), student.getID(), sender);
            sendHTMLEmail(student.getEmail(), subjectText, formattedMessage, session);

        }
        System.out.println("[Mensaje/s enviado/s correctamente]: Numero de recipientes:" + students.size());
        LOGGER.info("[Mensaje/s enviado/s correctamente]: Numero de recipientes:" + students.size());
    }

    private Session generateSessionObject() {
        Properties props = generatePropertiesObject();
        //create the Session object
        return Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    private Properties generatePropertiesObject() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        return props;
    }

    private void sendHTMLEmail(String dest, String header, String contentText, Session session) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(dest));
            message.setSubject(header);
            message.setContent(contentText, "text/html; charset=UTF-8");
            Transport.send(message);
        } catch (MessagingException e) {
            System.err.println("[Error sending email]: " + e.getMessage());
            LOGGER.severe("[Error sending email]: " + e.getMessage());
        }
    }


    private void loadSettings() {
        JSONObject settingsObject = EmailHandler.parent.loadJSONObject("data/files/settings.json");
        sender = settingsObject.getString("senderEmail");
        username = settingsObject.getString("senderUsername");
        password = settingsObject.getString("senderPassword");
    }
}
