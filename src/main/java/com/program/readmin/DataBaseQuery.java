package com.program.readmin;

public class DataBaseQuery {
    private String queryTruncateTable = "TRUNCATE TABLE users;";
    private String querySelectAllUsers = "SELECT * FROM users;";
    private  String queryInsertIntoUser = "INSERT INTO users (id, email, login, password, role) VALUES (";
}
