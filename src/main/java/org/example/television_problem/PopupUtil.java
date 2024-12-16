package org.example.television_problem;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PopupUtil {

    public interface PopupCallback {
        void onFormSubmitted(String name, int channel, int ttv, int td);
    }

    public static void showFormPopup(PopupCallback callback) {
        // Cria um novo stage (janela) para o popup
        Stage popupStage = new Stage();
        popupStage.setTitle("Formulário de Informações");

        // Campos do formulário
        TextField nameField = new TextField();
        nameField.setPromptText("Digite seu nome (opcional)");

        ComboBox<Integer> channelComboBox = new ComboBox<>();
        for (int i = 1; i <= 100; i++) {
            channelComboBox.getItems().add(i); // Adiciona canais de 1 a 100
        }
        channelComboBox.setPromptText("Escolha o canal");

        TextField ttvField = new TextField();
        ttvField.setPromptText("Digite o tempo (Ttv) assistindo TV (segundos)");

        TextField tdField = new TextField();
        tdField.setPromptText("Digite o tempo (Td) descansando (segundos)");

        Button submitButton = new Button("Enviar");
        submitButton.setOnAction(_ -> {
            String name = nameField.getText();
            Integer channel = channelComboBox.getValue();
            String ttv = ttvField.getText();
            String td = tdField.getText();

            // Validação dos campos obrigatórios (Canal, Ttv, Td)
            if (channel == null || ttv.isEmpty() || td.isEmpty()) {
                showAlert("Erro", "Canal, Tempo Assistindo TV (Ttv) e Tempo Descansando (Td) são obrigatórios.");
                return;
            }

            try {
                // Convertendo os campos Ttv e Td para inteiros
                int ttvTime = Integer.parseInt(ttv);
                int tdTime = Integer.parseInt(td);

                // Chama o callback com os valores preenchidos
                callback.onFormSubmitted(name, channel, ttvTime, tdTime);

                // Fecha o popup
                popupStage.close();
            } catch (NumberFormatException e) {
                showAlert("Erro", "O tempo (Ttv, Td) deve ser um número inteiro válido.");
            }
        });

        // Layout do popup
        VBox vbox = new VBox(10,
                new Label("Nome (opcional):"), nameField,
                new Label("Canal (1 a N):"), channelComboBox,
                new Label("Tempo assistindo TV (Ttv) [segundos]:"), ttvField,
                new Label("Tempo descansando (Td) [segundos]:"), tdField,
                submitButton);
        vbox.setPadding(new Insets(20));

        // Configura a cena do popup
        Scene popupScene = new Scene(vbox, 400, 300);
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
