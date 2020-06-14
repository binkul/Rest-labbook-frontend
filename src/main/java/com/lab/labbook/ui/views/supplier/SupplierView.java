package com.lab.labbook.ui.views.supplier;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lab.labbook.client.SupplierClient;
import com.lab.labbook.client.exception.ResponseStatus;
import com.lab.labbook.domain.SupplierDto;
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

@Route(value = "supplier", layout = MainLayout.class)
@PageTitle("Suppliers")
public class SupplierView extends VerticalLayout {

    private final SupplierForm form;
    private final SupplierClient supplierClient = new SupplierClient();

    private Grid<SupplierDto> grid = new Grid<>(SupplierDto.class, false);
    private Button btnAddNew = new Button("Add new");
    private TextField txtFindTitle = new TextField();

    public SupplierView() {
        addClassName("supplier-form");
        setSizeFull();
        configureGrid();
        configureTitleFilter();

        this.form = new SupplierForm(this);
        this.form.setSupplier(null);

        btnAddNew.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnAddNew.addClickListener(e -> addNewMaterial());

        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        HorizontalLayout toolBar = new HorizontalLayout(txtFindTitle, btnAddNew);
        mainContent.setSizeFull();

        add(toolBar, mainContent);
        refresh();
    }

    public void refresh() {
        grid.setItems(supplierClient.getAll(txtFindTitle.getValue()));
    }

    private void addNewMaterial() {
        grid.asSingleSelect().clear();
        form.setSupplier(new SupplierDto());
    }

    private void configureTitleFilter() {
        txtFindTitle.setPlaceholder("Filter by name ...");
        txtFindTitle.setClearButtonVisible(true);
        txtFindTitle.setValueChangeMode(ValueChangeMode.LAZY);
        txtFindTitle.addValueChangeListener(i -> refresh());
    }

    private void configureGrid() {
        grid.setClassName("supplier-grid");
        grid.setSizeFull();

        grid.addColumns("name");
        grid.addColumns("shortName");
        grid.addColumns("address");
        grid.addColumns("phones");
        grid.addColumn(sup -> {
            if (sup.isProducer()) return "Yes";
            return "No";
        }).setHeader("Is producer");
        grid.addColumns("comments");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> selectRow());
    }

    private void selectRow() {
        SupplierDto supplierDto = grid.asSingleSelect().getValue();
        form.setSupplier(supplierDto);
    }

    public boolean delete(Long id) {
        try {
            ResponseStatus status = supplierClient.delete(id);
            return MessagesView.showException(status.getStatus(), status.getMessage());
        } catch (JsonProcessingException ex) {
            MessagesView.showException(MessagesView.NOT_OK, MessagesView.DEFAULT_ERROR_MESSAGE);
        }
        return false;
    }

    public boolean save(SupplierDto supplierDto) {
        try {
            ResponseStatus status = supplierClient.add(supplierDto);
            return MessagesView.showException(status.getStatus(), status.getMessage());
        } catch (JsonProcessingException ex) {
            MessagesView.showException(MessagesView.NOT_OK, MessagesView.DEFAULT_ERROR_MESSAGE);
        }
        return false;
    }

    public boolean update(SupplierDto supplierDto) {
        try {
            ResponseStatus status = supplierClient.update(supplierDto);
            return MessagesView.showException(status.getStatus(), status.getMessage());
        } catch (JsonProcessingException ex) {
            MessagesView.showException(MessagesView.NOT_OK, MessagesView.DEFAULT_ERROR_MESSAGE);
        }
        return false;
    }
}
