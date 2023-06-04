package com.program.readmin;

import java.sql.Connection;
import java.sql.Statement;

public class DataBaseConnector {
    public static final String USER_NAME = "root";
    public static final String PASSWORD = "1234";
    public static final String URL = "jdbc:mysql://localhost:3306/remadmin_db";
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver"; // Коннектор подключен как библиотека в project structure -> sdk -> java 18
    public static Statement statement;
    public static Connection connection;
}
