package com.lab.labbook.ui;

import com.lab.labbook.client.UserClient;
import com.lab.labbook.domain.UserDto;
import com.lab.labbook.ui.views.account.MyCountForm;
import com.lab.labbook.ui.views.currency.CurrencyView;
import com.lab.labbook.ui.views.info.InfoView;
import com.lab.labbook.ui.views.labbook.LabBookView;
import com.lab.labbook.ui.views.log.LogView;
import com.lab.labbook.ui.views.material.MaterialView;
import com.lab.labbook.ui.views.series.SeriesView;
import com.lab.labbook.ui.views.supplier.SupplierView;
import com.lab.labbook.ui.views.user.UserView;
import com.lab.labbook.ui.views.wetaher.WeatherView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;
import org.springframework.security.core.context.SecurityContextHolder;

@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {

    private UserDto mainUser;

    public MainLayout() {
        createUser();
        createHeader();
        createDrawer();
    }

    private void createUser() {
        UserClient userClient = new UserClient();
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        mainUser = userClient.getByLogin(login);
    }

    private void createHeader() {
        H1 logo = new H1("Laboratory work");
        logo.addClassName("logo");

        Anchor logout = new Anchor("logout", "Log out");
        logout.addClassName("logout-style");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logout);
        header.expand(logo);
        header.addClassName("header");
        header.setWidth("100%");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink labBookLink = new RouterLink("Laboratory Book", LabBookView.class);
        labBookLink.setHighlightCondition(HighlightConditions.sameLocation());
        RouterLink materialLink = new RouterLink("Raw materials", MaterialView.class);
        RouterLink supplierLink = new RouterLink("Raw materials suppliers", SupplierView.class);
        RouterLink seriesLink = new RouterLink("Series", SeriesView.class);
        RouterLink userLink = new RouterLink("Users", UserView.class);
        RouterLink currencyLink = new RouterLink("Currency", CurrencyView.class);
        RouterLink weatherTestLink = new RouterLink("Weather aging tests", WeatherView.class);
        RouterLink myCountLink = new RouterLink("My account", MyCountForm.class);
        RouterLink logLink = new RouterLink("Logs", LogView.class);
        RouterLink infoLink = new RouterLink("About", InfoView.class);

        if (mainUser.getRole().equals("ADMIN")) {
            addToDrawer(new VerticalLayout(
                    labBookLink,
                    materialLink,
                    supplierLink,
                    seriesLink,
                    currencyLink,
                    weatherTestLink,
                    userLink,
                    myCountLink,
                    logLink,
                    infoLink
            ));
        } else {
            addToDrawer(new VerticalLayout(
                    labBookLink,
                    materialLink,
                    supplierLink,
                    seriesLink,
                    currencyLink,
                    weatherTestLink,
                    myCountLink,
                    infoLink
            ));
        }
    }
}
