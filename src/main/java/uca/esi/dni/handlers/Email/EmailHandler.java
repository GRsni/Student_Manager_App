package uca.esi.dni.handlers.Email;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import processing.data.JSONObject;
import uca.esi.dni.types.Student;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

public class EmailHandler implements EmailHandlerI {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final String HOST = "smtp.gmail.com";
    private static final String GENERAL_HEADER_TEXT = "Clave segura para aplicación de Manual de Laboratorio Escuela Superior de Ingeniería";
    private static final String GENERAL_CONTENT_TEXT = "<h1>Clave de acceso al manual de laboratorio de la Escuela Superior de Ingeniería</h1>" +
            "<p>Has recibido este correo porque se te ha incluido en la lista de alumnos con acceso a la aplicación " +
            "Android de manual de laboratorio de Resistencia de Materiales de la Escuela Superior de Ingeniería.</p>" +
            "<p><b>Tu clave de acceso es: %s .</b></p>" +
            "<p>Esta clave, junto con tu identificador único de la UCA (%s), son necesarios para acceder a la aplicación.</p>" +
            "<p>Este mensaje ha sido generado de forma automática. Si has recibido este mensaje por error, puedes ignorarlo. Si tu identificador único de la UCA " +
            "no coincide con el mostrado en el mensaje, envía un correo electrónico a la dirección: %s .</p>";

    private static final String BACKUP_HEADER_TEXT = "Archivo de copia de seguridad de datos de alumnos [%s]";
    private static final String BACKUP_CONTENT_TEXT = "<p>Se adjunta la lista de contraseñas en texto plano de los alumnos añadidos a la base de datos.</p>";

    private String sender;
    private String username;
    private String password;
    private String backupEmail;


    public void sendSecretKeyEmails(Set<Student> students) {
        Map<String, String> emailContentMap = new HashMap<>();
        for (Student student : students) {
            String formattedMessage = String.format(GENERAL_CONTENT_TEXT, student.getKey(), student.getId(), sender);
            emailContentMap.put(student.getEmail(), formattedMessage);
        }
        int sentEmails = sendEmailCollection(emailContentMap);
        String toLog = "[Mensaje/s enviado/s correctamente]: Numero de recipientes:" + sentEmails;
        LOGGER.info(toLog);
    }

    private int sendEmailCollection(Map<String, String> emailContentMap) {
        int sentEmails = 0;
        Session session = getSessionObject();
        for (Map.Entry<String, String> entry : emailContentMap.entrySet()) {
            try {
                sendHTMLEmail(entry.getKey(), EmailHandler.GENERAL_HEADER_TEXT, entry.getValue(), session);
                sentEmails++;
            } catch (MessagingException e) {
                LOGGER.severe("[Error sending email]: " + e.getMessage());
            }
        }
        return sentEmails;
    }

    public void sendBackupEmail(String filepath) {
        try {
            File attachment = new File(filepath);
            Session session = getSessionObject();
            String header = String.format(BACKUP_HEADER_TEXT, new Date());
            sendHTMLEmail(backupEmail, header, BACKUP_CONTENT_TEXT, attachment, session);
            LOGGER.info("[Mensaje enviado correctamente]: Copia de seguridad de alumnos introducidos.");
        } catch (NullPointerException e) {
            LOGGER.severe("[Error while sending student data backup email]: " + e.getMessage());
        } catch (MessagingException e) {
            LOGGER.severe("[Error sending email]: " + e.getMessage());
        }
    }

    private Session getSessionObject() {
        Properties props = generatePropertiesObject();
        //create the Session object
        return Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    private Properties generatePropertiesObject() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", HOST);
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", "587");
        return props;
    }

    private void sendHTMLEmail(String dest, String header, String contentText, Session session) throws
            MessagingException {
        sendHTMLEmail(dest, header, contentText, null, session);
    }

    private void sendHTMLEmail(String dest, String header, String contentText, File attachment, Session
            session) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(dest));
        message.setSubject(header);
        setMultiPartContent(message, contentText, attachment);
        Transport.send(message);
    }

    private void setMultiPartContent(Message message, String textContent, File attachment) throws
            MessagingException {
        Multipart multipart = new MimeMultipart();
        BodyPart messageTextBodyPart = new MimeBodyPart();
        messageTextBodyPart.setContent(textContent, "text/html; charset=UTF-8");
        multipart.addBodyPart(messageTextBodyPart);
        if (attachment != null && attachment.exists()) {
            BodyPart attachmentBodyPart = getAttachmentBodyPart(attachment);
            multipart.addBodyPart(attachmentBodyPart);
        }
        message.setContent(multipart);
    }

    private BodyPart getAttachmentBodyPart(File filename) throws MessagingException {
        BodyPart messageBodyPart = new MimeBodyPart();

        // specify your file
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename.getName());

        return messageBodyPart;
    }

    public void loadSettings(JSONObject settingsObject) {
        sender = settingsObject.getString("senderEmail");
        username = settingsObject.getString("senderUsername");
        password = settingsObject.getString("senderPassword");
        backupEmail = settingsObject.getString("backupEmail");
    }
}
