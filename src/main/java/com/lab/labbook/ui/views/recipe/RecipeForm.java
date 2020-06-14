package com.lab.labbook.ui.views.recipe;

import com.lab.labbook.domain.IngredientDto;
import com.lab.labbook.domain.MaterialDto;
import com.lab.labbook.ui.views.common.MessagesView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


public class RecipeForm extends FormLayout {

    private List<MaterialDto> materials;
    private final RecipeView recipeView;

    private TextField amount = new TextField("Amount [%]");
    private TextField comment = new TextField("Comment");
    private ComboBox<MaterialDto> cmbMaterials = new ComboBox<>("Material");

    private Button btnSave = new Button("Save");
    private Button btnUpTo = new Button("Up to 100%");
    private Button btnCancel = new Button("Cancel");

    private Binder<IngredientDto> binder = new Binder<>(IngredientDto.class);

    public RecipeForm(List<MaterialDto> materials, RecipeView recipeView) {

        this.materials = materials;
        this.recipeView = recipeView;

        binder.forField(amount).withConverter(new StringToBigDecimalConverter("Incorrect value"))
                .bind(IngredientDto::getAmount, IngredientDto::setAmount);
        binder.forField(comment).bind(IngredientDto::getComment, IngredientDto::setComment);
        binder.forField(cmbMaterials).withValidator(Objects::nonNull, "Please chose raw material")
                .bind(IngredientDto::getMaterial, IngredientDto::setMaterial);

        cmbMaterials.setItems(materials);
        cmbMaterials.setItemLabelGenerator(MaterialDto::getName);
        cmbMaterials.setValue(materials.get(0));

        btnSave.addClickListener(i -> save());
        btnUpTo.addClickListener(i -> fillToHundred());
        btnCancel.addClickListener(i -> cancel());

        VerticalLayout formLayout = new VerticalLayout(amount, comment, cmbMaterials, createButtonsLayout());
        add(formLayout);
    }

    private void cancel() {
        setIngredient(null);
    }

    private void save() {
        IngredientDto ingredientDto = binder.getBean();
        ingredientDto.setMaterialId(cmbMaterials.getValue().getId());
        if (ingredientDto.getId() != null) {
            recipeView.update(ingredientDto);
            setVisible(false);
        } else {
            recipeView.save(ingredientDto);
            setVisible(false);
        }
        recipeView.refresh();
    }

    private void fillToHundred() {
        IngredientDto ingredient = binder.getBean();
        BigDecimal sum = recipeView.getSum();
        BigDecimal different = sum.subtract(new BigDecimal("100"));
        if (ingredient.getId() != null) {
            amount.setValue(ingredient.getAmount().subtract(different).toString().replace(".", ","));
        } else if (sum.compareTo(new BigDecimal("100")) < 0) {
            amount.setValue(different.abs().toString().replace(".", ","));
        } else {
            MessagesView.showException("200", "Sum is over 100%");
        }
    }

    private Component createButtonsLayout() {
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnUpTo.addThemeVariants(ButtonVariant.MATERIAL_OUTLINED);
        btnCancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        btnSave.addClickShortcut(Key.ENTER);
        btnCancel.addClickShortcut(Key.ESCAPE);
        return new HorizontalLayout(btnSave, btnUpTo, btnCancel);
    }

    void setIngredient(IngredientDto ingredient) {
        binder.setBean(ingredient);

        if (ingredient == null) {
            setVisible(false);
        } else {
            setVisible(true);
            amount.focus();
        }
    }
}
