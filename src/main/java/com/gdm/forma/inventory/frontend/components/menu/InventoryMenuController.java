package com.gdm.forma.inventory.frontend.components.menu;

import com.gdm.forma.inventory.app.utils.service.DatabaseService;
import com.gdm.forma.inventory.backend.domain.entity.Product;
import com.gdm.forma.inventory.backend.service.impl.ProductServiceImpl;
import com.gdm.forma.inventory.frontend.components.product.AddOrEditController;
import com.gdm.forma.inventory.frontend.domain.model.ProductTable;
import com.gdm.forma.inventory.frontend.utils.alert.CustomAlert;
import com.gdm.forma.inventory.frontend.utils.alert.CustomAlertShowOption;

import com.gdm.forma.inventory.frontend.utils.service.InventoryMenuService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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

    private Stage addOrEditStage;
    private Stage inventoryStage;
    private AddOrEditController addOrEditController;

    private @Getter @Setter FXMLLoader fxmlLoader;
    private @Getter @Setter Stage inventoryMenuStage;

    private @Getter @Setter
    ProductServiceImpl productService;

    private @Setter
    InventoryMenuService inventoryMenuService;

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

        Product product = getProductFromTable(tableRow);
        Optional<ButtonType> optionButtonType =
                Objects.requireNonNull(CustomAlert.alert(Alert.AlertType.CONFIRMATION, CustomAlertShowOption.NO,
                        String.format("Scade 1 unitate din produsul %s",
                                product.getName()))).showAndWait();
        AtomicReference<ButtonType> buttonType = new AtomicReference<>();
        assert false;
        optionButtonType.ifPresent(buttonType::set);
        if (buttonType.get().equals(ButtonType.OK)) {
            if (product.getStockInSalon() > 0) {
                product.setStockInSalon(product.getStockInSalon() - 1);
                productService.update(product);
                CustomAlert.alert(Alert.AlertType.INFORMATION, CustomAlertShowOption.YES,
                        String.format("A fost scazuta cu succes o unitate din produsul %s",
                                product.getName()));
            } else {
                CustomAlert.alert(Alert.AlertType.WARNING, CustomAlertShowOption.YES,
                        String.format("Nu se poate scade o unitate!\nProdusul %s are stocul 0!",
                                product.getName()));
            }

            refreshTable();
        }
    }

    private void resetQuantity(int tableRow){

        if(tableRow != Integer.MIN_VALUE) {
            Product product = getProductFromTable(tableRow);
            Optional<ButtonType> optionButtonType =
                    Objects.requireNonNull(CustomAlert.alert(Alert.AlertType.CONFIRMATION, CustomAlertShowOption.NO,
                            String.format("Reseteaza stocul produsului %s",
                                    product.getName()))).showAndWait();
            AtomicReference<ButtonType> buttonType = new AtomicReference<>();
            assert false;
            optionButtonType.ifPresent(buttonType::set);
            if (buttonType.get().equals(ButtonType.OK)) {
                product.setStockInSalon(product.getStockPerWeek());
                productService.update(product);
                CustomAlert.alert(Alert.AlertType.INFORMATION, CustomAlertShowOption.YES,
                        String.format("A fost resetat cu succes stocul produsului %s",
                                product.getName()));
                productTableList.clear();
                initTableList();
                refreshTableData();
            }
        }
        else{
            if(!tableViewProduct.getItems().isEmpty()) {
                Optional<ButtonType> optionButtonType =
                        Objects.requireNonNull(CustomAlert.alert(Alert.AlertType.CONFIRMATION, CustomAlertShowOption.NO,
                                "Resetati stocul pentru toate produsele?")).showAndWait();
                AtomicReference<ButtonType> buttonType = new AtomicReference<>();
                assert false;
                optionButtonType.ifPresent(buttonType::set);
                if (buttonType.get().equals(ButtonType.OK)) {
                    List<Product> productList = getAllProducts();
                    productList.forEach(product -> {
                        product.setStockInSalon(product.getStockPerWeek());
                        productService.update(product);
                    });
                    CustomAlert.alert(Alert.AlertType.INFORMATION, CustomAlertShowOption.YES,
                            "A fost resetat stocul pentru toate produsele");
                }
            }else{
                CustomAlert.alert(Alert.AlertType.WARNING, CustomAlertShowOption.YES,
                        "Nu exista produse in tabel!");
            }

            productTableList.clear();
            initTableList();
            refreshTableData();
        }
    }

    private void editProduct(int tableRow){

        Product product = getProductFromTable(tableRow);
        Optional<ButtonType> optionButtonType =
                Objects.requireNonNull(CustomAlert.alert(Alert.AlertType.CONFIRMATION, CustomAlertShowOption.NO,
                        String.format("Editeaza produsul %s",
                                product.getName()))).showAndWait();
        AtomicReference<ButtonType> buttonType = new AtomicReference<>();
        assert false;
        optionButtonType.ifPresent(buttonType::set);
        if (buttonType.get().equals(ButtonType.OK)) {
            openAddOrEditPage("edit", tableRow);
        }
    }

    private void deleteProduct(int tableRow){

        if(tableRow != Integer.MIN_VALUE) {
            Product product = getProductFromTable(tableRow);
            Optional<ButtonType> optionButtonType =
                    Objects.requireNonNull(CustomAlert.alert(Alert.AlertType.CONFIRMATION, CustomAlertShowOption.NO,
                            String.format("Sterge produsul %s",
                                    product.getName()))).showAndWait();
            AtomicReference<ButtonType> buttonType = new AtomicReference<>();
            assert false;
            optionButtonType.ifPresent(buttonType::set);
            if (buttonType.get().equals(ButtonType.OK)) {
                productService.delete(product);
                CustomAlert.alert(Alert.AlertType.INFORMATION, CustomAlertShowOption.YES,
                        String.format("A fost sters cu succes produsul %s",
                                product.getName()));
                productTableList.clear();
                initTableList();
                refreshTableData();
            }
        }
        else{
            if(!tableViewProduct.getItems().isEmpty()) {
                Optional<ButtonType> optionButtonType =
                        Objects.requireNonNull(CustomAlert.alert(Alert.AlertType.CONFIRMATION, CustomAlertShowOption.NO,
                                "Stergeti toate produsele?")).showAndWait();
                AtomicReference<ButtonType> buttonType = new AtomicReference<>();
                assert false;
                optionButtonType.ifPresent(buttonType::set);
                if (buttonType.get().equals(ButtonType.OK)) {
                    List<Product> productList = getAllProducts();
                    productList.forEach(product -> {
                        product.setStockInSalon(product.getStockPerWeek());
                        productService.delete(product);
                    });
                    CustomAlert.alert(Alert.AlertType.INFORMATION, CustomAlertShowOption.YES,
                            "Au fost sterse toate produsele");
                }
            }else{
                CustomAlert.alert(Alert.AlertType.WARNING, CustomAlertShowOption.YES,
                        "Nu exista produse in tabel!");
            }

            productTableList.clear();
            initTableList();
            refreshTableData();
        }
    }

    private void addProduct(){

        Optional<ButtonType> optionButtonType =
                Objects.requireNonNull(CustomAlert.alert(Alert.AlertType.CONFIRMATION, CustomAlertShowOption.NO,
                        "Adaugati un produs nou?")).showAndWait();
        AtomicReference<ButtonType> buttonType = new AtomicReference<>();
        assert false;
        optionButtonType.ifPresent(buttonType::set);
        if (buttonType.get().equals(ButtonType.OK)) {
            openAddOrEditPage("add", Integer.MIN_VALUE);
        }
    }

    private void openAddOrEditPage(String page, int tableRow){
        if(addOrEditStage == null) {

            try {
                fxmlLoader = new FXMLLoader(
                        AddOrEditController.class.getResource("add-or-edit.fxml"));
                Parent window = fxmlLoader.load();

                addOrEditController = fxmlLoader.getController();
                if(tableRow != Integer.MIN_VALUE) {
                    addOrEditController.setProduct(getProductFromTable(tableRow));
                }
                addOrEditController.setProductServiceImpl(productService);
                addOrEditController.setInventoryMenuController(this);
                addOrEditController.setInventoryStage(inventoryStage);
                addOrEditController.setPage(page);

                addOrEditStage = new Stage();
                addOrEditController.setAddOrEditStage(addOrEditStage);
                addOrEditController.init();

                addOrEditStage.getIcons().add(new Image("/com/gdm/forma/inventory/frontend/images/inventoryLogo.png"));
                Scene addOrEditScene = new Scene(window);
//                addOrEditScene.getStylesheets().add(String.valueOf(
//                        AddOrEditController.class.getResource("AddOrEdit.css")));
                addOrEditStage.setScene(addOrEditScene);

                addOrEditStage.setWidth(640.0);
                addOrEditStage.setHeight(360.0);

                addOrEditStage.setResizable(false);

                enableButtons(true);

                addOrEditStage.setOnHiding(temp -> {
                    addOrEditStage = null;

                    enableButtons(false);

                    productTableList.clear();
                    initTableList();
                    refreshTableData();
                });

                addOrEditStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            addOrEditStage.toFront();
        }
    }

    private void enableButtons(boolean enable){

        textFieldSearchProduct.setDisable(enable);

        buttonSearch.setDisable(enable);
        buttonAddProduct.setDisable(enable);

//        buttonResetAllStocks.setDisable(enable);
//        buttonDeleteAll.setDisable(enable);

        tableViewProduct.setDisable(enable);
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
                "/com/gdm/forma/inventory/frontend/images/%s.png",
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
                "/com/gdm/forma/inventory/frontend/images/%s.png",
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

        InventoryMenuService.setTableColumnsWidth(
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

        InventoryMenuService.setTableDataFromObservableList(
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