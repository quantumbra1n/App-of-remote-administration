package Server;

import Connection.Connection;

import java.util.HashMap;
import java.util.Map;

public class ModelGuiServer {
    // Модель хранит карту со всеми подключившимися клиентами (ключ - имя клиента, значение - объект connecton)
    private Map<String, Connection> allUsersChat = new HashMap<>();


    protected Map<String, Connection> getAllUsersChat() {
        return allUsersChat;
    }

    protected void addUser(String nameUser, Connection connection) {
        allUsersChat.put(nameUser, connection);
    }

    protected void removeUser(String nameUser) {
        allUsersChat.remove(nameUser);
    }

}