package org.example.television_problem.view.guest;

import org.example.television_problem.view_model.guest.GuestViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class GuestView implements FxmlView<GuestViewModel> {

    @FXML
    private ImageView spriteView;
    @InjectViewModel
    private GuestViewModel viewModel;

    public void initialize() {
        this.viewModel = new GuestViewModel();
        // Bindings: Conecta ViewModel à View
        spriteView.translateXProperty().bind(viewModel.xProperty());
        spriteView.translateYProperty().bind(viewModel.yProperty());
        spriteView.imageProperty().bind(viewModel.currentImageProperty());

        // Configura eventos de teclado
        spriteView.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    switch (event.getCode()) {
                        case W -> viewModel.moveBack();
                        case S -> viewModel.moveFront();
                        case A -> viewModel.moveLeft();
                        case D -> viewModel.moveRight();
                        case SPACE -> viewModel.stop();
                    }
                });
            }
        });
    }

}