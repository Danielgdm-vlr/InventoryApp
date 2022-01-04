package com.gdm.formainventoryapp.frontend.components.mainmenu;

import com.gdm.formainventoryapp.app.utils.logging.CustomLogger;
import com.gdm.formainventoryapp.backend.model.entity.Product;
import com.gdm.formainventoryapp.backend.service.impl.ProductServiceImpl;
import com.gdm.formainventoryapp.frontend.model.ProductTable;
import com.gdm.formainventoryapp.frontend.utils.alert.CustomAlert;
import com.gdm.formainventoryapp.frontend.utils.service.DatabaseService;
import com.gdm.formainventoryapp.frontend.utils.service.InventoryMenuService;
import com.gdm.formainventoryapp.frontend.utils.service.InventoryMenuTableService;
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

    private @Setter InventoryMenuService inventoryMenuService;

    private List<ProductTable> productTableList = new ArrayList<>();

    public void init() {

        //method only for demo project
        initDataBaseData();

        initTableList();
        setTableColumnsWidth();
        setTableDataFromList();

        initTableButtons();
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

    private void addProduct(){

        Alert alert = CustomAlert.getAlert(Alert.AlertType.CONFIRMATION, "merge?");
        assert alert != null;
        alert.show();
        System.out.println("Ar trebui sa porneasca alerta");
    }

    private void openAddOrEditPage(String actions, int tableRow){
        CustomLogger.log("info", "test to see if it creates the folder and logs the message");
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

    private List<Product> getAllProducts(){

        return productService.getAll();
    }

    private void setTableColumnsWidth(){

        InventoryMenuTableService.setTableColumnsWidth(
                tableViewProduct,
                tableColumnProductName,
                tableColumnProductStockInSalon,
                tableColumnProductStockToBuy,
                tableColumnProductStockPerWeek,
                tableColumnProductActions,
                tableColumnProductActionMinus,
                tableColumnProductActionReset,
                tableColumnProductActionEdit,
                tableColumnProductActionDelete
        );
    }

    private void setTableDataFromList(){

        InventoryMenuTableService.setTableDataFromObservableList(
                tableViewProduct,
                tableColumnProductName,
                tableColumnProductStockInSalon,
                tableColumnProductStockToBuy,
                tableColumnProductStockPerWeek,
                tableColumnProductActionMinus,
                tableColumnProductActionReset,
                tableColumnProductActionEdit,
                tableColumnProductActionDelete,
                productTableList
        );

    }

    // method for demo - init database dummy items
    private void initDataBaseData(){

        DatabaseService.initDataBaseData(productService);
    }
}