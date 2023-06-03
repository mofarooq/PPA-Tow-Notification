package org.example;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.sheets.v4.SheetsScopes;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.parser.ParseException;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GMailer {
    public final Gmail gmailService;
    public GMailer() throws GeneralSecurityException, IOException {
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        gmailService = new Gmail.Builder(HTTP_TRANSPORT, jsonFactory, getCredentials(HTTP_TRANSPORT, jsonFactory))
                .setApplicationName("PPA TOWED")
                .build();

    }
    public static void main(String[] args) throws SQLException, GeneralSecurityException, IOException, ParseException {

                System.out.println("Running: " + new java.util.Date());
                GetTowedInfo connection = new GetTowedInfo();
                GMailer service = new GMailer();

                SqlNinja sqlNinja = new SqlNinja();
                ResultSet resultSet = sqlNinja.userSet();

                while (resultSet.next()) {
                    // Retrieve data from the result set for each individual row in mySQL table
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String license = resultSet.getString("license");
                    String firstEmail = resultSet.getString("firstEmail");
                    HashMap<String, String> carInfo;

                    System.out.println(license);

                    carInfo = connection.creator(license);

                    if (carInfo != null) {
                        try {
                            service.sendMail("YOUR CAR HAS BEEN TOWED",
                                    "Dear, "
                                            + name
                                            + " If you are receiving this message, your car has been towed."
                                            + " Your car with license plate "
                                            + carInfo.get("License")
                                            + " has been towed to "
                                            + carInfo.get("StorageLotAddress")
                                            + " "
                                            + carInfo.get("StorageLocation")
                                            + ". "
                                            + "It was towed at "
                                            + carInfo.get("TowedDate")
                                            + ". "
                                            + "The number to the tow lot is "
                                            + carInfo.get("Phone")
                                            +".",
                                    email,
                                    carInfo.get("License"));

                        } catch (GeneralSecurityException | IOException | MessagingException e) {
                            throw new RuntimeException(e);
                        }}}}

    /**
     * If car is towed, send email to appropriate email
     * @param subject
     * @param message
     * @param emailAddress
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws MessagingException
     */
    private void sendMail(String subject, String message, String emailAddress, String licensePlate) throws GeneralSecurityException, IOException, MessagingException {

        // Encode as MIME message
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("yourcargotowed@gmail.com"));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(emailAddress));
        email.setSubject(subject);
        email.setText(message);

        // Encode and wrap the MIME message into a gmail message
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message msg = new Message();
        msg.setRaw(encodedEmail);

        try {
            // Create send message
            msg = gmailService.users().messages().send("me", msg).execute();
            //System.out.println("Message id: " + msg.getId());
            System.out.println("Message sent to " + emailAddress + " for license plate " +licensePlate);
            //System.out.println(msg.toPrettyString());
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            } else {
                throw e;
            }}}


    /**
     *
     * @param httpTransport
     * @param jsonFactory
     * @return Oauth credentials for accessing Google APIs
     * @throws IOException
     */
    private static Credential getCredentials(final NetHttpTransport httpTransport, GsonFactory jsonFactory)
            throws IOException {
        // Load client secrets.
        InputStream in = GMailer.class.getResourceAsStream("/credentials.json");
        if (in == null) {
            throw new FileNotFoundException("Resource not found: ");
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(GsonFactory.getDefaultInstance(), new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, List.of(GmailScopes.GMAIL_SEND, SheetsScopes.SPREADSHEETS))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        //returns an authorized Credential object.
        return credential;
    }
}
