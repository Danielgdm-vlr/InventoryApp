package com.gdm.formainventoryapp.frontend.components.mainmenu;

import com.gdm.formainventoryapp.backend.model.entity.Product;
import com.gdm.formainventoryapp.backend.service.impl.ProductServiceImpl;
import com.gdm.formainventoryapp.frontend.model.ProductTable;
import com.gdm.formainventoryapp.frontend.utils.alert.CustomAlert;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class InventoryMenuController{

    @FXML
    private TextField textFieldSearchProduct;
    @FXML
    private Button buttonSearch, buttonAddProduct, buttonResetAllStocks, buttonDeleteAll;
    @FXML
    private TableView<ProductTable> tableViewProduct;
    @FXML
    private TableColumn<ProductTable, Label> tableColumnProductName;
    @FXML
    private TableColumn<ProductTable, Integer> tableColumnProductStockInSalon,
            tableColumnProductStockToBuy, tableColumnProductStockPerWeek, tableColumnProductActions;
    @FXML
    private TableColumn<ProductTable, Button> tableColumnProductActionMinus, tableColumnProductActionReset,
            tableColumnProductActionEdit, tableColumnProductActionDelete;

    private @Getter @Setter FXMLLoader fxmlLoader;
    private @Getter @Setter Stage inventoryMenuStage;

    private @Getter @Setter ProductServiceImpl productService;

    private List<ProductTable> productTableList = new ArrayList<>();

    public static final ProductTable MINIMUM_STOCK_0 = new ProductTable(0);
    public static final ProductTable MINIMUM_STOCK_1 = new ProductTable(1);
    public static final ProductTable MINIMUM_STOCK_2 = new ProductTable(2);

    private static final String rowStyleWhenProductStockIs0 =
            "-fx-background-color: #ff0400;\n" +
            "-fx-font-size: 1.25em;\n" +
            "-fx-font-weight: bold;\n" +
            "-fx-text-fill: #131516;\n" +
            "-fx-table-cell-border-color: #ff0400;\n" +
            "-fx-border-color: #ff0400;\n" +
            "-fx-cell-size: 5em;";
    private static final String rowStyleWhenProductStockIs1 =
            "-fx-background-color: #ff3633;\n" +
            "-fx-font-size: 1.25em;\n" +
            "-fx-font-weight: bold;\n" +"" +
            "-fx-text-fill: #131516;\n" +
            "-fx-table-cell-border-color: #ff3633;\n" +
            "-fx-border-color: #ff3633;\n" +
            "-fx-cell-size: 5em;";
    private static final String rowStyleWhenProductStockIs2 =
            "-fx-background-color: #ff6966;\n" +
            "-fx-font-size: 1.25em;\n" +
            "-fx-font-weight: bold;\n" +
            "-fx-text-fill: #131516;\n" +
            "-fx-table-cell-border-color: #ff6966;\n" +
            "-fx-border-color: #ff6966;\n" +
            "-fx-cell-size: 5em;";

    public void init() {

        //method only for demo project
        initDataBaseData();

        initTableList();
        setTableColumnsWidth();
        setTableColumnsDataFromList();

        initTableButtons();
    }

    private void initTableList() {
        List<Product> productList = getAllProducts();
        int size = productList.size();
        ProductTable productTable;
        Product product;

        for (int tableRow = 0; tableRow < size; tableRow++) {

            product = productList.get(tableRow);

            Button minusButton = initTableRowButton("minus", tableRow);
            Button resetButton = initTableRowButton("reset", tableRow);
            Button buttonEdit = initTableRowButton("edit", tableRow);
            Button buttonDelete = initTableRowButton("delete", tableRow);

            Label labelName = new Label();
            labelName.setText(product.getName());
//            labelName.setStyle("-fx-text-fill: #131516");
            labelName.setWrapText(true);

//            if (labelName.getText().length() > 14) {
//                labelName.setText(String.format("%s\n%s",
//                        labelName.getText().substring(0, 10),
//                        labelName.getText().substring(10)));
////                labelName.setStyle("-fx-font-size: 0.75em; -fx-text-fill: #131516");
//            }

            productTable = new ProductTable(
                    product.getId(),
                    labelName,
                    product.getStockInSalon(),
                    (product.getStockPerWeek() - product.getStockInSalon()),
                    product.getStockPerWeek(),
                    minusButton,
                    resetButton,
                    buttonEdit,
                    buttonDelete
            );

            productTableList.add(productTable);
        }
    }

    private Button initTableRowButton(String imageName, int tableRow){

        Image image = new Image(String.format(
                "/com/gdm/formainventoryapp/frontend/images/%s.png",
                imageName));
        ImageView imageView = new ImageView(image);

        Button button = new Button();
        button.setGraphic(imageView);

        button.setOnAction(actionEvent -> {
            switch (imageName){
                case "minus":
                    removeUnits(tableRow);
                    break;
                case "reset":
                    resetQuantity(tableRow);
                    break;
                case "edit":
                    editProduct(tableRow);
                    break;
                case "delete":
                    deleteProduct(tableRow);
                    break;
            }
        });

        return button;
    }


    private void removeUnits(int tableRow) {

        // TODO: confirmation before action
        Product product = getProductFromTable(tableRow);

        if (product.getStockInSalon() > 0) {
            product.setStockInSalon(product.getStockInSalon() - 1);
            productService.update(product);
        } else {
            // TODO: alert that stock is 0 and cant be negative
        }

        refreshTable();
    }

    private void resetQuantity(int tableRow){

        // TODO: alert if no products are in table

        if(tableRow != Integer.MIN_VALUE) {
            Product product = getProductFromTable(tableRow);
            product.setStockInSalon(product.getStockPerWeek());
            productService.update(product);
            productTableList.clear();
            initTableList();
            refreshTableData();
        }
        else{
            List<Product> productList = getAllProducts();
            productList.forEach(product -> {
                product.setStockInSalon(product.getStockPerWeek());
                productService.update(product);
            });

            productTableList.clear();
            initTableList();
            refreshTableData();
        }
    }

    private void editProduct(int tableRow){

        // TODO: edit product action from table button, with confirmation
        openAddOrEditPage("edit", tableRow);
    }

    private void deleteProduct(int tableRow){

        // TODO: alert if no products are in table

//        if(tableRow != Integer.MIN_VALUE) {
//            Product product = getProductFromTable(tableRow);
//                product.setStockInSalon(product.getStockPerWeek());
//                productService.delete(product);
//
//                productTableList.clear();
//                initTableList();
//                refreshTableData();
//        }
//        else{
//
//            List<Product> productList = getAllProducts();
//            productList.forEach(product -> {
//                product.setStockInSalon(product.getStockPerWeek());
//                productService.truncate();
//            });
//
//            productTableList.clear();
//            initTableList();
//            refreshTableData();
//        }

        CustomAlert.showAlert(Alert.AlertType.CONFIRMATION, "dar asta merge?");
    }

    private Product getProductFromTable(int tableRow){

        ProductTable tableProduct = getProductTableFromTable(tableRow);

        Product product = new Product(
                tableProduct.getName().getText(),
                tableProduct.getStockInSalon(),
                tableProduct.getStockPerWeek()
        );
        product.setId(tableProduct.getId());

        return product;
    }

    private ProductTable getProductTableFromTable(int tableRow){

        return tableViewProduct.getItems().get(tableRow);
    }

    private void refreshTable(){

        productTableList.clear();
        initTableList();
        refreshTableData();
    }

    private void refreshTableData(){

        tableViewProduct.setItems(FXCollections.observableList(productTableList));}

    private void refreshTableData(List<ProductTable> productTableList){

        tableViewProduct.setItems(FXCollections.observableList(productTableList));
    }

    private void setTableColumnsWidth(){

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

    private void setTableColumnsDataFromList(){

        tableColumnProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnProductStockInSalon.setCellValueFactory(new PropertyValueFactory<>("stockInSalon"));
        tableColumnProductStockToBuy.setCellValueFactory(new PropertyValueFactory<>("stockToBuy"));
        tableColumnProductStockPerWeek.setCellValueFactory(new PropertyValueFactory<>("stockPerWeek"));
        tableColumnProductActionMinus.setCellValueFactory(new PropertyValueFactory<>("minusStock"));
        tableColumnProductActionReset.setCellValueFactory(new PropertyValueFactory<>("resetStock"));
        tableColumnProductActionEdit.setCellValueFactory(new PropertyValueFactory<>("editProduct"));
        tableColumnProductActionDelete.setCellValueFactory(new PropertyValueFactory<>("deleteProduct"));

        tableViewProduct.setItems(FXCollections.observableList(productTableList));

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

    private void initTableButtons(){

        initTableOptionsButtons(buttonSearch,"search");
        initTableOptionsButtons(buttonAddProduct,"addProduct");

        initTableOptionsButtons(buttonResetAllStocks,"reset");
        initTableOptionsButtons(buttonDeleteAll,"delete");
    }

    private void initTableOptionsButtons(Button button, String imageName){

        Image image = new Image(String.format(
                "/com/gdm/formainventoryapp/frontend/images/%s.png",
                imageName));
        ImageView imageView = new ImageView(image);

        button.setGraphic(imageView);

        button.setOnMouseClicked(actionEvent ->{
            switch (imageName){
                case "search":
                    // TODO: either a search product method or a keyReleasedProperty that searched for any product
                    //  "like %textFieldValue"
                    System.out.println("search");
                    break;
                case "addProduct":
                    addProduct();
                    break;
                case "reset":
                    resetQuantity(Integer.MIN_VALUE);
                    break;
                case "delete":
                    deleteProduct(Integer.MIN_VALUE);
                    break;
            }
        });
    }

    private void addProduct(){

        Alert alert = CustomAlert.getAlert(Alert.AlertType.CONFIRMATION, "merge?");
        alert.show();
        System.out.println("Ar trebui sa porneasca alerta");
    }

    private void openAddOrEditPage(String actions, int tableRow){

    }

    @FXML
    private void onKeyReleasedSearch(){

        List<ProductTable> tempProductList = new ArrayList<>();

        String inputSearchTextFromUser = textFieldSearchProduct.getText().toLowerCase();

        productTableList.forEach(productTable -> {
            if(productTable.getName().getText().toLowerCase().contains(inputSearchTextFromUser)){
                tempProductList.add(productTable);
            }
        });

        refreshTableData(tempProductList);
    }

    private List<Product> getAllProducts(){

        return productService.getAll();
    }

    //method only for demo project
    private void initDataBaseData(){

        List<Product> productList = new ArrayList<>();
        List<String> productNames = getProductNamesFromFile();

        productNames
                .forEach(productName -> productList.add(new Product(
                        productName,
                        3,
                        7
                )));

        productService.saveOrUpdateAllList(productList);
    }

    //method only for demo project
    private List<String> getProductNamesFromFile(){

        Path path = Path.of("src/main/resources/db/products.txt");

        List<String> productNames = new ArrayList<>();

        try(Stream<String> productsFileLines = Files.lines(path)) {
            productsFileLines.forEach(productNames::add);
        }catch (Exception exception){
            exception.printStackTrace();
        }

        return productNames;
    }
}