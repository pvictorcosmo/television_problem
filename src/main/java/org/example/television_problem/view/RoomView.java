package org.example.television_problem.view;

import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.television_problem.view_model.RoomViewModel;

public class RoomView implements FxmlView<RoomViewModel> {

    @FXML
    public Label welcomeText;
}
