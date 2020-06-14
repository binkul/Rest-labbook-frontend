package com.lab.labbook.ui.views.info;

import com.lab.labbook.client.ActuatorClient;
import com.lab.labbook.domain.ActuatorDto;
import com.lab.labbook.ui.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "info", layout = MainLayout.class)
@PageTitle("Info")
public class InfoView extends VerticalLayout {

    private final ActuatorClient actuatorClient = new ActuatorClient();

    public InfoView() {
        ActuatorDto actuatorDto = actuatorClient.getInfo();

        H1 apiName = new H1(actuatorDto.getName());
        H2 apiAuthor = new H2("by " + actuatorDto.getAuthor() + ", ver." + actuatorDto.getVersion());
        H3 apiDescription = new H3(actuatorDto.getDescription());
        H3 apiExtension = new H3(actuatorDto.getExtension());
        H4 apiPath = new H4(actuatorDto.getPath());

        VerticalLayout content = new VerticalLayout(apiName, apiAuthor, apiDescription, apiExtension, apiPath);
        content.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(content);
    }
}
