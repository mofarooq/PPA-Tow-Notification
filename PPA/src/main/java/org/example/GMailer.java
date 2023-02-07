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
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.sun.tools.javac.Main;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.parser.ParseException;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class GMailer {

    public final Gmail gmailService;
    private static Sheets sheetsService;
    private final String spreadsheetId = "16HD4S0ZHSVi3k-Vz75tyiPaRS9gV7kUmi9BTku97yOg";


    public GMailer() throws GeneralSecurityException, IOException {
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        gmailService = new Gmail.Builder(HTTP_TRANSPORT, jsonFactory, getCredentials(HTTP_TRANSPORT, jsonFactory))
                .setApplicationName("PPA TOWED")
                .build();

        sheetsService =
                new Sheets.Builder(HTTP_TRANSPORT, jsonFactory, getCredentials(HTTP_TRANSPORT, jsonFactory))
                        .setApplicationName("PPA TOWED")
                        .build();
    }



    public static void main(String[] args) throws GeneralSecurityException, IOException, MessagingException, ParseException {

       //run on timed schedule
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Running: " + new java.util.Date());
                GetTowedInfo connection = new GetTowedInfo();
                GMailer service;
                try {
                    service = new GMailer();
                } catch (GeneralSecurityException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                //hashmap of license plate/email from google sheets
                HashMap<String, String> userInfo = null;
                try {
                    userInfo = service.readSheet();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                for (String key: userInfo.keySet()) {
                    //hashmap of car information
                    HashMap<String, String> carInfo = null;
                    try {
                        carInfo = connection.creator(key);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    if (carInfo != null) {
                        try {
                            service.sendMail("YOUR CAR HAS BEEN TOWED",
                                    "If you are receiving this message, your car has been towed. "
                                            + "Your car with license plate "
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
                                    userInfo.get(key),
                                    carInfo.get("License"));
                        } catch (GeneralSecurityException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (MessagingException e) {
                            throw new RuntimeException(e);
                        }
                    }}}}, 0, 60000);

    }

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
     * @return hashmap of all license plate, email key-value pairs in Excel Sheet for Google Forms Responses
     * @throws IOException
     */
    public HashMap<String, String> readSheet() throws IOException {
        String sheetName = "Form Responses 1!";

        List<List<Object>> readResult = sheetsService.spreadsheets().values()
                .get(spreadsheetId, sheetName + "B2:C1000")
                .execute()
                .getValues();

        String[][] array = new String[readResult.size()][];

        int i = 0;
        for (List<Object> row : readResult) {
            array[i++] = row.toArray(new String[row.size()]);
        }

        HashMap<String, String> userInfo = new HashMap<>();

        //clean up
        for(int y=0;y<array.length;y++) {
            String str = Arrays.deepToString(array[y]);
            String[] arrOfStr = str.split(",", -1);
            arrOfStr[0] = arrOfStr[0].substring(1, arrOfStr[0].length());
            arrOfStr[1] = arrOfStr[1].trim().substring(0,arrOfStr[1].length()-2);
            userInfo.put(arrOfStr[0], arrOfStr[1]);
        }

        //KEY IS LICENSE PLATE
        //VALUE IS EMAIL
        return userInfo;
    }


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
