module com.tsmteam.erubz214javasmarttraffic {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tsmteam.erubz214javasmarttraffic to javafx.fxml;
    exports com.tsmteam.erubz214javasmarttraffic;
}