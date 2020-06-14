package com.lab.labbook.ui.views.material;

import com.lab.labbook.domain.CurrencyDto;
import com.lab.labbook.domain.MaterialDto;
import com.lab.labbook.domain.SupplierDto;
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

import java.util.List;
import java.util.Objects;

public class MaterialForm extends FormLayout {

    private final List<CurrencyDto> currencies;
    private final List<SupplierDto> suppliers;
    private final MaterialView materialView;

    private TextField name = new TextField("Name");
    private TextField price = new TextField("Price");
    private TextField voc = new TextField("VOC");
    private ComboBox<CurrencyDto> cmbCurrency = new ComboBox<>("Currency");
    private ComboBox<SupplierDto> cmbSupplier = new ComboBox<>("Supplier");

    private Button btnSave = new Button("Save");
    private Button btnDelete = new Button("Delete");
    private Button btnCancel = new Button("Cancel");

    private Binder<MaterialDto> binder = new Binder<>(MaterialDto.class);

    public MaterialForm(List<CurrencyDto> currencies, List<SupplierDto> suppliers, MaterialView materialView) {
        this.currencies = currencies;
        this.suppliers = suppliers;
        this.materialView = materialView;

        addClassName("material-form");
        createBinder();
        createCombos();

        btnSave.addClickListener(i -> save());
        btnDelete.addClickListener(i -> delete());
        btnCancel.addClickListener(i -> cancel());

        VerticalLayout formLayout = new VerticalLayout(name, price, voc, cmbCurrency, cmbSupplier, createButtonsLayout());
        add(formLayout);
    }

    void setMaterial(MaterialDto materialDto) {
        binder.setBean(materialDto);

        if (materialDto == null) {
            setVisible(false);
        } else if (materialDto.getId() == null) {
            setVisible(true);
            name.setEnabled(true);
            name.focus();
        } else {
            setVisible(true);
            name.setEnabled(false);
            price.focus();
        }
    }

    private void createBinder() {
        binder.forField(name).withValidator(string -> string != null && !string.isEmpty(), "Please enter the name.")
                .bind(MaterialDto::getName, MaterialDto::setName);
        binder.forField(price).withValidator(string -> string != null && !string.isEmpty(), "Please enter the price.")
                .withConverter(new StringToBigDecimalConverter("Incorrect value."))
                .bind(MaterialDto::getPrice, MaterialDto::setPrice);
        binder.forField(voc).withValidator(string -> string != null && !string.isEmpty(), "Please enter the VOC.")
                .withConverter(new StringToBigDecimalConverter("Incorrect value."))
                .bind(MaterialDto::getVoc, MaterialDto::setVoc);
        binder.forField(cmbCurrency).withValidator(Objects::nonNull, "Please chose currency")
                .bind(MaterialDto::getCurrency, MaterialDto::setCurrency);
        binder.forField(cmbSupplier).withValidator(Objects::nonNull, "Please chose currency")
                .bind(MaterialDto::getSupplier, MaterialDto::setSupplier);
    }

    private void createCombos() {
        cmbCurrency.setItems(currencies);
        cmbCurrency.setItemLabelGenerator(CurrencyDto::getSymbol);
        cmbSupplier.setItems(suppliers);
        cmbSupplier.setItemLabelGenerator(SupplierDto::getShortName);
    }

    private Component createButtonsLayout() {
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnCancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        btnSave.addClickShortcut(Key.ENTER);
        btnCancel.addClickShortcut(Key.ESCAPE);
        return new HorizontalLayout(btnSave, btnDelete, btnCancel);
    }

    private void cancel() {
        setMaterial(null);
    }

    private void delete() {
        MaterialDto materialDto = binder.getBean();
        if (materialView.delete(materialDto.getId())) {
            setVisible(false);
            materialView.refresh();
        }
    }

    private void save() {
        if(binder.validate().hasErrors()) return;

        MaterialDto materialDto = binder.getBean();
        boolean status;

        materialDto.setCurrencyId(cmbCurrency.getValue().getId());
        if (materialDto.getId() != null) {
            status = materialView.update(materialDto);
        } else {
            status = materialView.save(materialDto);
        }

        if (status) {
            setVisible(false);
            materialView.refresh();
        }
    }
}
