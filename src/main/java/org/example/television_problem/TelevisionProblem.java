package org.example.television_problem;

import de.saxsys.mvvmfx.FluentViewLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import de.saxsys.mvvmfx.ViewTuple;

import org.example.television_problem.model.Guest;
import org.example.television_problem.model.GuestStatus;
import org.example.television_problem.service.ControlTvService;
import org.example.television_problem.view.MainView;
import org.example.television_problem.view_model.MainViewModel;

public class TelevisionProblem extends Application {

    public static void main(String... args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Caminho para o arquivo FXML
        String fxmlPath = "/org/example/television_problem/view/MainView.fxml";

        // Cria um FXMLLoader padrão
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

        // Carrega a view e o controlador
        Parent root = loader.load();

        // Obtém o ViewModel a partir do controlador e injeta-o no carregamento do FXML
        MainView mainView = loader.getController();
        MainViewModel viewModel = new MainViewModel(); // Criar o ViewModel
        mainView.setViewModel(viewModel); // Injeção manual do ViewModel

        // Configura a cena
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Problema da televisão");
        // Definindo o tamanho inicial da janela
        primaryStage.setWidth(1000); // Largura inicial
        primaryStage.setHeight(800); // Altura inicial
        primaryStage.show();

        ControlTvService controlTvService = new ControlTvService(viewModel);

        // Cria e inicia vários Guests
        Guest guest1 = new Guest(1, 5, 10, 5, controlTvService, GuestStatus.WAITING);
        Guest guest2 = new Guest(2, 10, 15, 7, controlTvService, GuestStatus.WAITING);
        Guest guest3 = new Guest(3, 11, 10, 5, controlTvService, GuestStatus.WAITING);
        Guest guest4 = new Guest(4, 5, 15, 7, controlTvService, GuestStatus.WAITING);

        // Inicia as threads dos Guests
        guest1.start();
        guest2.start();
        guest3.start();
        guest4.start();
    }

}