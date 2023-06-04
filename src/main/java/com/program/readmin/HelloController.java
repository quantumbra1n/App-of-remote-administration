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
}