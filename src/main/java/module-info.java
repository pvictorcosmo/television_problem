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
}
