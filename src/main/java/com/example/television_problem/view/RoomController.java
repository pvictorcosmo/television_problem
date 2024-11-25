package com.example.television_problem.view;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

/**
 *
 * @author celso
 */
public class RoomController implements Initializable {

    @FXML
    private Button button1;

    @FXML
    private TextField Quant_hosp;

    @FXML
    private TextField Quant_canais;

    @FXML
    private TextField Assistir_min;

    @FXML
    private TextField Assistir_max;

    @FXML
    private TextField Descansar_min;

    @FXML
    private TextField Descansar_max;


    private final String AUDIO = getClass().getResource("MusicAbertura.mp3").toString();
    AudioClip clip = new AudioClip(AUDIO);

    @FXML
    void handleButtonAction(ActionEvent event) throws IOException {
        if (Integer.parseInt(Assistir_min.getText()) > Integer.parseInt(Assistir_max.getText())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERRO");
            alert.setHeaderText(null);
            alert.setContentText("Tempo de assistir minimo tem que ser menor que o tempo maximo.");
            alert.showAndWait();
        } else if (Integer.parseInt(Descansar_min.getText()) > Integer.parseInt(Descansar_max.getText())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERRO");
            alert.setHeaderText(null);
            alert.setContentText("Tempo de descanso minimo tem que ser menor que o tempo maximo.");
            alert.showAndWait();
        }
        else {
            clip.stop();
            Hotel hotel = new Hotel();

            hotel.setQtdHospedes(Integer.parseInt(Quant_hosp.getText()));
            hotel.setQtdCanais(Integer.parseInt(Quant_canais.getText()));
            hotel.setMinTV(Integer.parseInt(Assistir_min.getText()));
            hotel.setMaxTV(Integer.parseInt(Assistir_max.getText()));
            hotel.setMinDesc(Integer.parseInt(Descansar_min.getText()));
            hotel.setMaxDesc(Integer.parseInt(Descansar_max.getText()));
            hotel.setAtributosTempo();
            hotel.setTemposThreads();


            FXMLLoader sala = new FXMLLoader(getClass().getResource("SalaTV.fxml"));
            // Definindo quem é o controller desse 'fxml':
            sala.setController(new SalaTVController(hotel));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(sala.load()));
            stage.show();

        }



    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clip.play();
        // TODO
    }

}
