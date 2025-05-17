module com.tsmteam.erubz214javasmarttraffic {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.tsmteam.erubz214javasmarttraffic to javafx.fxml;
    opens com.tsmteam.erubz214javasmarttraffic.controller to javafx.fxml;
    exports com.tsmteam.erubz214javasmarttraffic.controller to javafx.fxml;
    exports com.tsmteam.erubz214javasmarttraffic;
}