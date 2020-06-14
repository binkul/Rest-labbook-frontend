package com.lab.labbook.ui.views.series;

import com.lab.labbook.domain.SeriesDto;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class SeriesForm extends FormLayout {

    private final SeriesView seriesView;

    private TextField title = new TextField("Title");

    private Button btnSave = new Button("Save");
    private Button btnDelete = new Button("Delete");
    private Button btnCancel = new Button("Cancel");

    private Binder<SeriesDto> binder = new Binder<>(SeriesDto.class);


    public SeriesForm(SeriesView seriesView) {
        this.seriesView = seriesView;
        addClassName("series-form");

        binder.forField(title).withValidator(string -> string != null && !string.isEmpty(), "Please enter the title.")
                .bind(SeriesDto::getTitle, SeriesDto::setTitle);

        btnSave.addClickListener(i -> save());
        btnDelete.addClickListener(i -> delete());
        btnCancel.addClickListener(i -> cancel());

        VerticalLayout formLayout = new VerticalLayout(title, createButtonsLayout());
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

    private void delete() {
        SeriesDto seriesDto = binder.getBean();
        if (seriesView.delete(seriesDto.getId())) {
            setVisible(false);
            seriesView.refresh();
        }
    }

    private void cancel() {
        setSeries(null);
    }

    private void save() {
        if (binder.validate().hasErrors()) return;

        SeriesDto seriesDto = binder.getBean();
        boolean status;

        if (seriesDto.getId() != null) {
            status = seriesView.update(seriesDto);
        } else {
            status = seriesView.save(seriesDto);
        }

        if (status) {
            setVisible(false);
            seriesView.refresh();
        }
    }

    public void setSeries(SeriesDto seriesDto) {
        binder.setBean(seriesDto);

        if (seriesDto == null) {
            setVisible(false);
        } else {
            setVisible(true);
        }
    }
}
