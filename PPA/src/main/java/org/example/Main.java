package org.example;

import org.json.simple.parser.ParseException;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws SQLException, IOException, ParseException {

                System.out.println("Running: " + new java.util.Date());

                GetTowedInfo connection = new GetTowedInfo();
                SqlNinja sqlNinja = new SqlNinja();
                Email emailSender = new Email();
                TwilioSMS twilioSMS = new TwilioSMS();
                MessageComposer messageComposer = new MessageComposer();

                ResultSet resultSet = sqlNinja.userSet();
                while (resultSet.next()) {

                    // Retrieve data from the result set for each individual row in mySQL table
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("Name");
                    String email = resultSet.getString("Email");
                    String license = resultSet.getString("License").replace("-", "").replace(" ","");
                    Boolean signUpMessageSent = resultSet.getBoolean("signUpMessage");
                    Boolean towedMessageSent = resultSet.getBoolean("towedMessage");
                    String phone = resultSet.getString("phoneNumber");

                    //Logging
                    System.out.println("Checking " + name + " (" + email + ", " + phone + ")" + " with license "
                    + license +". " + "Sign up Message sent: " + signUpMessageSent);

                    HashMap<String, String> carInfo = connection.creator(license);

                    String carTowedMessage = null;
                    if ((carInfo!=null))  {
                         carTowedMessage = messageComposer.carTowedMessage
                            (name, carInfo.get("License"), carInfo.get("StorageLotAddress"), carInfo.get("StorageLocation")
                            ,carInfo.get("TowedDate"), carInfo.get("Phone"));}

                    String signUpMessage = messageComposer.signUpMessage(name, license);


                    if (signUpMessageSent == false) {
                        emailSender.sendEmail(email, signUpMessage, false);
                        twilioSMS.sendMessage(phone, signUpMessage);
                        System.out.println("SIGN UP MESSAGE SENT TO " + name + "(" + phone + ", " + email + ")");
                        sqlNinja.updateEmailSent(id, "signUpMessage");
                    }

                    if (carInfo != null && towedMessageSent == false) {
                        emailSender.sendEmail(email, carTowedMessage, true);
                        twilioSMS.sendMessage(phone, carTowedMessage);
                        System.out.println("CAR TOWED MESSAGE SENT TO " + name + "(" + phone + ", " + email + ")");
                        sqlNinja.updateEmailSent(id, "towedMessage");
                     }}}}

