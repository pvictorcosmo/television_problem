package org.example.television_problem.view;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import org.example.television_problem.model.Guest;
import org.example.television_problem.model.GuestStatus;
import org.example.television_problem.service.ControlTvService;
import org.example.television_problem.view_model.MainViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class MainView implements FxmlView<MainViewModel> {

    @FXML
    private TextField channelInput;

    @FXML
    private void handleInitButton() {
        try {
            // Captura o número de canais (opcional: valide se é um número válido)
            String channels = channelInput.getText();
            System.out.println("Número de canais: " + channels);

            // Carrega a próxima tela (MainView.fxml)
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/org/example/television_problem/view/lobby/LobbyView.fxml"));
            Parent mainView = loader.load();

            // Obtem a cena atual e substitui
            Stage stage = (Stage) channelInput.getScene().getWindow();
            stage.setScene(new Scene(mainView));
            stage.show();

        } catch (Exception e) {  
            e.printStackTrace();
        }
    }
}

// Integer lastId = 0;
// @FXML
// private TextField durationField;

// @FXML
// private Button startButton;

// @FXML
// private Label statusLabel;
// @FXML
// private Button openFormButton; // Botão para abrir o formulário
// @FXML
// private Rectangle square;
// @FXML
// private Label squareText;

// @FXML
// private VBox squaresContainer; // O contêiner onde os quadrados serão
// exibidos

// @InjectViewModel
// private MainViewModel viewModel;

// // Método para abrir o popup com o formulário
// private void openFormPopup() {
// // Cria um novo stage (janela) para o popup
// Stage popupStage = new Stage();
// popupStage.setTitle("Formulário de Informações");

// // Campos do formulário
// TextField nameField = new TextField();
// nameField.setPromptText("Digite seu nome (opcional)");

// ComboBox<Integer> channelComboBox = new ComboBox<>();
// for (int i = 1; i <= 100; i++) {
// channelComboBox.getItems().add(i); // Adiciona canais de 1 a 100
// }
// channelComboBox.setPromptText("Escolha o canal");

// TextField ttvField = new TextField();
// ttvField.setPromptText("Digite o tempo (Ttv) assistindo TV (segundos)");

// TextField tdField = new TextField();
// tdField.setPromptText("Digite o tempo (Td) descansando (segundos)");

// Button submitButton = new Button("Enviar");
// submitButton.setOnAction(_ -> {
// String name = nameField.getText();
// Integer channel = channelComboBox.getValue();
// String ttv = ttvField.getText();
// String td = tdField.getText();

// // Validação dos campos obrigatórios (Canal, Ttv, Td)
// if (channel == null || ttv.isEmpty() || td.isEmpty()) {
// showAlert("Erro", "Canal, Tempo Assistindo TV (Ttv) e Tempo Descansando (Td)
// são obrigatórios.");
// return;
// }

// try {
// // Convertendo os campos Ttv e Td para inteiros
// int ttvTime = Integer.parseInt(ttv);
// int tdTime = Integer.parseInt(td);

// // Aqui você pode usar os dados conforme necessário
// System.out.println("Nome: " + (name.isEmpty() ? "Não informado" : name));
// System.out.println("Canal: " + channel);
// System.out.println("Tempo Assistindo TV (Ttv): " + ttvTime + " segundos");
// System.out.println("Tempo Descansando (Td): " + tdTime + " segundos");
// int id = lastId;
// Guest guest = new Guest(id, channel, ttvTime, tdTime, viewModel,
// GuestStatus.BLOCKED);
// lastId++;
// guest.start();
// // Fechar o popup

// popupStage.close();

// } catch (NumberFormatException e) {
// showAlert("Erro", "O tempo (Ttv, Td) deve ser um número inteiro válido.");
// }
// });

// // Layout do popup
// VBox vbox = new VBox(10,
// new Label("Nome (opcional):"), nameField,
// new Label("Canal (1 a N):"), channelComboBox,
// new Label("Tempo assistindo TV (Ttv) [segundos]:"), ttvField,
// new Label("Tempo descansando (Td) [segundos]:"), tdField,
// submitButton);
// vbox.setPadding(new Insets(20));

// // Configura a cena do popup
// Scene popupScene = new Scene(vbox, 800, 600);
// popupStage.setScene(popupScene);
// popupStage.show();
// }

// // Método para exibir alertas
// private void showAlert(String title, String message) {
// Alert alert = new Alert(AlertType.ERROR);
// alert.setTitle(title);
// alert.setHeaderText(null);
// alert.setContentText(message);
// alert.showAndWait();
// }

// @FXML
// public void onOpenFormButtonPressed() {
// openFormPopup();
// }

// public void setViewModel(MainViewModel viewModel) {
// this.viewModel = viewModel;

// // Atualiza os quadrados na interface quando a lista de quadrados muda
// viewModel.squaresProperty().addListener((observable, oldValue, newValue) -> {
// // Limpa os quadrados anteriores
// System.out.println("CHEGOU AQUI PAULO");
// squaresContainer.getChildren().clear();

// // Adiciona os quadrados da nova lista
// squaresContainer.getChildren().addAll(newValue);
// });
// }