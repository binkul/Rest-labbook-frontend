package com.lab.labbook.ui.views.material;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lab.labbook.client.MaterialClient;
import com.lab.labbook.client.exception.ResponseStatus;
import com.lab.labbook.domain.GhsDto;
import com.lab.labbook.domain.MaterialDto;
import com.lab.labbook.ui.MainLayout;
import com.lab.labbook.ui.views.common.MessagesView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.Map;

@Route(value = "clp", layout = MainLayout.class)
@PageTitle("Safety data sheet")
public class ClpDataForm extends FormLayout implements HasUrlParameter<String> {

    private final MaterialClient materialClient = new MaterialClient();
    private MaterialDto material;

    private Label lblName = new Label();
    private Label lblGhs = new Label("GHS settings");
    private Button btnExit = new Button("Back ->");
    private Checkbox chbGhs01 = new Checkbox();
    private Image imgGhs01 = new Image("img/ghs01.png", "Explosion");
    private Checkbox chbGhs02 = new Checkbox();
    private Image imgGhs02 = new Image("img/ghs02.png", "Flammable");
    private Checkbox chbGhs03 = new Checkbox();
    private Image imgGhs03 = new Image("img/ghs03.png", "Oxidants");
    private Checkbox chbGhs04 = new Checkbox();
    private Image imgGhs04 = new Image("img/ghs04.png", "Compressed gas");
    private Checkbox chbGhs05 = new Checkbox();
    private Image imgGhs05 = new Image("img/ghs05.png", "Corrosive");
    private Checkbox chbGhs06 = new Checkbox();
    private Image imgGhs06 = new Image("img/ghs06.png", "Toxic");
    private Checkbox chbGhs07 = new Checkbox();
    private Image imgGhs07 = new Image("img/ghs07.png", "Harmful");
    private Checkbox chbGhs08 = new Checkbox();
    private Image imgGhs08 = new Image("img/ghs08.png", "Mutagen");
    private Checkbox chbGhs09 = new Checkbox();
    private Image imgGhs09 = new Image("img/ghs09.png", "Toxic to environment");

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        material = materialClient.getMaterial(Long.parseLong(parameter));
        lblName.setText(material.getName());
        refresh();
    }

    public ClpDataForm() {
        addClassName("clp-form");

        setImages();
        setBoxesStyle();

        HorizontalLayout layout_top = new HorizontalLayout(imgGhs01, chbGhs01, imgGhs02, chbGhs02, imgGhs03, chbGhs03, imgGhs04, chbGhs04,
                imgGhs05, chbGhs05);
        HorizontalLayout layout_bottom = new HorizontalLayout(imgGhs06, chbGhs06, imgGhs07, chbGhs07, imgGhs08, chbGhs08, imgGhs09, chbGhs09);
        layout_top.setAlignItems(FlexComponent.Alignment.END);
        layout_bottom.setAlignItems(FlexComponent.Alignment.END);

        lblName.addClassName("info-label");
        lblGhs.addClassName("red-label");

        btnExit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnExit.addClickShortcut(Key.ENTER);
        btnExit.addClickListener(event -> btnExit.getUI().ifPresent(i -> i.navigate(MaterialView.class)));

        VerticalLayout location = new VerticalLayout(lblName, lblGhs, layout_top, layout_bottom, btnExit);
        add(location);
    }

    private void setBoxesStyle() {
        chbGhs01.addClassName("boxes-style");
        chbGhs02.addClassName("boxes-style");
        chbGhs03.addClassName("boxes-style");
        chbGhs04.addClassName("boxes-style");
        chbGhs05.addClassName("boxes-style");
        chbGhs06.addClassName("boxes-style");
        chbGhs07.addClassName("boxes-style");
        chbGhs08.addClassName("boxes-style");
        chbGhs09.addClassName("boxes-style");
    }

    private void setImages() {
        imgGhs01.setWidth("64px");
        imgGhs01.setHeight("64px");
        imgGhs02.setWidth("64px");
        imgGhs02.setHeight("64px");
        imgGhs03.setWidth("64px");
        imgGhs03.setHeight("64px");
        imgGhs04.setWidth("64px");
        imgGhs04.setHeight("64px");
        imgGhs05.setWidth("64px");
        imgGhs05.setHeight("64px");
        imgGhs06.setWidth("64px");
        imgGhs06.setHeight("64px");
        imgGhs07.setWidth("64px");
        imgGhs07.setHeight("64px");
        imgGhs08.setWidth("64px");
        imgGhs08.setHeight("64px");
        imgGhs09.setWidth("64px");
        imgGhs09.setHeight("64px");
    }

    private void refresh() {
        Map<String, String> symbols = material.getSymbols();
        if (symbols.containsKey("GHS01")) {
            chbGhs01.setValue(true);
        }
        if (symbols.containsKey("GHS02")) {
            chbGhs02.setValue(true);
        }
        if (symbols.containsKey("GHS03")) {
            chbGhs03.setValue(true);
        }
        if (symbols.containsKey("GHS04")) {
            chbGhs04.setValue(true);
        }
        if (symbols.containsKey("GHS05")) {
            chbGhs05.setValue(true);
        }
        if (symbols.containsKey("GHS06")) {
            chbGhs06.setValue(true);
        }
        if (symbols.containsKey("GHS07")) {
            chbGhs07.setValue(true);
        }
        if (symbols.containsKey("GHS08")) {
            chbGhs08.setValue(true);
        }
        if (symbols.containsKey("GHS09")) {
            chbGhs09.setValue(true);
        }
        setBoxesEvents();
    }

    private void setBoxesEvents() {
        chbGhs01.addValueChangeListener(i -> changeValue(1, i.getValue()));
        chbGhs02.addValueChangeListener(i -> changeValue(2, i.getValue()));
        chbGhs03.addValueChangeListener(i -> changeValue(3, i.getValue()));
        chbGhs04.addValueChangeListener(i -> changeValue(4, i.getValue()));
        chbGhs05.addValueChangeListener(i -> changeValue(5, i.getValue()));
        chbGhs06.addValueChangeListener(i -> changeValue(6, i.getValue()));
        chbGhs07.addValueChangeListener(i -> changeValue(7, i.getValue()));
        chbGhs08.addValueChangeListener(i -> changeValue(8, i.getValue()));
        chbGhs09.addValueChangeListener(i -> changeValue(9, i.getValue()));
    }

    private void changeValue(int number, boolean value) {
        GhsDto ghsDto = new GhsDto("GHS0" + number, value);
        try {
            ResponseStatus status = materialClient.updateClp(material.getId(), ghsDto);
            MessagesView.showException(status.getStatus(), status.getMessage());
        } catch (JsonProcessingException ex) {
            MessagesView.showException(MessagesView.NOT_OK, MessagesView.DEFAULT_ERROR_MESSAGE);
        }
    }
}

