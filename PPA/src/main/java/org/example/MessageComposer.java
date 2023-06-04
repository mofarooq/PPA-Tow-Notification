package org.example;

public class MessageComposer {

    public String carTowedMessage(String name, String license, String storageLotAddress, String storageLocation, String towedDate, String phone) {

        String message =        "Dear "
                                + name
                                +", \n"
                                + "If you are receiving this message, your car has been towed."
                                + " Your car with license plate "
                                + license
                                + " has been towed to "
                                + storageLotAddress
                                + " "
                                + storageLocation
                                + ". "
                                + "It was towed at "
                                + towedDate
                                + ". "
                                + "The number to the tow lot is "
                                + phone
                                +".";

            return message;
    }

}
