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
}