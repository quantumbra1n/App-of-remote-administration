package com.program.readmin;

import com.mysql.cj.xdevapi.Warning;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.program.readmin.DataBaseConnector.DRIVER;
import static com.program.readmin.DataBaseConnector.statement;

public class DataBaseEditorController implements Initializable {
    @FXML
    private TextField emailTextField;

    @FXML
    private TextField loginTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField roleTextField;

    @FXML
    private Button saveEditButton;

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, Integer> idTableColumn;

    @FXML
    private TableColumn<User, String> emailTableColumn;

    @FXML
    private TableColumn<User, String> loginTableColumn;

    @FXML
    private TableColumn<User, String> passwordTableColumn;

    @FXML
    private TableColumn<User, Integer> roleTableColumn;

    private final ObservableList<User> usersData = FXCollections.observableArrayList();

    // Подготавливаем данные для таблицы
    private void initData() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER); // Убедимся в существовании JDBC-драйвера
        ResultSet resultSet = statement.executeQuery("SELECT * FROM users"); // Запрос
        while (resultSet.next()){ // Инициализация пользователей из БД
            usersData.add(new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5)));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initData(); // Инициализируем пользователей
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        // Устанавливаем тип и значение, которое должно хранится в колонке
        idTableColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        loginTableColumn.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        passwordTableColumn.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        roleTableColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("role"));

        // Заполняем таблицу пользователями
        tableView.setItems(usersData);

        // Редактирование данных
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        emailTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailTableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<User, String> userStringCellEditEvent) {
                User user = userStringCellEditEvent.getRowValue();
                int indexOfEditUser = usersData.indexOf(user); // Фиксируем индекс редактируемого пользователя
                user.setEmail(userStringCellEditEvent.getNewValue());
                usersData.set(indexOfEditUser, user); // Фиксируем измененные данные
                saveEditButton.setDisable(false); // Сделаем кнопку "Сохранить изменения" активной
            }
        });
        loginTableColumn.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        loginTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        loginTableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<User, String> userStringCellEditEvent) {
                User user = userStringCellEditEvent.getRowValue();
                int indexOfEditUser = usersData.indexOf(user); // Фиксируем индекс редактируемого пользователя
                user.setLogin(userStringCellEditEvent.getNewValue());
                usersData.set(indexOfEditUser, user); // Фиксируем измененные данные
                saveEditButton.setDisable(false); // Сделаем кнопку "Сохранить изменения" активной
            }
        });
        passwordTableColumn.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        passwordTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordTableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, String>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<User, String> userStringCellEditEvent) {
                User user = userStringCellEditEvent.getRowValue();
                int indexOfEditUser = usersData.indexOf(user); // Фиксируем индекс редактируемого пользователя
                user.setPassword(userStringCellEditEvent.getNewValue());
                usersData.set(indexOfEditUser, user); // Фиксируем измененные данные
                saveEditButton.setDisable(false); // Сделаем кнопку "Сохранить изменения" активной
            }
        });

        roleTableColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("role"));
        roleTableColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        roleTableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<User, Integer>>() {

            @Override
            public void handle(TableColumn.CellEditEvent<User, Integer> userIntegerCellEditEvent) {
                User user = userIntegerCellEditEvent.getRowValue();
                int indexOfEditUser = usersData.indexOf(user); // Фиксируем индекс редактируемого пользователя
                user.setRole(userIntegerCellEditEvent.getNewValue());
                usersData.set(indexOfEditUser, user); // Фиксируем измененные данные
                saveEditButton.setDisable(false); // Сделаем кнопку "Сохранить изменения" активной
            }
        });
    }
}
