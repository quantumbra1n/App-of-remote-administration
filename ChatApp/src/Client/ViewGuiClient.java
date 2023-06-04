package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Set;

public class ViewGuiClient {
    private final Client client;
    private JFrame frame = new JFrame("Чат");
    private JTextArea messages = new JTextArea(30, 20);
    private JTextArea users = new JTextArea(30, 15);
    private JPanel panel = new JPanel();
    private JTextField textField = new JTextField(40);
    private JButton buttonDisable = new JButton("Отключиться");
    private JButton buttonConnect = new JButton("Подключиться");

    public ViewGuiClient(Client client) {
        this.client = client;
    }

    // Инициализирует графический интерфейс клиентского приложения
    protected void initFrameClient() {
        messages.setEditable(false);
        users.setEditable(false);
        frame.add(new JScrollPane(messages), BorderLayout.CENTER);
        frame.add(new JScrollPane(users), BorderLayout.EAST);
        panel.add(textField);
        panel.add(buttonConnect);
        panel.add(buttonDisable);
        frame.add(panel, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null); // При запуске отображает окно по центру экрана
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Класс обработки события при закрытии окна приложения Сервера
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (client.isConnect()) {
                    client.disableClient();
                }
                System.exit(0);
            }
        });
        frame.setVisible(true);
        buttonDisable.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.disableClient();
            }
        });
        buttonConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.connectToServer();
            }
        });
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.sendMessageOnServer(textField.getText());
                textField.setText("");
            }
        });
    }

    protected void addMessage(String text) {
        messages.append(text);
    }

    // Обновляет список имен подлючившихся пользователей
    protected void refreshListUsers(Set<String> listUsers) {
        users.setText("");
        if (client.isConnect()) {
            StringBuilder text = new StringBuilder("Список пользователей:\n");
            for (String user : listUsers) {
                text.append(user + "\n");
            }
            users.append(text.toString());
        }
    }

    // Вызывает окно для ввода адреса сервера
    protected String getServerAddressFromOptionPane() {
        while (true) {
            String addressServer = JOptionPane.showInputDialog(
                    frame, "Введите адрес сервера:",
                    "Ввод адреса сервера",
                    JOptionPane.QUESTION_MESSAGE
            );
            return addressServer.trim();
        }
    }

    // Вызывает окно для ввода порта сервера
    protected int getPortServerFromOptionPane() {
        while (true) {
            String port = JOptionPane.showInputDialog(
                    frame, "Введите порт сервера:",
                    "Ввод порта сервера",
                    JOptionPane.QUESTION_MESSAGE
            );
            try {
                return Integer.parseInt(port.trim());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        frame, "Введен неккоректный порт сервера. Попробуйте еще раз.",
                        "Ошибка ввода порта сервера", JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    // Вызывает окно для ввода имени пользователя
    protected String getNameUser() {
        return JOptionPane.showInputDialog(
                frame, "Введите имя пользователя:",
                "Ввод имени пользователя",
                JOptionPane.QUESTION_MESSAGE
        );
    }

    // Вызывает окно ошибки с заданным текстом
    protected void errorDialogWindow(String text) {
        JOptionPane.showMessageDialog(
                frame, text,
                "Ошибка", JOptionPane.ERROR_MESSAGE
        );
    }
}
