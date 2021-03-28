package uca.esi.dni.file;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import processing.data.JSONObject;
import uca.esi.dni.data.Student;

import java.util.*;
import java.util.logging.Logger;

public class EmailHandler {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static String sender;
    private static String username;
    private static String password;
    private static String backupEmail;
    private static final String host = "smtp.gmail.com";

    private static final String subjectText = "Clave segura para aplicación de Manual de Laboratorio Escuela Superior de Ingeniería";
    private static final String contentText = "<h1>Clave de acceso al manual de laboratorio de la Escuela Superior de Ingeniería</h1>" +
            "<p>Has recibido este correo porque se te ha incluido en la lista de alumnos con acceso a la aplicación " +
            "Android de manual de laboratorio de Resistencia de Materiales de la Escuela Superior de Ingeniería.</p>" +
            "<p><b>Tu clave de acceso es: %s .</b></p>" +
            "<p>Esta clave, junto con tu identificador único de la UCA (%s), son necesarios para acceder a la aplicación.</p>" +
            "<p>Este mensaje ha sido generado de forma automática. Si has recibido este mensaje por error, puedes ignorarlo. Si tu identificador único de la UCA " +
            "no coincide con el mostrado en el mensaje, envía un correo electrónico a la dirección: %s .</p>";

    private static final String backupHeaderText = "Archivo de copia de seguridad de datos de alumnos [%s]";
    private static final String backupContentText = "<p>Se adjunta la lista de contraseñas en texto plano de los alumnos añadidos a la base de datos.</p>";

    public EmailHandler() {
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

    public static void sendSecretKeyEmails(Set<Student> students) {
        Map<String, String> emailContentMap = new HashMap<>();
        for (Student student : students) {
            String formattedMessage = String.format(contentText, student.getKey(), student.getID(), sender);
            emailContentMap.put(student.getEmail(), formattedMessage);
        }
        sendEmailCollection(emailContentMap);
        System.out.println("[Mensaje/s enviado/s correctamente]: Numero de recipientes:" + students.size());
        LOGGER.info("[Mensaje/s enviado/s correctamente]: Numero de recipientes:" + students.size());
    }

    public static void sendBackupEmail(String attachment) {
        Session session = getSessionObject();
        String header = String.format(backupHeaderText, new Date().toString());
        sendHTMLEmail(backupEmail, header, backupContentText, attachment, session);
        System.out.println("[Mensaje enviado correctamente]: Copia de seguridad de alumnos introducidos.");
        LOGGER.info("[Mensaje enviado correctamente]: Copia de seguridad de alumnos introducidos.");
    }

    private static void sendEmailCollection(Map<String, String> emailContentMap) {
        Session session = getSessionObject();
        for (String email : emailContentMap.keySet()) {
            sendHTMLEmail(email, EmailHandler.subjectText, emailContentMap.get(email), session);
        }
    }

    private static Session getSessionObject() {
        Properties props = generatePropertiesObject();
        //create the Session object
        return Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    private static Properties generatePropertiesObject() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        return props;
    }

    private static void sendHTMLEmail(String dest, String header, String contentText, Session session) {
        sendHTMLEmail(dest, header, contentText, "", session);
    }

    private static void sendHTMLEmail(String dest, String header, String contentText, String attachment, Session session) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(dest));
            message.setSubject(header);
            message.setContent(contentText, "text/html; charset=UTF-8");
            if (attachment != null && !attachment.isEmpty()) {
                setAttachment(message, attachment);
            }
            Transport.send(message);
        } catch (MessagingException e) {
            System.err.println("[Error sending email]: " + e.getMessage());
            LOGGER.severe("[Error sending email]: " + e.getMessage());
        }
    }


    public static void setAttachment(Message message, String filename) throws MessagingException {
        // create a multipart message
        Multipart multipart = new MimeMultipart();
        BodyPart messageBodyPart = new MimeBodyPart();

        // specify your file
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);

        //Add the file part
        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);
    }

    private void loadSettings() {
        JSONObject settingsObject = UtilParser.loadJSONObject("data/files/settings.json");
        sender = settingsObject.getString("senderEmail");
        username = settingsObject.getString("senderUsername");
        password = settingsObject.getString("senderPassword");
        backupEmail = settingsObject.getString("backupEmail");
    }
}
