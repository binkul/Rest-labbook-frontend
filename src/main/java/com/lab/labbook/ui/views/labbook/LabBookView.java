package com.lab.labbook.ui.views.labbook;

import com.lab.labbook.client.LabBookClient;
import com.lab.labbook.client.SeriesClient;
import com.lab.labbook.client.UserClient;
import com.lab.labbook.domain.LabBookDto;
import com.lab.labbook.domain.SeriesDto;
import com.lab.labbook.domain.UserDto;
import com.lab.labbook.ui.MainLayout;
import com.lab.labbook.ui.views.recipe.RecipeView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;

@Route(value = "labbook", layout = MainLayout.class)
@PageTitle("Laboratory Book")
public class LabBookView extends VerticalLayout {

    private Grid<LabBookDto> grid = new Grid<>(LabBookDto.class, false);
    private TextField txtFindTitle = new TextField();
    private Button btnAddNew = new Button("Add new");
    private Label lblInfo = new Label("");

    private LabBookClient labBookClient = new LabBookClient();
    private SeriesClient seriesClient = new SeriesClient();
    private UserClient userClient = new UserClient();
    private List<SeriesDto> series = seriesClient.getSeries("");
    private UserDto mainUser;

    public LabBookView() {
        setClassName("labbok-view");
        lblInfo.addClassName("info-label");

        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        mainUser = userClient.getByLogin(login);

        setSizeFull();
        configureGrid();
        configureTitleFilter();

        btnAddNew.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnAddNew.addClickListener(e -> addNewLabBook());

        HorizontalLayout mainContent = new HorizontalLayout(grid);
        HorizontalLayout toolBar = new HorizontalLayout(txtFindTitle, btnAddNew, lblInfo);
        mainContent.setSizeFull();

        add(toolBar, mainContent);
        refresh();
    }

    public void refresh() {
        lblInfo.setText("User: " + mainUser.getName() + " " + mainUser.getLastName());
        grid.setItems(labBookClient.getByUser(mainUser.getId(), txtFindTitle.getValue()));
    }

    private void addNewLabBook() {
        grid.asSingleSelect().clear();
        btnAddNew.getUI().flatMap(event -> grid.getUI()).ifPresent(i -> i.navigate(LabBookForm.class, mainUser.getId() + "_0"));
    }

    private void configureGrid() {
        grid.setClassName("labbok-grid");
        grid.setSizeFull();

        grid.addColumns("id");
        addUserColumn();
        grid.addColumns("title");
        grid.addColumn(labBookDto -> getSeriesById(labBookDto.getSeriesId()).getTitle()).setHeader("Series");
        grid.addColumn(labBookDto -> LocalDate.of(
                labBookDto.getCreatedDate().getYear(),
                labBookDto.getCreatedDate().getMonth(),
                labBookDto.getCreatedDate().getDayOfMonth())).setHeader("Date");
        grid.addColumns("status", "density");
        addRecipeColumn();

        grid.getColumnByKey("id").setHeader("Nr");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> {
            grid.getUI().ifPresent(i -> i.navigate(LabBookForm.class, mainUser.getId() + "_" + event.getValue().getId().toString()));
        });
    }

    private void configureTitleFilter() {
        txtFindTitle.setPlaceholder("Filter by title ...");
        txtFindTitle.setClearButtonVisible(true);
        txtFindTitle.setValueChangeMode(ValueChangeMode.LAZY);
        txtFindTitle.addValueChangeListener(i -> refresh());
    }

    private void addUserColumn() {
        if (mainUser.getRole().equals("ADMIN")) {
            List<UserDto> users = userClient.getUsers("");
            grid.addColumn(labBookDto -> {
                String name = getUsersById(users, labBookDto.getUserId()).getName();
                if (!name.equals("default")) {
                    name += " ";
                    name += getUsersById(users, labBookDto.getUserId()).getLastName();
                }
                return name;
            }).setHeader("User");
        }
    }

    private void addRecipeColumn() {
        grid.addComponentColumn(i -> {
            Button button = new Button("Rec ->");
            String link = i.getId().toString() + "-" + i.getDensity().toString();
            RouterLink routerLink = new RouterLink("", RecipeView.class, link);
            routerLink.getElement().appendChild(button.getElement());
            return routerLink;
        }).setHeader("Recipe");
    }

    private SeriesDto getSeriesById(Long id) {
        return series.stream()
                .filter(i -> i.getId() == id)
                .findFirst().orElseGet(null);
    }

    private UserDto getUsersById(List<UserDto> users, Long id) {
        return users.stream()
                .filter(i -> i.getId() == id)
                .findFirst().orElseGet(null);
    }
}

