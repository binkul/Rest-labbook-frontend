package com.lab.labbook.ui.views.supplier;

import com.lab.labbook.domain.MaterialDto;
import com.lab.labbook.domain.SeriesDto;
import com.lab.labbook.domain.SupplierDto;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class SupplierForm extends FormLayout {

    private final SupplierView supplierView;

    private TextField name = new TextField("Name");
    private TextField shortName = new TextField("Short name");
    private TextField address = new TextField("Address");
    private TextField phones = new TextField("Phones");
    private Checkbox producer = new Checkbox("Is producer");
    private TextField comments = new TextField("Comments");

    private Button btnSave = new Button("Save");
    private Button btnDelete = new Button("Delete");
    private Button btnCancel = new Button("Cancel");

    private Binder<SupplierDto> binder = new Binder<>(SupplierDto.class);

    public SupplierForm(SupplierView supplierView) {
        this.supplierView = supplierView;
        addClassName("supplier-view");

        createBinder();

        btnSave.addClickListener(i -> save());
        btnDelete.addClickListener(i -> delete());
        btnCancel.addClickListener(i -> cancel());

        VerticalLayout formLayout = new VerticalLayout(name, shortName, address, phones, producer, comments, createButtonsLayout());
        add(formLayout);
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
        setSupplier(null);
    }

    private void delete() {
        SupplierDto supplierDto = binder.getBean();
        if (supplierView.delete(supplierDto.getId())) {
            setVisible(false);
            supplierView.refresh();
        }
    }

    private void save() {
        if(binder.validate().hasErrors()) return;

        SupplierDto supplierDto = binder.getBean();
        boolean status;

        if (supplierDto.getId() != null) {
            status = supplierView.update(supplierDto);
        } else {
            status = supplierView.save(supplierDto);
        }

        if (status) {
            setVisible(false);
            supplierView.refresh();
        }
    }

    private void createBinder() {
        binder.forField(name).withValidator(string -> string != null && !string.isEmpty(), "Please enter the name.")
                .bind(SupplierDto::getName, SupplierDto::setName);
        binder.forField(shortName).withValidator(string -> string != null && !string.isEmpty(), "Please enter the short name.")
                .bind(SupplierDto::getShortName, SupplierDto::setShortName);
        binder.forField(address).bind(SupplierDto::getAddress, SupplierDto::setAddress);
        binder.forField(phones).bind(SupplierDto::getPhones, SupplierDto::setPhones);
        binder.forField(producer).bind(SupplierDto::isProducer, SupplierDto::setProducer);
        binder.forField(comments).bind(SupplierDto::getComments, SupplierDto::setComments);
    }

    public void setSupplier(SupplierDto supplierDto) {
        binder.setBean(supplierDto);

        if (supplierDto == null) {
            setVisible(false);
        } else {
            setVisible(true);
            name.focus();
        }
    }
}
