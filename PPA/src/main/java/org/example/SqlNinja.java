package org.example;
import java.sql.*;

public class SqlNinja {

    public final String jdbcUrl = "com.mysql.cj.jdbc.Driver";
    public ResultSet userSet()  {
        // SQL query to execute
        String query = "SELECT * FROM UserbaseWithPhone";

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

    public void updateFirstEmail(int id) {

        // SQL query to update the column
        String sql = "UPDATE UserbaseWithPhone SET FirstEmail = ? WHERE id = ?";

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
            if (rowsAffected > 0) {
                System.out.println("FirstEmail column updated successfully for ID: " + id);
            } else {
                System.out.println("No rows were updated.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    }



