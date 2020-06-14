package com.lab.labbook.ui.views.wetaher;

import com.lab.labbook.client.WeatherClient;
import com.lab.labbook.domain.WeatherDto;
import com.lab.labbook.ui.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.UploadI18N;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "weather", layout = MainLayout.class)
@PageTitle("Weather aging test")
public class WeatherView extends VerticalLayout {
    private final static String ICON_URL = "https://www.weatherbit.io/static/img/icons/";

    private final WeatherClient weatherClient = new WeatherClient();

    private Label lblTitle = new Label("Comparision of conditions to perform accelerated weather aging tests");
    private Grid<WeatherDto> grid = new Grid<>(WeatherDto.class, false);

    public WeatherView() {
        setClassName("weather-view");
        setSizeFull();

        lblTitle.setClassName("info-label");

        grid.setClassName("weather-grid");
        grid.setSizeFull();
        createGrid();

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        add(lblTitle, grid);
        refresh();
    }

    private void createGrid() {
        grid.addColumns("city_name");
        grid.addColumns("temp");
        grid.addColumns("rh");
        grid.addColumns("pres");
        grid.addColumns("wind_spd");
        grid.addColumns("clouds");
        grid.addColumns("description");

        grid.getColumnByKey("city_name").setHeader("City");
        grid.getColumnByKey("temp").setHeader("Temp [C]");
        grid.getColumnByKey("rh").setHeader("R.H. [%]");
        grid.getColumnByKey("pres").setHeader("Pressure");
        grid.getColumnByKey("wind_spd").setHeader("Wind speed");
        grid.getColumnByKey("clouds").setHeader("Clouds [%]");
        grid.getColumnByKey("description").setHeader("Description");
        grid.addColumn(new ComponentRenderer<>(i -> {
            String link = ICON_URL + i.getIcon() + ".png";
            Image image = new Image(link, "No picture");
            image.setHeight("48px");
            image.setWidth("64px");
            return image;
        })).setHeader("Weather");
    }

    private void refresh() {
        grid.setItems(weatherClient.getAll());
    }
}
