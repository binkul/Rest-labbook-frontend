package com.lab.labbook.ui.views.series;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lab.labbook.client.SeriesClient;
import com.lab.labbook.client.exception.ResponseStatus;
import com.lab.labbook.domain.SeriesDto;
import com.lab.labbook.ui.MainLayout;
import com.lab.labbook.ui.views.common.MessagesView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "series", layout = MainLayout.class)
@PageTitle("Series of LabBook")
public class SeriesView extends VerticalLayout {

    private final SeriesForm form;
    private final SeriesClient seriesClient = new SeriesClient();

    private Grid<SeriesDto> grid = new Grid<>(SeriesDto.class, false);
    private Button btnAddNew = new Button("Add new");
    private TextField txtFindTitle = new TextField();

    public SeriesView() {
        setClassName("Series-view");
        setSizeFull();
        configureGrid();
        configureTitleFilter();

        form = new SeriesForm(this);
        form.setSeries(null);

        btnAddNew.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnAddNew.addClickListener(e -> addNewSeries());

        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        HorizontalLayout toolBar = new HorizontalLayout(txtFindTitle, btnAddNew);
        mainContent.setSizeFull();

        add(toolBar, mainContent);
        refresh();
    }

    public void refresh() {
        grid.setItems(seriesClient.getSeries(txtFindTitle.getValue()));
    }

    private void addNewSeries() {
        grid.asSingleSelect().clear();
        SeriesDto seriesDto = new SeriesDto();
        form.setSeries(seriesDto);
    }

    private void configureTitleFilter() {
        txtFindTitle.setPlaceholder("Filter by title ...");
        txtFindTitle.setClearButtonVisible(true);
        txtFindTitle.setValueChangeMode(ValueChangeMode.LAZY);
        txtFindTitle.addValueChangeListener(i -> refresh());
    }

    private void configureGrid() {
        grid.setClassName("series-grid");
        grid.setSizeFull();

        grid.addColumns("title");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> selectRow());
    }

    private void selectRow() {
        form.setSeries(grid.asSingleSelect().getValue());
    }

    public boolean save(SeriesDto seriesDto) {
        try {
            ResponseStatus status = seriesClient.add(seriesDto);
            return MessagesView.showException(status.getStatus(), status.getMessage());
        } catch (JsonProcessingException ex) {
            MessagesView.showException(MessagesView.NOT_OK, MessagesView.DEFAULT_ERROR_MESSAGE);
        }
        return false;
    }

    public boolean update(SeriesDto seriesDto) {
        try {
            ResponseStatus status = seriesClient.update(seriesDto);
            return MessagesView.showException(status.getStatus(), status.getMessage());
        } catch (JsonProcessingException ex) {
            MessagesView.showException(MessagesView.NOT_OK, MessagesView.DEFAULT_ERROR_MESSAGE);
        }
        return false;
    }

    public boolean delete(Long id) {
        try {
            ResponseStatus status = seriesClient.delete(id);
            return MessagesView.showException(status.getStatus(), status.getMessage());
        } catch (JsonProcessingException ex) {
            MessagesView.showException(MessagesView.NOT_OK, MessagesView.DEFAULT_ERROR_MESSAGE);
        }
        return false;
    }
}
