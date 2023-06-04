package Connection;

import java.io.Serializable;
import java.util.Set;

public class Message implements Serializable {
    private MessageType typeMessage; // Тип сообщения
    private String textMessage; // Текст сообщения
    private Set<String> listUsers; // Множество имен уже подлючившихся пользователей

    // Конструкторы
    // Принимает тип сообщения и текст
    public Message(MessageType typeMessage, String textMessage) {
        this.textMessage = textMessage;
        this.typeMessage = typeMessage;
        this.listUsers = null;
    }

    // Принимает тип сообщения и список подключившихся пользователей
    public Message(MessageType typeMessage, Set<String> listUsers) {
        this.typeMessage = typeMessage;
        this.textMessage = null;
        this.listUsers = listUsers;
    }

    // Принимает тип сообщения
    public Message(MessageType typeMessage) {
        this.typeMessage = typeMessage;
        this.textMessage = null;
        this.listUsers = null;
    }

    // Геттеры
    public MessageType getTypeMessage() {
        return typeMessage;
    }

    public Set<String> getListUsers() {
        return listUsers;
    }

    public String getTextMessage() {
        return textMessage;
    }

}
