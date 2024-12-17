package org.example.television_problem.view.lobby;

import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.example.television_problem.model.Guest;
import org.example.television_problem.service.ControlTvService;
import org.example.television_problem.view_model.lobby.LobbyViewModel;

import de.saxsys.mvvmfx.InjectViewModel;

public class LobbyView {

    private static final double BED_WIDTH = 80; // Largura da cama
    private static final double BED_HEIGHT = 100; // Altura da cama
    private static final double GUEST_WIDTH = 60; // Largura do Guest
    private static final double GUEST_HEIGHT = 80; // Altura do Guest

    private static final double START_X = 800; // Posição inicial X das camas
    private static final double START_Y = 200; // Posição inicial Y das camas
    private int bedCount = 0; // Contador para camas (usado para calcular a posição)

    @FXML
    private HBox bedContainer; // Container para as camas
    @FXML
    private Pane guestContainer;
    @FXML
    private HBox televisionContent;

    @InjectViewModel
    private LobbyViewModel viewModel;

    private void updateImage(String imagePath) {
        // Carrega a nova imagem
        System.out.println("path image:" + imagePath);
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        System.out.println("Change channel");
        // Cria o ImageView para exibir a imagem
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(televisionContent.getPrefWidth());
        imageView.setFitHeight(televisionContent.getPrefHeight());

        // Remove imagens antigas e adiciona a nova
        televisionContent.getChildren().clear();
        televisionContent.getChildren().add(imageView);
    }

    // Método de inicialização
    public void initialize() {
        this.viewModel = new LobbyViewModel();
        ObservableList<Guest> guests = viewModel.getGuests();
        int numBeds = ControlTvService.getInstance().channels;
        viewModel.currentImagePathProperty().addListener((obs, oldPath, newPath) -> {
            updateImage(newPath);
        });
        // Listener para adicionar ou remover sprites conforme a lista de guests mudar
        guests.addListener((ListChangeListener<Guest>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (Guest guest : change.getAddedSubList()) {
                        createBedAndGuest(guest); // Criar cama e Guest
                    }
                }
                if (change.wasRemoved()) {
                    for (Guest guest : change.getRemoved()) {
                        removeGuestAndBed(guest); // Remover cama e Guest
                    }
                }
            }
        });

        // Criar os sprites iniciais (se existirem guests)
        for (Guest guest : guests) {
            createBedAndGuest(guest);
        }
    }

    @FXML
    private void handleAddGuestButton() {
        viewModel.openGuestForm();
    }

    private void createBedAndGuest(Guest guest) {
        // Calcular a posição da cama com base no índice (bedCount)
        double positionX = START_X + (bedCount * (BED_WIDTH + 10)); // Espaço de 10px entre camas
        double positionY = START_Y;

        // Criar a cama
        ImageView bedView = new ImageView();
        bedView.setFitWidth(BED_WIDTH);
        bedView.setFitHeight(BED_HEIGHT);
        bedView.setImage(new Image(getClass()
                .getResource("/org/example/television_problem/view/assets/bed.png").toExternalForm()));
        bedView.setTranslateX(positionX);
        bedView.setTranslateY(positionY);

        // Adicionar a cama ao container
        guestContainer.getChildren().add(bedView);

        // Criar o Guest
        ImageView guestView = new ImageView();
        guestView.setFitWidth(GUEST_WIDTH);
        guestView.setFitHeight(GUEST_HEIGHT);

        // Definir o sprite inicial para o guest
        guest.setImage(new Image(getClass()
                .getResource("/org/example/television_problem/view/assets/guest_sprite.png").toExternalForm()));

        // Vincular a imagem do Guest ao seu próprio modelo
        guestView.imageProperty().bind(guest.imageProperty()); // Atualize a imagem de acordo com o modelo do Guest

        // Posição inicial do Guest (igual à cama)
        guestView.setTranslateX(positionX + (BED_WIDTH - GUEST_WIDTH) / 2); // Centraliza o Guest na cama
        guestView.setTranslateY(positionY); // Ajuste vertical para parecer em cima da cama

        guest.setPositionX(positionX);
        guest.setPositionY(positionY);
        guest.setBedPositionX(positionX);
        guest.setBedPositionY(positionY);
        // Vincular posição do Guest ao modelo
        guestView.translateXProperty().bind(Bindings.createDoubleBinding(
                () -> guest.getPositionX(), guest.positionXProperty()));
        guestView.translateYProperty().bind(Bindings.createDoubleBinding(
                () -> guest.getPositionY(), guest.positionYProperty()));

        // Criar o ID Label para o Guest
        Label idLabel = new Label();
        idLabel.setText("ID: " + guest.getGuestId()); // Exibir o ID do guest
        idLabel.setStyle(
                "-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: black;");
        idLabel.setTranslateX(positionX + (BED_WIDTH - GUEST_WIDTH) / 2); // Centraliza o label na cama
        idLabel.setTranslateY(positionY - 20); // Coloca o label acima do sprite

        // Vincular posição do ID Label ao modelo do Guest
        idLabel.translateXProperty().bind(Bindings.createDoubleBinding(
                () -> guest.getPositionX() + (BED_WIDTH - GUEST_WIDTH) / 2, guest.positionXProperty()));
        idLabel.translateYProperty().bind(Bindings.createDoubleBinding(
                () -> guest.getPositionY() - 20, guest.positionYProperty()));

        // Adicionar o Guest e o ID Label ao container
        guestContainer.getChildren().addAll(guestView, idLabel);

        // Incrementar o contador de camas
        bedCount++;
    }

    // Método para remover o Guest e a cama correspondente
    private void removeGuestAndBed(Guest guest) {
        guestContainer.getChildren().removeIf(node -> (node instanceof ImageView && node.getUserData() == guest));
    }

}
