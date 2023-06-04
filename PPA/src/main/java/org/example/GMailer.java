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
    public static void main(String[] args) throws SQLException, IOException, ParseException {

                System.out.println("Running: " + new java.util.Date());
                GetTowedInfo connection = new GetTowedInfo();

                SqlNinja sqlNinja = new SqlNinja();
                ResultSet resultSet = sqlNinja.userSet();

                Email emailSender = new Email();

                while (resultSet.next()) {
                    // Retrieve data from the result set for each individual row in mySQL table
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String license = resultSet.getString("license");
                    String firstEmail = resultSet.getString("firstEmail");
                    HashMap<String, String> carInfo;

                    System.out.println(license);
                    System.out.println(email);

                    carInfo = connection.creator(license);

                    if (email.equals("smrsmr0502@gmail.com")) {  //if (!carinfo = null))
                        System.out.println("HIT");
                        emailSender.sendEmail(email,"Dear, "
                                + name
                                + " If you are receiving this message, your car has been towed."
                                + " Your car with license plate "
                               // + carInfo.get("License")
                                + " has been towed to "
                                //+ carInfo.get("StorageLotAddress")
                                + " "
                                //+ carInfo.get("StorageLocation")
                                + ". "
                                + "It was towed at "
                                //+ carInfo.get("TowedDate")
                                + ". "
                                + "The number to the tow lot is "
                               // + carInfo.get("Phone")
                                +".");
                        }}}}
