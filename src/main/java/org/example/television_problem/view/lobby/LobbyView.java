package org.example.television_problem.view.lobby;

import org.example.television_problem.view_model.lobby.LobbyViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;

public class LobbyView implements FxmlView<LobbyViewModel> {

    @InjectViewModel
    LobbyViewModel viewModel;

    
}