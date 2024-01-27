package org.example;

public class MessageComposer {

    public String carTowedMessage(String name, String license, String storageLotAddress, String storageLocation, String towedDate, String phone) {

        String message =        "Hey "
                                + name
                                +"! \n"
                                +"\n"
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
                                +"."
                                +"\n"
                                +"\n"
                                +"The PPA will charge you a tow fee of $175 and an additional storage fee of $25 every day your vehicle sits in their lot. " +
                                "When picking up your vehicle, make sure to bring your license, insurance, and registration or the PPA will not allow you to take your vehicle. " +
                                "If these documents are in your vehicle, the PPA will allow you to retrieve them from your vehicle at the lot."
                                +"\n"
                                +"\n"
                                +"P.S. We hate the PPA too :)";

            return message;
    }

    public String signUpMessage(String name, String licensePlate) {
        String message = "Hey " + name + "! Confirming that your vehicle with license plate " + licensePlate + " is now signed up for PPA Tow Alerts. If this vehicle ever gets towed, you will receive an email and text with information on how to retrieve it.";
        return message;

    }

}
