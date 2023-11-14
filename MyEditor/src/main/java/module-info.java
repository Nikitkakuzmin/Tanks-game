module kz.nik.myeditor {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swingEmpty;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens kz.nik.myeditor to javafx.fxml;
    exports kz.nik.myeditor;

}