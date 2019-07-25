package com.packagename.myapp.spring;

import java.sql.*;

public class DataBaseConnection {
    Connection connection = null;

    boolean loadDriver() {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Файл ./libs/hsqldb.jar не подключен.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    boolean getConnection() {

        try {
            String path = "database/";
            String dbname = "mydb";
            String connectionString = "jdbc:hsqldb:file:"+path+dbname;
            String login = "admin";
            String password = "admin";
            connection = DriverManager.getConnection(connectionString, login, password);

        } catch (SQLException e) {
            System.out.println("Соединение не создано");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    void closeConnection() {

        Statement statement;
        try {
            statement = connection.createStatement();
            String sql = "SHUTDOWN";
            statement.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
