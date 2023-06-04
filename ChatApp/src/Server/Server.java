package Server;

import Connection.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


public class Server {
    private ServerSocket serverSocket;
    private static ViewGuiServer gui; // Объект класса представления
    private static ModelGuiServer model; // Объект класса модели
    private static volatile boolean isServerStart = false; // Состояние сервера: запущен/остановлен

    // Запуск сервера
    protected void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            isServerStart = true;
            gui.refreshDialogWindowServer("Сервер запущен.\n");
        } catch (Exception e) {
            gui.refreshDialogWindowServer("Не удалось запустить сервер.\n");
        }
    }

    // Остановка сервера
    protected void stopServer() {
        try {
            // Если серверный Сокет не имеет ссылки или не запущен
            if (serverSocket != null && !serverSocket.isClosed()) {
                for (Map.Entry<String, Connection> user : model.getAllUsersChat().entrySet()) {
                    user.getValue().close();
                }
                serverSocket.close();
                model.getAllUsersChat().clear();
                gui.refreshDialogWindowServer("Сервер остановлен.\n");
            } else gui.refreshDialogWindowServer("Сервер не запущен - останавливать нечего!\n");
        } catch (Exception e) {
            gui.refreshDialogWindowServer("Остановить сервер не удалось.\n");
        }
    }

    // Метод, в котором в бесконечном цикле сервер принимает новое сокетное подключение от клиента
    protected void acceptServer() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new ServerThread(socket).start();
            } catch (Exception e) {
                gui.refreshDialogWindowServer("Связь с сервером потеряна.\n");
                break;
            }
        }
    }

    // Рассылает заданное сообщение всем клиентам из карты
    protected void sendMessageAllUsers(Message message) {
        for (Map.Entry<String, Connection> user : model.getAllUsersChat().entrySet()) {
            try {
                user.getValue().send(message);
            } catch (Exception e) {
                gui.refreshDialogWindowServer("Ошибка отправки сообщения всем пользователям!\n");
            }
        }
    }

    protected void sendMessageAllUsers(Message myMessage, Message allMessage,  String nameUser) {
        for (Map.Entry<String, Connection> user : model.getAllUsersChat().entrySet()) {
            if (Objects.equals(user.getKey(), nameUser)) { // Отправить мне
                try {
                    user.getValue().send(myMessage);
                } catch (Exception e) {
                    gui.refreshDialogWindowServer("Ошибка отправки сообщения всем пользователям!\n");
                }
            } else { // Отправить всем
                try {
                    user.getValue().send(allMessage);
                } catch (Exception e) {
                    gui.refreshDialogWindowServer("Ошибка отправки сообщения всем пользователям!\n");
                }
            }
        }
    }

    // Точка входа для приложения сервера
    public static void main(String[] args) {
        Server server = new Server();
        gui = new ViewGuiServer(server);
        model = new ModelGuiServer();
        gui.initFrameServer();
        /*
         Цикл снизу ждет true от флага isServerStart (при старте сервера в методе startServer устанавливается в true)
         после чего запускается бесконечный цикл принятия подключения от клиента в  методе acceptServer
         до тех пор пока сервер не остановится, либо не возникнет исключение
         */
        while (true) {
            if (isServerStart) {
                server.acceptServer();
                isServerStart = false;
            }
        }
    }

    // Класс-поток, который запускается при принятии сервером нового сокетного соединения с клиентом, в конструктор
    // Передается объект класса Socket
    private class ServerThread extends Thread {
        private Socket socket;

        public ServerThread(Socket socket) {
            this.socket = socket;
        }

        // Метод, реализующий запрос сервера у клиента имени и добавлении имени в карту
        private String requestAndAddingUser(Connection connection) {
            while (true) {
                try {
                    // Посылаем клиенту сообщение-запрос имени
                    connection.send(new Message(MessageType.REQUEST_NAME_USER));
                    Message responseMessage = connection.receive();
                    String userName = responseMessage.getTextMessage();

                    // Получили ответ с именем и проверяем не занято ли это имя другим клиентом
                    if (responseMessage.getTypeMessage() == MessageType.USER_NAME && userName != null && !userName.isEmpty() && !model.getAllUsersChat().containsKey(userName)) {

                        // Добавляем имя в карту
                        model.addUser(userName, connection);
                        Set<String> listUsers = new HashSet<>();
                        for (Map.Entry<String, Connection> users : model.getAllUsersChat().entrySet()) {
                            listUsers.add(users.getKey());
                        }

                        // Отправляем клиенту множетство имен всех уже подключившихся пользователей
                        connection.send(new Message(MessageType.NAME_ACCEPTED, listUsers));

                        // Отправляем всем клиентам сообщение о новом пользователе
                        Message messageMe = new Message(MessageType.TEXT_MESSAGE, userName);
                        String textMessageMe = String.format(" ==========================ДОБРО ПОЖАЛОВАТЬ В ЧАТ=========================\n", messageMe.getTextMessage());
                        sendMessageAllUsers(new Message(MessageType.ME_ADDED, textMessageMe), new Message(MessageType.USER_ADDED, userName), userName);
                        return userName;
                    }

                    // Если такое имя уже занято отправляем сообщение клиенту, что имя используется
                    else connection.send(new Message(MessageType.NAME_USED));
                } catch (Exception e) {
                    gui.refreshDialogWindowServer("Возникла ошибка при запросе и добавлении нового пользователя\n");
                }
            }
        }

        // Реализация обмена сообщениями между пользователями
        private void messagingBetweenUsers(Connection connection, String userName) {
            while (true) {
                try {
                    Message message = connection.receive();

                    if (message.getTypeMessage() == MessageType.TEXT_MESSAGE) {
                        String textMessageMe = String.format("Вы: %s\n", message.getTextMessage());
                        String textMessageAll = String.format("%s: %s\n", userName, message.getTextMessage());
                        sendMessageAllUsers(new Message(MessageType.TEXT_MESSAGE, textMessageMe), new Message(MessageType.TEXT_MESSAGE, textMessageAll), userName);
                    }

                    // Если тип сообщения DISABLE_USER, то рассылаем всем пользователям, что данный пользователь покинул чат,
                    // удаляем его из мапы, закрываем его connection
                    if (message.getTypeMessage() == MessageType.DISABLE_USER) {
                        sendMessageAllUsers(new Message(MessageType.REMOVED_USER, userName));
                        model.removeUser(userName);
                        connection.close();
                        gui.refreshDialogWindowServer(String.format("Пользователь с удаленным доступом %s отключился.\n", socket.getRemoteSocketAddress()));
                        break;
                    }
                } catch (Exception e) {
                    gui.refreshDialogWindowServer(String.format("Произошла ошибка при рассылке сообщения от пользователя %s, либо отключился!\n", userName));
                    break;
                }
            }
        }

        @Override
        public void run() {
            gui.refreshDialogWindowServer(String.format("Подключился новый пользователь с удаленным сокетом - %s.\n", socket.getRemoteSocketAddress()));
            try {

                // Получаем connection при помощи принятого сокета от клиента и запрашиваем имя, регистрируем, запускаем
                // цикл обмена сообщениями между пользователями
                Connection connection = new Connection(socket);
                String nameUser = requestAndAddingUser(connection);
                messagingBetweenUsers(connection, nameUser);
            } catch (Exception e) {
                gui.refreshDialogWindowServer(String.format("Произошла ошибка при рассылке сообщения от пользователя!\n"));
            }
        }
    }
}