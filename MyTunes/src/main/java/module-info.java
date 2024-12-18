module dk.igor.mytunes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.jdi;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;
    requires java.naming;
    requires java.compiler;
    requires javafx.media;


    opens dk.igor.mytunes.gui.controllers to javafx.fxml;


    exports dk.igor.mytunes;
    exports dk.igor.mytunes.gui.controllers;
    exports dk.igor.mytunes.be;

}



