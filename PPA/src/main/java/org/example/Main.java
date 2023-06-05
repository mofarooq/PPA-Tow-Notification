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
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String license = resultSet.getString("license");
                    Boolean firstEmail = resultSet.getBoolean("firstEmail");
                    String phone = resultSet.getString("PhoneNumber");

                    System.out.println(license);
                    System.out.println(email);
                    System.out.println(firstEmail);

                    HashMap<String, String> carInfo = connection.creator(license);

//                  String message = messageComposer.carTowedMessage
//                            (name, carInfo.get("License"), carInfo.get("StorageLotAddress"), carInfo.get("StorageLocation")
//                            ,carInfo.get("TowedDate"), carInfo.get("Phone"));

                    String fakeMessage = messageComposer.carTowedMessage
                            (name, "c", "d", "e"
                                    ,"f", "g");

                    if (firstEmail == false) {
                        emailSender.sendEmail(email, "SIGN UP EMAIL");
                        twilioSMS.sendMessage(phone, "SIGN UP TEXT");
                        System.out.println("MESSAGE SENT TO" + phone);
                        sqlNinja.updateFirstEmail(id);
                    }

                    if (email.equals("smrsmr0502@gmail.com")) {  //if (!carinfo = null))
                        System.out.println("HIT");
                        emailSender.sendEmail(email, fakeMessage);
                        twilioSMS.sendMessage(phone, fakeMessage);
                        }}}}
