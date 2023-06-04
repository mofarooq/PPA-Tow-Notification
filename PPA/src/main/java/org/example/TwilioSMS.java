package org.example;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

    public class TwilioSMS {
        public static final String ACCOUNT_SID = "AC19fdc1cf553dc7d2576713b3cffb2d6c";
        public static final String AUTH_TOKEN = "c199d6459ba8b7b7e5151e80ad99e845";
        public static final String TWILIO_PHONE_NUMBER = "+18775229485";

        public static void main(String[] args) {
            // Initialize Twilio client
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            // Provide the recipient's phone number and the message body
            String recipientPhoneNumber = "+16464070224";  // Replace with the recipient's phone number
            String messageBody = "Hello from Twilio! This is a test message.";

            // Send the message
            Message message = Message.creator(
                    new PhoneNumber(recipientPhoneNumber),
                    new PhoneNumber(TWILIO_PHONE_NUMBER),
                    messageBody
            ).create();

            // Print the message SID (unique identifier) if successfully sent
            System.out.println("Message sent successfully. SID: " + message.getSid());
        }
    }


