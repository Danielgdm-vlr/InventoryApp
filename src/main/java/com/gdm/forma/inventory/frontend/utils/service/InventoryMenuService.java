package com.gdm.forma.inventory.frontend.utils.service;

import com.gdm.forma.inventory.frontend.domain.model.ProductTable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class InventoryMenuService {

    public static final ProductTable MINIMUM_STOCK_0;
    public static final ProductTable MINIMUM_STOCK_1;
    public static final ProductTable MINIMUM_STOCK_2;

    public static final String rowStyleWhenProductStockIs0;
    public static final String rowStyleWhenProductStockIs1;
    public static final String rowStyleWhenProductStockIs2;

    static {
        MINIMUM_STOCK_0 = new ProductTable(0);
        MINIMUM_STOCK_1 = new ProductTable(1);
        MINIMUM_STOCK_2 = new ProductTable(2);

        rowStyleWhenProductStockIs0 =
                "-fx-background-color: #ff0400;\n" +
                        "-fx-font-size: 1.25em;\n" +
                        "-fx-font-weight: bold;\n" +
                        "-fx-text-fill: #131516;\n" +
                        "-fx-table-cell-border-color: #ff0400;\n" +
                        "-fx-border-color: #ff0400;\n" +
                        "-fx-cell-size: 5em;";
        rowStyleWhenProductStockIs1 =
                "-fx-background-color: #ff3633;\n" +
                        "-fx-font-size: 1.25em;\n" +
                        "-fx-font-weight: bold;\n" +"" +
                        "-fx-text-fill: #131516;\n" +
                        "-fx-table-cell-border-color: #ff3633;\n" +
                        "-fx-border-color: #ff3633;\n" +
                        "-fx-cell-size: 5em;";
        rowStyleWhenProductStockIs2 =
                "-fx-background-color: #ff6966;\n" +
                        "-fx-font-size: 1.25em;\n" +
                        "-fx-font-weight: bold;\n" +
                        "-fx-text-fill: #131516;\n" +
                        "-fx-table-cell-border-color: #ff6966;\n" +
                        "-fx-border-color: #ff6966;\n" +
                        "-fx-cell-size: 5em;";
    }

    public static void setTableColumnsWidth(
            TableView<ProductTable> tableViewProduct,
            TableColumn<ProductTable, Label> tableColumnProductName,
            TableColumn<ProductTable, Integer> tableColumnProductStockInSalon,
            TableColumn<ProductTable, Integer> tableColumnProductStockToBuy,
            TableColumn<ProductTable, Integer> tableColumnProductStockPerWeek,
            TableColumn<ProductTable, Integer> tableColumnProductActions,
            TableColumn<ProductTable, Button> tableColumnProductActionMinus,
            TableColumn<ProductTable, Button> tableColumnProductActionReset,
            TableColumn<ProductTable, Button> tableColumnProductActionEdit,
            TableColumn<ProductTable, Button> tableColumnProductActionDelete){

        tableViewProduct.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableColumnProductName.setMaxWidth(1f * Integer.MAX_VALUE * 20);
        tableColumnProductStockInSalon.setMaxWidth(1f * Integer.MAX_VALUE * 6);
        tableColumnProductStockToBuy.setMaxWidth(1f * Integer.MAX_VALUE * 12);
        tableColumnProductStockPerWeek.setMaxWidth(1f * Integer.MAX_VALUE * 12);
        tableColumnProductActions.setMaxWidth(1f * Integer.MAX_VALUE * 50);
        tableColumnProductActionMinus.setMaxWidth(1f * Integer.MAX_VALUE * 13);
        tableColumnProductActionReset.setMaxWidth(1f * Integer.MAX_VALUE * 13);
        tableColumnProductActionEdit.setMaxWidth(1f * Integer.MAX_VALUE * 12);
        tableColumnProductActionDelete.setMaxWidth(1f * Integer.MAX_VALUE * 12);
    }

    public static void setTableDataFromObservableList(
            TableView<ProductTable> tableViewProduct,
            TableColumn<ProductTable, Label> tableColumnProductName,
            TableColumn<ProductTable, Integer> tableColumnProductStockInSalon,
            TableColumn<ProductTable, Integer> tableColumnProductStockToBuy,
            TableColumn<ProductTable, Integer> tableColumnProductStockPerWeek,
            TableColumn<ProductTable, Button> tableColumnProductActionMinus,
            TableColumn<ProductTable, Button> tableColumnProductActionReset,
            TableColumn<ProductTable, Button> tableColumnProductActionEdit,
            TableColumn<ProductTable, Button> tableColumnProductActionDelete,
            List<ProductTable> productTableList){

        tableColumnProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnProductStockInSalon.setCellValueFactory(new PropertyValueFactory<>("stockInSalon"));
        tableColumnProductStockToBuy.setCellValueFactory(new PropertyValueFactory<>("stockToBuy"));
        tableColumnProductStockPerWeek.setCellValueFactory(new PropertyValueFactory<>("stockPerWeek"));
        tableColumnProductActionMinus.setCellValueFactory(new PropertyValueFactory<>("minusStock"));
        tableColumnProductActionReset.setCellValueFactory(new PropertyValueFactory<>("resetStock"));
        tableColumnProductActionEdit.setCellValueFactory(new PropertyValueFactory<>("editProduct"));
        tableColumnProductActionDelete.setCellValueFactory(new PropertyValueFactory<>("deleteProduct"));

        tableViewProduct.setItems(FXCollections.observableList(productTableList));

        setRowStyleByItemStock(tableViewProduct);
    }

    private static void setRowStyleByItemStock(TableView<ProductTable> tableViewProduct){

        tableViewProduct.setRowFactory(tempTableRow -> {
                    TableRow<ProductTable> row = new TableRow<>();
                    row.styleProperty().bind(Bindings.when(
                                    row.itemProperty().isEqualTo(MINIMUM_STOCK_0).and(
                                            row.itemProperty().isNotNull()))
                            .then(rowStyleWhenProductStockIs0)
                            .otherwise(Bindings.when(
                                            row.itemProperty().isEqualTo(MINIMUM_STOCK_1).and(
                                                    row.itemProperty().isNotNull()))
                                    .then(rowStyleWhenProductStockIs1)
                                    .otherwise(Bindings.when(
                                                    row.itemProperty().isEqualTo(MINIMUM_STOCK_2).and(
                                                            row.itemProperty().isNotNull()))
                                            .then(rowStyleWhenProductStockIs2)
                                            .otherwise(""))));
                    return row;
                }
        );
    }
}