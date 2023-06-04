module com.program.readmin {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires java.datatransfer;


    opens com.program.readmin to javafx.fxml;
    exports com.program.readmin;
    exports com.program.readmin.chatt;
    opens com.program.readmin.chatt to javafx.fxml;
}