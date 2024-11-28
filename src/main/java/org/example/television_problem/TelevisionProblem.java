package org.example.television_problem;

import de.saxsys.mvvmfx.FluentViewLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import de.saxsys.mvvmfx.ViewTuple;

import org.example.television_problem.view.MainView;
import org.example.television_problem.view_model.MainViewModel;

public class TelevisionProblem extends Application {

    public static void main(String... args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Hello World Application");

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/org/example/television_problem/view/MainView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}