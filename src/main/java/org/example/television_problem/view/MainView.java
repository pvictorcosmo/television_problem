package org.example.television_problem.view;

import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import org.example.television_problem.view_model.MainViewModel;

public class MainView implements FxmlView<MainViewModel> {

    @FXML
    public Label welcomeText;
}
