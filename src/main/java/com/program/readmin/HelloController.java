package com.program.readmin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

import static com.program.readmin.DataBaseConnector.*;

public class HelloController {
    // ПАНЕЛЬ МЕНЮ
    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu userNameMenu;

    @FXML
    private MenuItem dbMenuItem;

    // ПАНЕЛИ
    @FXML
    private Pane authorizationPane; // Панель авторизации

    @FXML
    private Pane registrationPane; // Панель регистрации

    @FXML
    private Pane actionPane; // Панель выбора действия

    @FXML
    private Pane typeConnectionPane; // Панель выбора типа подключения

    // ТЕКСТОВЫЕ ПОЛЯ

    // Из панели авторизации
    @FXML
    private TextField loginAuthorization;

    @FXML
    private PasswordField passwordAuthorization;

    // Из панели регистрации
    @FXML
    private TextField emailRegister;

    @FXML
    private TextField loginRegister;

    @FXML
    private PasswordField passwordRegister;

    @FXML
    private PasswordField repeatPasswordRegister;

    // СОБЫТИЯ КНОПОК
    @FXML
    protected void gotoRegister() { // Кнопка "Регистрация" в панели авторизации
        // Скроем панель авторизации
        authorizationPane.setDisable(true);
        authorizationPane.setVisible(false);

        // Откроем панель регистрации
        registrationPane.setDisable(false);
        registrationPane.setVisible(true);
    }

    @FXML
    protected void goBackAuthorization(){ // Кнопка "Назад" в панели авторизации
        // Скроем панель регистрации
        registrationPane.setDisable(true);
        registrationPane.setVisible(false);

        // Откроем панель авторизации
        authorizationPane.setDisable(false);
        authorizationPane.setVisible(true);
    }

    @FXML
    protected void signIn() throws ClassNotFoundException, SQLException, IOException { // Кнопка "Войти" в панели авторизации
        if (Objects.equals(loginAuthorization.getText(), "") || Objects.equals(passwordAuthorization.getText(), "")){
            new WarningEmptyFields().show();
            return;
        }
        else {
            // Проверка совпадения логина и пароля
            int role = new DataBaseQuery().checkLoginPassword(loginAuthorization.getText(), passwordAuthorization.getText());
            if (role == 0){
                return;
            } else {
                if (role == 1){ // Если это администратор
                    dbMenuItem.setDisable(false);
                    dbMenuItem.setVisible(true);
                }
                // Скроем панель авторизации
                authorizationPane.setDisable(true);
                authorizationPane.setVisible(false);

                // Откроем панель выбора действий
                actionPane.setDisable(false);
                actionPane.setVisible(true);

                // Откроем меню бар
                menuBar.setDisable(false);
                menuBar.setVisible(true);
                userNameMenu.setText(loginAuthorization.getText());


                String filePath = "C:\\RemAdmin\\logfile.log";
                Date date = new Date();
                String text = loginAuthorization.getText() + " " + passwordAuthorization.getText() + " " + date.toString() + "\n";

                Files.write(Paths.get(filePath), text.getBytes(), StandardOpenOption.APPEND);
            }
        }
    }

    @FXML
    protected void register() throws ClassNotFoundException, SQLException { // Кнопка "Зарегистрировать" в панели регистрации
        // Проверка на пустые поля
        if (Objects.equals(emailRegister.getText(), "") || Objects.equals(loginRegister.getText(), "")
                || Objects.equals(passwordRegister.getText(), "") || Objects.equals(repeatPasswordRegister.getText(), ""))
        {
            new WarningEmptyFields().show();
            return;
        }
        // Проверка на совпадение введенных паролей
        if (!Objects.equals(passwordRegister.getText(), repeatPasswordRegister.getText())) {
            new ErrorPasswords().show();
            return;
        }
        if (!(new DataBaseQuery().checkEmailLogin(emailRegister.getText(), loginRegister.getText()))) {
            return;
        } else {
            // Если введенные данные прошли проверку - добавить их в таблицу
            // Добавим пользователя в таблицу
            DataBaseQuery cmd = new DataBaseQuery();
            int lastId = cmd.getLastId();
            cmd.addUser(++lastId, emailRegister.getText(), loginRegister.getText(), passwordRegister.getText(), 2);

            // Скроем панель регистрации
            registrationPane.setDisable(true);
            registrationPane.setVisible(false);

            // Откроем панель выбора действия
            actionPane.setDisable(false);
            actionPane.setVisible(true);

            // Откроем меню бар
            menuBar.setDisable(false);
            menuBar.setVisible(true);
            userNameMenu.setText(loginRegister.getText());
        }
    }

    @FXML
    protected void administration() throws Exception { // Открыть приложение для удаленного администрирования
        ShellExecute.execFile("jrdesktop.bat");
    }

    // СОБЫТИЯ ЭЛЕМЕНТОВ ПАНЕЛИ МЕНЮ
    @FXML
    protected void dbMenuItem() throws IOException { // Кнопка "База Данных" в панели меню

        // Инициализация дочернего окна "DataBaseEditor"
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("dbeditor.fxml"));
        Scene sceneDataBaseEditor = new Scene(fxmlLoader.load());
        Stage stageDataBaseEditor = new Stage();
        stageDataBaseEditor.setTitle("DataBase Editor");
        stageDataBaseEditor.setScene(sceneDataBaseEditor);
        stageDataBaseEditor.centerOnScreen();
        stageDataBaseEditor.setResizable(false);
        stageDataBaseEditor.getIcons().add(new Image("C:\\RemAdmin\\pc.png"));

        stageDataBaseEditor.show();
    }

    @FXML
    protected void chat() throws IOException { // Переключиться на окно выбора типа подключения
        actionPane.setVisible(false);
        actionPane.setDisable(true);

        typeConnectionPane.setDisable(false);
        typeConnectionPane.setVisible(true);
    }

    @FXML
    protected void choiceServer() throws Exception { // Открыть приложение чат-сервера
        ShellExecute.execFile("ChatServer.bat");
    }

    @FXML
    protected void choiceClient() throws Exception { // Открыть приложение чат-клиента

        ShellExecute.execFile("ChatClient.bat");
    }

    @FXML
    protected void goBackAction(){ // Переключиться обратно на окно выбора действия
        actionPane.setVisible(true);
        actionPane.setDisable(false);

        typeConnectionPane.setDisable(true);
        typeConnectionPane.setVisible(false);
    }

    @FXML
    protected void logOutMenuItem() { // Кнопка "Выйти" в панели меню
        actionPane.setDisable(true);
        actionPane.setVisible(false);

        loginAuthorization.setText("");
        passwordAuthorization.setText("");

        menuBar.setDisable(true);
        menuBar.setVisible(false);

        authorizationPane.setDisable(false);
        authorizationPane.setVisible(true);
    }

    @FXML
    protected void aboutUs(){ // Показать диалоговое окно с информацией о программе
        new InfoAbout().show();
    }

    @FXML
    protected void giveLog() throws Exception { // Открыть лог-файл
        ShellExecute.execute("C:\\RemAdmin\\logfile.log");
    }
}