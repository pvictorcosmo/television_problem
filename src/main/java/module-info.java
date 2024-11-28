module org.example.television_problem {
    requires de.saxsys.mvvmfx;
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.television_problem to javafx.fxml;
    exports org.example.television_problem;  // Exporta o pacote para uso em outros módulos
    exports org.example.television_problem.view;  // Exporta a view para uso em outros módulos, caso necessário
    exports org.example.television_problem.view_model;  // Exporta o view model, caso necessário
}