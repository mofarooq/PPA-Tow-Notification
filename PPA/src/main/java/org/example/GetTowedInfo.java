package org.example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class GetTowedInfo {

    public GetTowedInfo() {

    }

    /**
     *
     * @param licensePlate retrieved from Google sheets
     * @return hashmap of important information about car and tow location
     * if car is towed.
     * @throws IOException
     * @throws ParseException
     */
    public HashMap<String, String> creator(String licensePlate) throws IOException, ParseException {

        //ESTABLISH CONNECTION
        URL url = new URL("https://ppaapi.govtow.com/FMTC/SearchTowedVehicle/GetTowedCitizen?PlateNum="+licensePlate+"&Vin=&MakeId=0&year=&Location=&pageNum=1&pageSize=20");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "Basic");
        conn.setRequestProperty("dlmsauth", "admin");
        conn.setRequestMethod("GET");
        conn.connect();

        //READ BODY
        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        StringBuilder sb = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }

        sb = new StringBuilder(sb.substring(1, sb.length() - 1));

        HashMap<String, String> map = new HashMap<>();
        //if valid response (car towed) create hashmap of important values and return it
        if(sb.length()>10) {
            //PARSE TO JSON
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(String.valueOf(sb));

            map.put("License", String.valueOf(json.get("LicensePlate")));
            map.put("StorageLotAddress", String.valueOf(json.get("StorageLotAddress")));
            map.put("StorageLocation", String.valueOf(json.get("StorageLocation")));
            map.put("TowedDate", String.valueOf(json.get("TowedDate")));
            map.put("Phone", String.valueOf(json.get("Phone")));
            map.put("TowedFrom", String.valueOf(json.get("towed_Street_name")));

            return map;
        } else return null;
    }}