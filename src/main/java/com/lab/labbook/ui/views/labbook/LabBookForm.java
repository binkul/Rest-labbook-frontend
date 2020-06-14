package com.lab.labbook.ui.views.labbook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lab.labbook.client.LabBookClient;
import com.lab.labbook.client.SeriesClient;
import com.lab.labbook.client.exception.ResponseStatus;
import com.lab.labbook.domain.LabBookDto;
import com.lab.labbook.domain.SeriesDto;
import com.lab.labbook.ui.MainLayout;
import com.lab.labbook.ui.views.common.MessagesView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigDecimalConverter;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Route(value = "edit", layout = MainLayout.class)
@PageTitle("Work edit")
public class LabBookForm extends FormLayout implements HasUrlParameter<String> {

    private Long userId;
    private Long labId;

    private Label lblStatus = new Label("Status: none");
    private TextField title = new TextField("Title of the laboratory work");
    private TextArea description = new TextArea("Description");
    private TextArea conclusion = new TextArea("Conclusion");
    private TextField density = new TextField("Density");
    private ComboBox<SeriesDto> cmbSeries = new ComboBox<>("Series");
    private Button btnSave = new Button("Save");
    private Button btnDelete = new Button("Delete");
    private Button btnCancel = new Button("Cancel");
    private Binder<LabBookDto> binder = new Binder<>(LabBookDto.class);

    private final LabBookClient labBookClient = new LabBookClient();
    private final SeriesClient seriesClient = new SeriesClient();
    private List<SeriesDto> series = seriesClient.getSeries("");
    private LabBookDto labBookDto;

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        String[] param = parameter.split("_");
        this.userId = Long.valueOf(param[0]);
        this.labId = Long.valueOf(param[1]);
        setForm();
    }

    public LabBookForm() {
        addClassName("labbook-form");
        bindFields();

        lblStatus.addClassName("info-label");
        cmbSeries.setItems(series);
        cmbSeries.setItemLabelGenerator(SeriesDto::getTitle);

        btnSave.addClickListener(i -> save());
        btnDelete.addClickListener(i -> delete());
        btnCancel.addClickListener(i -> exit());

        VerticalLayout content = new VerticalLayout(lblStatus, title, description, conclusion, density, cmbSeries, createButtonLayout());
        content.setSizeFull();
        title.setWidth("200%");
        description.setWidth("200%");
        conclusion.setWidth("200%");
        add(content);
    }

    private void setForm() {
        if (labId > 0) {
            labBookDto = labBookClient.getById(labId);
            labBookDto.setSeriesDto(getSeriesById(labBookDto.getSeriesId()));
            lblStatus.setText("Status: " + labBookDto.getStatus());
        } else {
            labBookDto = new LabBookDto();
            labBookDto.setDensity(BigDecimal.ONE);
            labBookDto.setSeriesId(series.size() > 0 ? series.get(0).getId() : null);
            labBookDto.setUserId(userId);
        }
        binder.setBean(labBookDto);
    }

    private void exit() {
        btnCancel.getUI().ifPresent(event -> event.navigate(LabBookView.class));
    }

    private void delete() {
        LabBookDto labBookDto = binder.getBean();

        if (labBookDto.getId() != null) {
            try {
                ResponseStatus status = labBookClient.delete(labBookDto.getId());
                MessagesView.showException(status.getStatus(), status.getMessage());
            } catch (JsonProcessingException ex) {
                MessagesView.showException(MessagesView.NOT_OK, MessagesView.DEFAULT_ERROR_MESSAGE);
            }
        }
        btnDelete.getUI().ifPresent(event -> event.navigate(LabBookView.class));
    }

    private void save() {
        if (binder.validate().hasErrors()) return;

        LabBookDto labBookDto = binder.getBean();
        labBookDto.setSeriesId(cmbSeries.getValue().getId());
        if (labBookDto.getId() != null) {
            update(labBookDto);
        } else {
            save(labBookDto);
        }
        btnSave.getUI().ifPresent(event -> event.navigate(LabBookView.class));
    }

    private void save(LabBookDto labBookDto) {
        try {
            ResponseStatus status = labBookClient.add(labBookDto);
            MessagesView.showException(status.getStatus(), status.getMessage());
        } catch (JsonProcessingException ex) {
            MessagesView.showException(MessagesView.NOT_OK, MessagesView.DEFAULT_ERROR_MESSAGE);
        }
    }

    private void update(LabBookDto labBookDto) {
        Map<String, String> updates = new HashMap<>();
        updates.put("title", labBookDto.getTitle());
        updates.put("description", labBookDto.getDescription());
        updates.put("conclusion", labBookDto.getConclusion());
        updates.put("density", labBookDto.getDensity().toString());
        updates.put("seriesId", labBookDto.getSeriesId().toString());
        try {
            ResponseStatus status = labBookClient.update(updates, labBookDto.getId());
            MessagesView.showException(status.getStatus(), status.getMessage());
        } catch (JsonProcessingException ex) {
            MessagesView.showException(MessagesView.NOT_OK, MessagesView.DEFAULT_ERROR_MESSAGE);
        }
    }

    private void bindFields() {
        binder.forField(title).withValidator(string -> string != null && !string.isEmpty(), "Please enter the title.")
                .bind(LabBookDto::getTitle, LabBookDto::setTitle);
        binder.forField(description).bind(LabBookDto::getDescription, LabBookDto::setDescription);
        binder.forField(conclusion).bind(LabBookDto::getConclusion, LabBookDto::setConclusion);
        binder.forField(density).withValidator(string -> string != null && !string.isEmpty(), "Please enter the density.")
                .withConverter(new StringToBigDecimalConverter("Incorrect value."))
                .bind(LabBookDto::getDensity, LabBookDto::setDensity);
        binder.forField(cmbSeries).withValidator(Objects::nonNull, "Please chose series")
                .bind(LabBookDto::getSeriesDto, LabBookDto::setSeriesDto);
    }

    private Component createButtonLayout() {
        btnSave.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        btnCancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        btnSave.addClickShortcut(Key.ENTER);
        btnCancel.addClickShortcut(Key.ESCAPE);
        return new HorizontalLayout(btnSave, btnDelete, btnCancel);
    }

    private SeriesDto getSeriesById(Long id) {
        return series.stream()
                .filter(i -> i.getId() == id)
                .findFirst().orElseGet(null);
    }
}
