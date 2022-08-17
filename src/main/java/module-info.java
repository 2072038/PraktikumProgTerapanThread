module com.pterapan.demosql {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;

    opens com.pterapan.demosql to javafx.fxml;
    exports com.pterapan.demosql;
    exports com.pterapan.demosql.util;
    exports com.pterapan.demosql.model;
    exports com.pterapan.demosql.controller;
}