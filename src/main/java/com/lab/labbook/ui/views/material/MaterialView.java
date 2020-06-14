package com.lab.labbook.ui.views.material;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lab.labbook.client.CurrencyClient;
import com.lab.labbook.client.MaterialClient;
import com.lab.labbook.client.exception.ResponseStatus;
import com.lab.labbook.client.SupplierClient;
import com.lab.labbook.domain.CurrencyDto;
import com.lab.labbook.domain.MaterialDto;
import com.lab.labbook.domain.SupplierDto;
import com.lab.labbook.ui.MainLayout;
import com.lab.labbook.ui.views.common.MessagesView;
import com.lab.labbook.ui.views.recipe.RecipeView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Route(value = "material", layout = MainLayout.class)
@PageTitle("Recipe from Lab book")
public class MaterialView extends VerticalLayout {

    private final MaterialForm form;
    private final CurrencyClient currencyClient = new CurrencyClient();
    private final SupplierClient supplierClient = new SupplierClient();
    private final MaterialClient materialClient = new MaterialClient();

    private Grid<MaterialDto> grid = new Grid<>(MaterialDto.class, false);
    private Button btnAddNew = new Button("Add new");
    private Label lblInfo = new Label("");
    private TextField txtFindTitle = new TextField();

    private List<CurrencyDto> currencies = currencyClient.getAll();
    private List<SupplierDto> suppliers = supplierClient.getAll("");

    public MaterialView() {
        setClassName("material-view");
        setSizeFull();
        configureGrid();
        configureTitleFilter();

        form = new MaterialForm(currencies, suppliers, this);
        form.setMaterial(null);

        btnAddNew.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnAddNew.addClickListener(e -> addNewMaterial());

        lblInfo.addClassName("info-label");

        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        HorizontalLayout toolBar = new HorizontalLayout(txtFindTitle, btnAddNew, lblInfo);
        mainContent.setSizeFull();

        add(toolBar, mainContent);
        refresh();
    }

    private void configureTitleFilter() {
        txtFindTitle.setPlaceholder("Filter by name ...");
        txtFindTitle.setClearButtonVisible(true);
        txtFindTitle.setValueChangeMode(ValueChangeMode.LAZY);
        txtFindTitle.addValueChangeListener(i -> refresh());
    }

    void refresh() {
        grid.setItems(materialClient.getAll(txtFindTitle.getValue()));
    }

    private void addNewMaterial() {
        grid.asSingleSelect().clear();
        MaterialDto materialDto = new MaterialDto();
        materialDto.setPrice(BigDecimal.ZERO);
        materialDto.setVoc(BigDecimal.ZERO);
        materialDto.setCurrencyId(currencies.size() > 0 ? currencies.get(0).getId() : null);
        materialDto.setCurrency(currencies.size() > 0 ? currencies.get(0) : null);
        materialDto.setSupplierId(suppliers.size() > 0 ? suppliers.get(0).getId() : null);
        materialDto.setSupplier(suppliers.size() > 0 ? suppliers.get(0) : null);
        form.setMaterial(materialDto);
    }

    private void configureGrid() {
        grid.setClassName("material-grid");
        grid.setSizeFull();

        grid.addColumns("name");
        grid.addColumn(mat -> mat.getPrice() + " " + mat.getSymbol()).setHeader("Price");
        grid.addColumns("voc");
        grid.addColumn(sup -> getSupplierById(sup.getSupplierId()).getShortName()).setHeader("Supplier");
        addGhsSymbols();
        addSafetyButton();

        grid.getColumnByKey("voc").setHeader("VOC");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> selectRow());
    }

    private void selectRow() {
        MaterialDto materialDto = grid.asSingleSelect().getValue();
        if (materialDto != null) {
            materialDto.setCurrency(getCurrencyById(materialDto.getCurrencyId()));
            materialDto.setSupplier(getSupplierById(materialDto.getSupplierId()));
        }
        form.setMaterial(materialDto);
    }

    private void addGhsSymbols() {
        grid.addColumn(new ComponentRenderer<>(i -> {
            HorizontalLayout layout = new HorizontalLayout();
            if (i.getSymbols().size() > 0) {
                for (Map.Entry<String, String> entry : i.getSymbols().entrySet()) {
                    String link = "img/" + entry.getKey() + ".png";
                    Image image = new Image(link, entry.getValue());
                    image.setWidth("36px");
                    image.setWidth("36px");
                    layout.add(image);
                }
            } else {
                layout.add(new Label("Safe"));
            }
            return layout;
        })).setHeader("Danger");
    }

    private void addSafetyButton() {
        grid.addComponentColumn(i -> {
            Button button = new Button("CLP ->");
            String link = i.getId().toString();
            RouterLink routerLink = new RouterLink("", ClpDataForm.class, link);
            routerLink.getElement().appendChild(button.getElement());
            return routerLink;
        }).setHeader("Safety");
    }

    private CurrencyDto getCurrencyById(Long id) {
        return currencies.stream()
                .filter(i -> i.getId() == id)
                .findFirst().orElseGet(null);
    }

    private SupplierDto getSupplierById(Long id) {
        return suppliers.stream()
                .filter(i -> i.getId() == id)
                .findFirst().orElseGet(null);
    }

    public boolean save(MaterialDto materialDto) {
        try {
            ResponseStatus status = materialClient.add(materialDto);
            return MessagesView.showException(status.getStatus(), status.getMessage());
        } catch (JsonProcessingException ex) {
            MessagesView.showException(MessagesView.NOT_OK, MessagesView.DEFAULT_ERROR_MESSAGE);
        }
        return false;
    }

    public boolean update(MaterialDto materialDto) {
        try {
            ResponseStatus status = materialClient.update(materialDto);
            return MessagesView.showException(status.getStatus(), status.getMessage());
        } catch (JsonProcessingException ex) {
            MessagesView.showException(MessagesView.NOT_OK, MessagesView.DEFAULT_ERROR_MESSAGE);
        }
        return false;
    }

    public boolean delete(Long id) {
        try {
            ResponseStatus status = materialClient.delete(id);
            return MessagesView.showException(status.getStatus(), status.getMessage());
        } catch (JsonProcessingException ex) {
            MessagesView.showException(MessagesView.NOT_OK, MessagesView.DEFAULT_ERROR_MESSAGE);
        }
        return false;
    }
}
