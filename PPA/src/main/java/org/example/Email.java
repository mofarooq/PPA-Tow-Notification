package org.example;
import java.util.HashMap;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;


public class Email {
    public Email() {

    };
    public void sendEmail(String email, String message, boolean towed) {

        // Sender details
        String from = "mfarooq@ihatetheppa.com";

        String subject = "Thank You For Signing Up!";
        // Email subject
        if (towed) {
             subject = "Your Car Has Been Towed!";
        }

        // Set the host and authentication properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", Credentials.EMAIL_HOST);
        properties.put("mail.smtp.port", Credentials.EMAIL_PORT);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.enable", "true");

        // Create a session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Credentials.EMAIL_USERNAME, Credentials.EMAIL_PASSWORD);
            }
        });

        try {
            // Create a MimeMessage object
            MimeMessage mimeMessage = new MimeMessage(session);

            // Set the sender, recipient, subject, and content
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);


            mimeMessage.setHeader("signUp", "Signed Up For Tow Notifications");

            session.setDebug(false);

            // Send the email
            Transport.send(mimeMessage);

           // System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }}}

