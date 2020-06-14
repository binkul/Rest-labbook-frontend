package com.lab.labbook.ui.views.register;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lab.labbook.client.UserClient;
import com.lab.labbook.client.exception.ResponseStatus;
import com.lab.labbook.config.security.PasswordEncoder;
import com.lab.labbook.domain.UserDto;
import com.lab.labbook.ui.views.common.MessagesView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "register")
@PageTitle("Registration")
public class RegisterForm extends FormLayout {

    private final PasswordEncoder encoder = new PasswordEncoder();
    private final UserClient userClient = new UserClient();

    private TextField name = new TextField("Name");
    private TextField lastName = new TextField("Last Name");
    private TextField email = new TextField("Email");
    private TextField login = new TextField("Login");
    private PasswordField password = new PasswordField("Password");
    private UserDto userDto = new UserDto();
    Binder<UserDto> binder = new Binder<>(UserDto.class);
    Button button = new Button("Register", VaadinIcon.CHECK.create());

    public RegisterForm() {
        HorizontalLayout buttons = new HorizontalLayout(button);
        binder.forField(name).withValidator(string -> string != null && !string.isEmpty(), "Please enter the name.").bind(UserDto::getName, UserDto::setName);
        binder.forField(lastName).withValidator(string -> string != null && !string.isEmpty(), "Please enter the last name.").bind(UserDto::getLastName, UserDto::setLastName);
        binder.forField(email).withValidator(string -> string != null && !string.isEmpty(), "Please enter the email.").bind(UserDto::getEmail, UserDto::setEmail);
        binder.forField(login).withValidator(string -> string != null && !string.isEmpty(), "Please enter the login.").bind(UserDto::getLogin, UserDto::setLogin);
        binder.forField(password).withValidator(string -> string != null && !string.isEmpty(), "Please enter the password.").bind(UserDto::getPassword, UserDto::setPassword);
        binder.setBean(this.userDto);

        button.addClickListener(event -> {
            if (register()) {
                buttons.getUI().ifPresent(ui -> {
                    ui.getPage().executeJs("window.open(\"http://localhost:8081/login\", \"_self\");");
                });
            }
        });

        VerticalLayout content = new VerticalLayout(name, lastName, email, login, password, buttons);
        content.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        add(content);
    }

    private boolean register() {
        if (binder.validate().hasErrors()) return false;

        UserDto userDto = binder.getBean();
        try {
            userDto.setPassword(encoder.passwordEncoder().encode(userDto.getPassword()));
            userDto.setRole("USER");
            ResponseStatus status = userClient.add(userDto);
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
