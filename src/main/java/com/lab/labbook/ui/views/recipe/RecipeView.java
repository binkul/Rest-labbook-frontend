package com.lab.labbook.ui.views.recipe;

import com.lab.labbook.client.IngredientClient;
import com.lab.labbook.client.MaterialClient;
import com.lab.labbook.domain.IngredientDto;
import com.lab.labbook.domain.IngredientMoveDto;
import com.lab.labbook.domain.MaterialDto;
import com.lab.labbook.domain.PriceDto;
import com.lab.labbook.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Route(value = "recipe", layout = MainLayout.class)
@PageTitle("Recipe from Lab book")
public class RecipeView extends VerticalLayout implements HasUrlParameter<String> {

    private Long labBookId;
    private BigDecimal density;

    private final RecipeForm form;
    private Grid<IngredientDto> grid = new Grid<>(IngredientDto.class, false);
    private Button btnAddNew = new Button("Add new");
    private Label lblPriceNbp = new Label("Price: 0 PLN");
    private Label lblPriceCommercial = new Label("Price: 0 PLN");
    private Label lblVoc = new Label("VOC: 0 g/l");
    private Label lblSum = new Label("Sum: 0%");

    private IngredientClient ingredientClient = new IngredientClient();
    private MaterialClient materialClient = new MaterialClient();
    private List<MaterialDto> materials = materialClient.getAll("");

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        String[] param = parameter.split("-");
        labBookId = Long.valueOf(param[0]);
        density = new BigDecimal(param[1]);
        refresh();
    }

    public RecipeView() {
        setClassName("recipe-view");
        setSizeFull();
        configureGrid();

        form = new RecipeForm(materials, this);
        form.setIngredient(null);

        btnAddNew.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnAddNew.addClickListener(e -> addNewIngredient());

        lblPriceNbp.addClassName("normal-label");
        lblPriceCommercial.addClassName("normal-label");
        lblVoc.addClassName("normal-label");
        lblSum.addClassName("normal-label");

        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        HorizontalLayout toolBar = new HorizontalLayout(btnAddNew, lblPriceNbp, lblPriceCommercial, lblVoc, lblSum);
        mainContent.setSizeFull();

        add(toolBar, mainContent);
    }

    void refresh() {
        grid.setItems(ingredientClient.getAll(labBookId));
        calculation();
    }

    private void addNewIngredient() {
        grid.asSingleSelect().clear();
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setAmount(BigDecimal.ZERO);
        ingredientDto.setLabId(labBookId);
        ingredientDto.setMaterial(materials.get(0));
        form.setIngredient(ingredientDto);
    }

    private void configureGrid() {
        grid.setClassName("recipe-grid");
        grid.setSizeFull();

        grid.addColumns("ordinal");
        grid.addColumn(ing -> getMaterialById(ing.getMaterialId()).getName()).setHeader("Material");
        grid.addColumn(this::setPriceContent).setHeader("Price").setSortable(false);
        grid.addColumns("amount");
        grid.addColumns("comment");
        addMoveColumn();
        addDelColumn();

        grid.getColumnByKey("ordinal").setHeader("lp");
        grid.getColumnByKey("amount").setHeader("Content [%]").setSortable(false);
        grid.getColumnByKey("comment").setSortable(false);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> selectRow());
    }

    private void selectRow() {
        IngredientDto ingredient = grid.asSingleSelect().getValue();
        if (ingredient != null) {
            ingredient.setMaterial(getMaterialById(ingredient.getMaterialId()));
        }
        form.setIngredient(ingredient);
    }

    private String setPriceContent(IngredientDto ingredientDto) {
        String result = getMaterialById(ingredientDto.getMaterialId()).getPrice().toString();
        result += " ";
        result += getMaterialById(ingredientDto.getMaterialId()).getSymbol();
        return result;
    }

    private void addMoveColumn() {
        grid.addComponentColumn(i -> {
            Button buttonUp = new Button(new Icon(VaadinIcon.ARROW_UP));
            Button buttonDown = new Button(new Icon(VaadinIcon.ARROW_DOWN));
            buttonUp.addClickListener(click -> {
                ingredientClient.move(new IngredientMoveDto(i.getId(), "up"));
                refresh();
            });
            buttonDown.addClickListener(click -> {
                ingredientClient.move(new IngredientMoveDto(i.getId(), "down"));
                refresh();
            });
            return new HorizontalLayout(buttonUp, buttonDown);
        }).setHeader("Move").setSortable(false);
    }

    private void addDelColumn() {
        grid.addComponentColumn(i -> {
            Button buttonDel = new Button(new Icon(VaadinIcon.DEL_A));
            buttonDel.addClickListener(click -> {
                ingredientClient.delete(i.getId());
                refresh();
            });
            return new HorizontalLayout(buttonDel);
        }).setHeader("Delete").setSortable(false);
    }

    private MaterialDto getMaterialById(Long id) {
        return materials.stream()
                .filter(i -> i.getId() == id)
                .findFirst().orElseGet(null);
    }

    private void calculation() {
        int scale = 2;

        PriceDto price = ingredientClient.getPrice(labBookId);
        BigDecimal priceLiterNbp = price.getNbpPrice().multiply(density).setScale(scale, RoundingMode.CEILING);
        lblPriceNbp.setText(String.format("NBP: %s zł/kg [%s zł/l]",  price.getNbpPrice(), priceLiterNbp));

        BigDecimal priceLiterCommercial = price.getCommercialPrice().multiply(density).setScale(scale, RoundingMode.CEILING);
        lblPriceCommercial.setText(String.format("Cantor: %s zł/kg [%s zł/l]",  price.getCommercialPrice(), priceLiterCommercial));

        BigDecimal voc = ingredientClient.getVoc(labBookId);
        lblVoc.setText(String.format("Voc: %s g/l", voc));

        BigDecimal sum = getSum();
        lblSum.setText(String.format("Sum: %s %s", sum, "%"));
        addCssToSum(sum);
    }

    void save(IngredientDto ingredientDto) {
        ingredientClient.add(ingredientDto);
    }

    void update(IngredientDto ingredientDto) {
        ingredientClient.update(ingredientDto);
    }

    BigDecimal getSum() {
        return ingredientClient.getSum(labBookId);
    }

    private void addCssToSum(BigDecimal sum) {
        if (sum.compareTo(new BigDecimal("100")) <= 0) {
            lblSum.removeClassName("red-label");
            lblSum.addClassName("normal-label");
        } else {
            lblSum.removeClassName("normal-label");
            lblSum.addClassName("red-label");
        }
    }
}
