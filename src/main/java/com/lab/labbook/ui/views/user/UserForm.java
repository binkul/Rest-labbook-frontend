package com.lab.labbook.ui.views.user;

import com.lab.labbook.domain.Role;
import com.lab.labbook.domain.UserDto;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import java.util.Objects;

public class UserForm extends FormLayout {

    private final UserView userView;

    private Label lblLogin = new Label();
    private TextField name = new TextField("Name");
    private TextField lastName = new TextField("Last name");
    private TextField email = new TextField("Name");
    private Checkbox blocked = new Checkbox("Is blocked");
    private Checkbox observer = new Checkbox("Is observer");
    private ComboBox<Role> cmbRole = new ComboBox<>("Role");

    private Button btnSave = new Button("Save");
    private Button btnDelete = new Button("Delete");
    private Button btnCancel = new Button("Cancel");

    private Binder<UserDto> binder = new Binder<>(UserDto.class);

    public UserForm(UserView userView) {
        addClassName("user-form");
        this.userView = userView;

        bindField();

        cmbRole.setItems(Role.values());
        lblLogin.setClassName("normal-label");

        btnSave.addClickListener(i -> save());
        btnDelete.addClickListener(i -> delete());
        btnCancel.addClickListener(i -> cancel());

        HorizontalLayout checkLayout = new HorizontalLayout(blocked, observer);

        VerticalLayout formLayout = new VerticalLayout(name, lastName, email, checkLayout, cmbRole, createButtonsLayout());
        add(formLayout);
    }

    public void setUser(UserDto userDto) {
        binder.setBean(userDto);

        if (userDto == null) {
            setVisible(false);
        } else {
            lblLogin.setText(userDto.getLogin());
            setVisible(true);
            name.focus();
        }
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
        setUser(null);
    }

    private void delete() {
        UserDto userDto = binder.getBean();
        if (userView.delete(userDto.getId())) {
            setVisible(false);
            userView.refresh();
        }
    }

    private void save() {
        if (binder.validate().hasErrors()) return;

        UserDto userDto = binder.getBean();
        userDto.setRole(cmbRole.getValue().name());
        if (userView.save(userDto)) {
            setVisible(false);
            userView.refresh();
        }
    }

    private void bindField() {
        binder.forField(name).withValidator(string -> string != null && !string.isEmpty(), "Please enter the Name.")
                .bind(UserDto::getName, UserDto::setName);
        binder.forField(lastName).withValidator(string -> string != null && !string.isEmpty(), "Please enter the Last name.")
                .bind(UserDto::getLastName, UserDto::setLastName);
        binder.forField(email).withValidator(string -> string != null && !string.isEmpty(), "Please enter the valid e-mail.")
                .bind(UserDto::getEmail, UserDto::setEmail);
        binder.forField(blocked).bind(UserDto::isBlocked, UserDto::setBlocked);
        binder.forField(observer).bind(UserDto::isObserver, UserDto::setObserver);
        binder.forField(cmbRole).withValidator(Objects::nonNull, "Please chose role")
                .bind(UserDto::getRoleType, UserDto::setRoleType);
    }
}
