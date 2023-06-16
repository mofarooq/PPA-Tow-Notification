package org.example;


import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

    public class TwilioSMS {
        public void sendMessage(String phone, String inputMessage) {
            // Initialize Twilio client
            Twilio.init(Credentials.TWILIO_ACCOUNT_SID, Credentials.TWILIO_AUTH_TOKEN);

            // Send the message

            try {
                Message message = Message.creator(
                        new PhoneNumber(phone),
                        new PhoneNumber(Credentials.TWILIO_PHONE_NUMBER),
                        inputMessage
                ).create();

                System.out.println("Message sent successfully. SID: " + message.getSid());
            } catch (ApiException e) {
                System.out.println("INVALID NUMBER");
            }


            // Print the message SID (unique identifier) if successfully sent

        }
    }


