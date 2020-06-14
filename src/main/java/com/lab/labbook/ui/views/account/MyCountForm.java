package com.lab.labbook.ui.views.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lab.labbook.client.UserClient;
import com.lab.labbook.client.exception.ResponseStatus;
import com.lab.labbook.config.security.PasswordEncoder;
import com.lab.labbook.domain.UserDto;
import com.lab.labbook.ui.MainLayout;
import com.lab.labbook.ui.views.common.MessagesView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.context.SecurityContextHolder;

@Route(value = "account", layout = MainLayout.class)
@PageTitle("My account")
public class MyCountForm extends FormLayout {

    private final PasswordEncoder encoder = new PasswordEncoder();
    private final UserClient userClient = new UserClient();
    private final UserDto mainUser;

    private Label oldLogin = new Label();
    private TextField name = new TextField("Name");
    private TextField lastName = new TextField("Last Name");
    private TextField email = new TextField("Email");
    private PasswordField password = new PasswordField("New password");
    Binder<UserDto> binder = new Binder<>(UserDto.class);
    Button btnUpdate = new Button("Update", VaadinIcon.CHECK.create());
    Button btnCancel = new Button("Cancel", VaadinIcon.CHECK.create());

    public MyCountForm() {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        mainUser = userClient.getByLogin(login);
        mainUser.setPassword("");

        oldLogin.setText(mainUser.getLogin());
        oldLogin.addClassName("normal-label");
        createBinder();
        createButtons();

        HorizontalLayout buttons = new HorizontalLayout(btnUpdate, btnCancel);
        VerticalLayout content = new VerticalLayout(oldLogin, name, lastName, email, password, buttons);
        content.expand(name);
        content.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        add(content);
    }

    private void createButtons() {
        btnUpdate.addClickListener(event -> {
            if (update()) {
                btnUpdate.getUI().ifPresent(ui -> {
                    ui.getPage().executeJs("window.open(\"http://localhost:8081/logout\", \"_self\");");
                });
            }
        });

        btnCancel.addClickListener(event -> {
            btnCancel.getUI().ifPresent(ui -> {
                ui.getPage().executeJs("window.open(\"http://localhost:8081/labbook\", \"_self\");");
            });
        });
    }

    private void createBinder() {
        binder.forField(name).withValidator(string -> string != null && !string.isEmpty(), "Please enter the name.").bind(UserDto::getName, UserDto::setName);
        binder.forField(lastName).withValidator(string -> string != null && !string.isEmpty(), "Please enter the last name.").bind(UserDto::getLastName, UserDto::setLastName);
        binder.forField(email).withValidator(string -> string != null && !string.isEmpty(), "Please enter the email.").bind(UserDto::getEmail, UserDto::setEmail);
        binder.forField(password).withValidator(string -> string != null && !string.isEmpty(), "Please enter the password.").bind(UserDto::getPassword, UserDto::setPassword);
        binder.setBean(this.mainUser);
    }

    private boolean update() {
        if (binder.validate().hasErrors()) return false;

        UserDto userDto = binder.getBean();
        try {
            userDto.setPassword(encoder.passwordEncoder().encode(userDto.getPassword()));
            ResponseStatus status = userClient.update(userDto);
            if (status.getStatus().equals("200")) {
                return true;
            } else {
                MessagesView.showException(status.getStatus(), status.getMessage());
            }
        } catch (JsonProcessingException ex) {
            MessagesView.showException(MessagesView.NOT_OK, MessagesView.DEFAULT_ERROR_MESSAGE);
        }
        return false;
    }
}
