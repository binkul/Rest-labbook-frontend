package com.lab.labbook.ui.views.welcome;

import com.lab.labbook.client.ActuatorClient;
import com.lab.labbook.domain.ActuatorDto;
import com.lab.labbook.ui.views.register.RegisterForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "")
@PageTitle("Welcome view")
public class WelcomeView extends VerticalLayout {

    private final ActuatorClient actuatorClient = new ActuatorClient();

    private Button btnLogin = new Button("Login");
    private Button btnRegister = new Button("Register");

    public WelcomeView() {
        ActuatorDto actuatorDto = actuatorClient.getInfo();

        btnLogin.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnRegister.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        btnLogin.addClickListener(i -> {
            btnLogin.getUI().ifPresent(ui -> {
                ui.getPage().executeJs("window.open(\"http://localhost:8081/login\", \"_self\");");
            });
        });

        btnRegister.addClickListener(i -> {
            btnRegister.getUI().ifPresent(ui -> ui.navigate(RegisterForm.class));
        });

        H1 apiName = new H1(actuatorDto.getName());
        H2 apiAuthor = new H2("by " + actuatorDto.getAuthor() + ", ver." + actuatorDto.getVersion());
        H3 apiDescription = new H3(actuatorDto.getDescription());
        H3 apiExtension = new H3(actuatorDto.getExtension());
        H4 apiPath = new H4(actuatorDto.getPath());

        HorizontalLayout toolBar = new HorizontalLayout(btnLogin, btnRegister);
        VerticalLayout content = new VerticalLayout(apiName, apiAuthor, apiDescription, apiExtension, apiPath);
        content.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(toolBar, content);
    }
}
