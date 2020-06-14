package com.lab.labbook.ui.views.log;

import com.lab.labbook.client.LogClient;
import com.lab.labbook.domain.LogDto;
import com.lab.labbook.ui.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "logs", layout = MainLayout.class)
@PageTitle("Logs from API")
public class LogView extends VerticalLayout {

    private Grid<LogDto> grid = new Grid<>(LogDto.class, false);
    private final LogClient logClient = new LogClient();

    public LogView() {
        addClassName("log_view");

        setSizeFull();
        configureGrid();

        HorizontalLayout mainContent = new HorizontalLayout(grid);
        mainContent.setSizeFull();

        add(mainContent);
        refresh();
    }

    private void refresh() {
        grid.setItems(logClient.getLogs());
    }

    private void configureGrid() {
        grid.setClassName("log-grid");
        grid.setSizeFull();

        grid.addColumns("level");
        grid.addColumns("log");
        grid.addColumns("comments");
        grid.addColumns("date");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }
}
