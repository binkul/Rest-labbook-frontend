package com.lab.labbook.ui.views.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lab.labbook.client.UserClient;
import com.lab.labbook.client.exception.ResponseStatus;
import com.lab.labbook.domain.Role;
import com.lab.labbook.domain.UserDto;
import com.lab.labbook.ui.MainLayout;
import com.lab.labbook.ui.views.common.MessagesView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;

@Route(value = "users", layout = MainLayout.class)
@PageTitle("Users")
public class UserView extends VerticalLayout {

    private final UserForm form;
    private Grid<UserDto> grid = new Grid<>(UserDto.class, false);
    private TextField txtFindTitle = new TextField();

    private UserClient userClient = new UserClient();

    public UserView() {
        addClassName("user_view");

        setSizeFull();
        configureGrid();
        configureUserFilter();

        form = new UserForm(this);
        form.setUser(null);

        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        HorizontalLayout toolBar = new HorizontalLayout(txtFindTitle);
        mainContent.setSizeFull();

        add(toolBar, mainContent);
        refresh();
    }

    public void refresh() {
        grid.setItems(userClient.getUsers(txtFindTitle.getValue()));
    }

    private void configureUserFilter() {
        txtFindTitle.setPlaceholder("Filter by name ...");
        txtFindTitle.setClearButtonVisible(true);
        txtFindTitle.setValueChangeMode(ValueChangeMode.LAZY);
        txtFindTitle.addValueChangeListener(i -> refresh());
    }

    private void configureGrid() {
        grid.setClassName("user-grid");
        grid.setSizeFull();

        grid.addColumns("name");
        grid.addColumns("lastName");
        grid.addColumns("login");
        grid.addColumns("email");
        grid.addColumn(i -> {
            if (i.isBlocked()) return  "Yes";
            return "No";
        }).setHeader("Is blocked");
        grid.addColumn(i -> {
            if (i.isObserver()) return "Yes";
            return "No";
        }).setHeader("Is observer");
        grid.addColumns("role");
        grid.addColumn(user -> LocalDate.of(
                user.getDate().getYear(),
                user.getDate().getMonth(),
                user.getDate().getDayOfMonth())).setHeader("Date");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> selectRow());
    }

    private void selectRow() {
        UserDto userDto = grid.asSingleSelect().getValue();
        if (userDto != null) {
            userDto.setRoleType(Role.findRole(userDto.getRole()));
        }
        form.setUser(userDto);
    }

    public boolean save(UserDto userDto) {
        UserDto userBefore = userClient.getByLogin(userDto.getLogin());
        userDto.setPassword(userBefore.getPassword());
        userDto.setDate(userBefore.getDate());
        try {
            ResponseStatus status = userClient.update(userDto);
            return MessagesView.showException(status.getStatus(), status.getMessage());
        } catch (JsonProcessingException ex) {
            MessagesView.showException(MessagesView.NOT_OK, MessagesView.DEFAULT_ERROR_MESSAGE);
        }
        return false;
    }

    public boolean delete(Long id) {
        try {
            ResponseStatus status = userClient.delete(id);;
            return MessagesView.showException(status.getStatus(), status.getMessage());
        } catch (JsonProcessingException ex) {
            MessagesView.showException(MessagesView.NOT_OK, MessagesView.DEFAULT_ERROR_MESSAGE);
        }
        return false;
    }
}
