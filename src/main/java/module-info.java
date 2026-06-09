module lk.ijse.realtimeauctionsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens lk.ijse.realtimeauctionsystem to javafx.fxml;
    exports lk.ijse.realtimeauctionsystem;
}