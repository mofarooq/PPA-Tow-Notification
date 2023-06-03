package org.example;
import java.sql.*;

public class SqlNinja {


    public ResultSet userSet()  {
        // AWS RDS database connection parameters
        String url = "jdbc:mysql://ppa-database-1.czkjwcsnqzk1.us-east-2.rds.amazonaws.com/PPA";
        String username = "admin";
        String password = "Mmfarooq1!";

        // SQL query to execute
        String query = "SELECT * FROM Userbase";

        ResultSet resultSetFinal = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the database connection
            Connection connection = DriverManager.getConnection(url, username, password);

            // Create a statement object to execute the query
            Statement statement = connection.createStatement();

            // Execute the query and obtain the result set
            ResultSet resultSet = statement.executeQuery(query);

            resultSetFinal = resultSet;

            // Process the result set
//            while (resultSet.next()) {
//                // Retrieve data from the result set
//                int id = resultSet.getInt("id");
//                String name = resultSet.getString("name");
//                String email = resultSet.getString("email");
//                String license = resultSet.getString("license");
//                String firstEmail = resultSet.getString("firstEmail");
//
//                // ... Retrieve other columns as needed
//
//                // Do something with the retrieved data
//                System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email+ ", License: " + license+ ", FirstEmailSent: " + firstEmail);
//                System.out.println(resultSet);
//
//            }

            // Close the result set, statement, and connection
//            resultSet.close();
//            statement.close();
//            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();

        }

        return resultSetFinal;
    }
}


        // Database connection parameters
