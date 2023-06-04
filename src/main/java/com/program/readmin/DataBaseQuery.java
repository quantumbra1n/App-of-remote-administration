package com.program.readmin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import static com.program.readmin.DataBaseConnector.connection;
import static com.program.readmin.DataBaseConnector.statement;

public class DataBaseQuery {
    private String queryTruncateTable = "TRUNCATE TABLE users;";
    private String querySelectAllUsers = "SELECT * FROM users;";
    private  String queryInsertIntoUser = "INSERT INTO users (id, email, login, password, role) VALUES (";

    public int checkLoginPassword(String login, String password) throws SQLException { // Проверка логина и почты, возвращает role
        ResultSet result = statement.executeQuery(this.querySelectAllUsers); // Запрос
        while (result.next()) {

            // Проверка на совпадение логина и пароля
            if (Objects.equals(login, result.getString(3))
                    && Objects.equals(password, result.getString(4)))
            {
                return result.getInt(5); // Возврат роли
            }
        }
        new ErrorAuthorization().show();
        return 0;
    }

    public int getLastId() throws SQLException {
        ResultSet result = statement.executeQuery(this.querySelectAllUsers); // Запрос
        int lastId = 0; // Для определения последнего id в таблице
        while (result.next()) {
            lastId = result.getInt(1);
        }
        return lastId;
    }

    public void addUser(User user) throws SQLException {
        String query = this.queryInsertIntoUser + user.getId() + ", '" + user.getEmail() + "', '"
                + user.getLogin() + "', '" + user.getPassword() + "', '" + user.getRole() + "');";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.executeUpdate(); // Выполним запрос
    }

    public void addUser(int id, String email, String login, String password, int role) throws SQLException {
        String query = this.queryInsertIntoUser + id + ", '" + email + "', '"
                + login + "', '" + password + "', '" + role + "');";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.executeUpdate(); // Выполним запрос
    }

    public boolean checkEmailLogin(String email, String login) throws SQLException { // Проверка логина и почты, возвращает последний id
        ResultSet resultSet = statement.executeQuery(this.querySelectAllUsers); // Запрос
        while (resultSet.next()) {

            // Проверка на существование почты и логина
            if (Objects.equals(email, resultSet.getString(2))
                    || Objects.equals(login, resultSet.getString(3))) {
                new ErrorEmailLogin().show();
                return false;
            }
        }
        return true;
    }

    public void truncateTable() throws ClassNotFoundException, SQLException {
        // Очистим таблицу
        Statement statement = connection.createStatement();
        statement.executeUpdate(this.queryTruncateTable); // Выполним запрос
    }
}
