package org.example;
import java.sql.*;

public class SqlNinja {

    public final String jdbcUrl = "com.mysql.cj.jdbc.Driver";
    public ResultSet userSet()  {
        // SQL query to execute
        String query = "SELECT * FROM final_ppa_userbase";

        ResultSet resultSetFinal = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName(jdbcUrl);

            // Establish the database connection
            Connection connection = DriverManager.getConnection(Credentials.SQL_URL, Credentials.SQL_USERNAME, Credentials.SQL_PASSWORD);

            // Create a statement object to execute the query
            Statement statement = connection.createStatement();

            // Execute the query and obtain the result set
            ResultSet resultSet = statement.executeQuery(query);

            resultSetFinal = resultSet;

//            resultSet.close();
//            statement.close();
//            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

        }
        return resultSetFinal;
    }

    //add way to update towedEmail. This is just for sign up email. Then different cron job/docker container will reset towedEmail for all rows
    //back to 0 every 24 hours
    public void updateEmailSent(int id, String message) {
        //message = signUpMessage for first email
        //message = towedMessage for towed

        // SQL query to update the column
        String sql = "UPDATE final_ppa_userbase SET " + message + " = ? WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(Credentials.SQL_URL, Credentials.SQL_USERNAME, Credentials.SQL_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the new value for FirstEmail column
            boolean newValue = true;
            statement.setBoolean(1, newValue);

            // Set the ID of the row to update
            statement.setInt(2, id);

            // Execute the update query
            int rowsAffected = statement.executeUpdate();

            // Check if the update was successful
            if (rowsAffected > 0 && message == "signUpMessage") {
                System.out.println("signUpMessage column updated successfully for ID: " + id);
            } else if (rowsAffected > 0 && message == "towedMessage") {
                System.out.println("towedMessage column updated successfully for ID: " + id);
            }else {
                System.out.println("No rows were updated.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    }



