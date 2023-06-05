package org.example;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

    public class TwilioSMS {
        public void sendMessage(String phone, String inputMessage) {
            // Initialize Twilio client
            Twilio.init(Credentials.TWILIO_ACCOUNT_SID, Credentials.TWILIO_AUTH_TOKEN);

            // Provide the recipient's phone number and the message body
            String recipientPhoneNumber = "+16464070224";  // Replace with the recipient's phone number
            String messageBody = inputMessage;

            // Send the message
            Message message = Message.creator(
                    new PhoneNumber(recipientPhoneNumber),
                    new PhoneNumber(Credentials.TWILIO_PHONE_NUMBER),
                    inputMessage
            ).create();

            // Print the message SID (unique identifier) if successfully sent
            System.out.println("Message sent successfully. SID: " + message.getSid());
        }
    }


