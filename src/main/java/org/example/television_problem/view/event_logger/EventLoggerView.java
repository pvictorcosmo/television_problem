package org.example.television_problem.view.event_logger;

import org.example.television_problem.view_model.event_logger.EventLoggerViewModel;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;

public class EventLoggerView implements FxmlView<EventLoggerViewModel> {
    @InjectViewModel
    EventLoggerViewModel viewModel;
}