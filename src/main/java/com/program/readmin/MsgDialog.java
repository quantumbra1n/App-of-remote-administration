package com.program.readmin;

import javafx.scene.control.Alert;

// Абстрактный класс диалоговых окон
abstract class AbstractMsgDialog {
    protected Alert alert; // Тип диалогового окна
    protected String title; // Название диалогового окна
    protected String contentText; // Текстовое сообщение

    // Конструктор абстрактного класса
    public AbstractMsgDialog(Alert alert, String title, String contentText) {
        this.alert = alert;
        this.title = title;
        this.contentText = contentText;
    }

    public abstract void show();
}

public class MsgDialog extends AbstractMsgDialog{
    public MsgDialog(Alert alert, String title, String contentText) {
        super(alert, title, contentText);
    }

    @Override
    public void show() {
        this.alert.setTitle(this.title);
        this.alert.setHeaderText(null);
        this.alert.setContentText(this.contentText);
        this.alert.showAndWait();
    }
}

// Класс диалога типа "Ошибка"
class ErrorDialog extends MsgDialog {
    public ErrorDialog(String contentText) {
        super(new Alert(Alert.AlertType.ERROR), "Ошибка", contentText);
        this.alert = new Alert(Alert.AlertType.ERROR);
    }
}

// Класс диалога типа "Предупреждение"
class WarningDialog extends MsgDialog {

    public WarningDialog(String contentText) {
        super(new Alert(Alert.AlertType.WARNING), "Предупреждение", contentText);
    }
}
// Класс диалога типа "Информация"
class InfoDialog extends MsgDialog {

    public InfoDialog(String contentText) {
        super(new Alert(Alert.AlertType.INFORMATION), "О программе", contentText);
    }
}