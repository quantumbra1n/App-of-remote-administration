package com.program.readmin;

import javafx.scene.control.Alert;

public class MsgDialog {
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
}
