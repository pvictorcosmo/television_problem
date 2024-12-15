module org.example.television_problem {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.saxsys.mvvmfx;
    requires javafx.graphics;
    requires java.logging;

    exports org.example.television_problem;  // Exporta o pacote para javafx.graphics
    exports org.example.television_problem.view;
    exports org.example.television_problem.view_model;
    exports org.example.television_problem.model;
    exports org.example.television_problem.service;

    opens org.example.television_problem.view to javafx.fxml;
    opens org.example.television_problem.view.waiting_room to javafx.fxml;
    opens org.example.television_problem.view.watching_room to javafx.fxml;
    opens org.example.television_problem.view.event_logger to javafx.fxml;
    opens org.example.television_problem.view.sleeping_room to javafx.fxml;
    opens org.example.television_problem.view.lobby to javafx.fxml;
    opens org.example.television_problem.view.guest to javafx.fxml;

}
