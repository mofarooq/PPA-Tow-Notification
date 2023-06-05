package org.example;
import java.sql.*;

public class SqlNinja {
    public ResultSet userSet()  {

        // SQL query to execute
        String query = "SELECT * FROM UserbaseWithPhone";

        ResultSet resultSetFinal = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

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
}


