package Connection;

import java.io.*;
import java.net.Socket;

public class Connection implements Closeable {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;


    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    // Отправка сообщения по сокетному соединению
    public void send(Message message) throws IOException {
        synchronized (this.out) {
            out.writeObject(message);
        }
    }

    // Принятие сообщения по сокетному соединению
    public Message receive() throws IOException, ClassNotFoundException {
        synchronized (this.in) {
            Message message = (Message) in.readObject();
            return message;
        }
    }

    // Закрытие потоков чтения, записи и сокета
    @Override
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
