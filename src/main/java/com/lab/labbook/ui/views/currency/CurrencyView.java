package com.lab.labbook.ui.views.currency;

import com.lab.labbook.client.CurrencyClient;
import com.lab.labbook.domain.CurrencyDto;
import com.lab.labbook.ui.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "currency", layout = MainLayout.class)
@PageTitle("Currency")
public class CurrencyView extends VerticalLayout {

    private final CurrencyClient currencyClient = new CurrencyClient();

    private Grid<CurrencyDto> grid = new Grid<>(CurrencyDto.class, false);

    public CurrencyView() {
        setClassName("currency-view");
        setSizeFull();

        grid.setClassName("currency-grid");
        grid.setSizeFull();

        grid.addColumns("symbol");
        grid.addColumns("exchange");
        grid.addColumns("commercial");
        grid.getColumnByKey("exchange").setHeader("NBP rate");
        grid.getColumnByKey("commercial").setHeader("Commercial rate");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        add(grid);
        refresh();
    }

    private void refresh() {
        grid.setItems(currencyClient.getAll());
    }
}
