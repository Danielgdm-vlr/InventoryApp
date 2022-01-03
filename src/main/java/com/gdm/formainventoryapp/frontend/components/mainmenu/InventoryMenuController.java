package com.gdm.formainventoryapp.frontend.components.mainmenu;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gdm.formainventoryapp.backend.model.entity.Product;
import com.gdm.formainventoryapp.backend.service.impl.ProductServiceImpl;
import com.gdm.formainventoryapp.frontend.components.addoredit.AddOrEditController;
import com.gdm.formainventoryapp.frontend.model.ProductTable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InventoryMenuController {

    @FXML
    private AnchorPane mainContainer;
    @FXML
    private VBox vBoxPageTitles;
    @FXML
    private HBox hBoxTableOptions;
    @FXML
    private Label labelTitle, labelSubtitle;
    @FXML
    private TextField textFieldSearchProduct;
    @FXML
    private Button buttonSearch, buttonAddProduct;
    //            buttonResetAllStocks, buttonDeleteAll;
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
    private final List<ProductTable> productTableList = new ArrayList<>();
    private AddOrEditController addOrEdit;
    private ProductServiceImpl productService;
    private FXMLLoader fxmlLoader;

    public static final ProductTable MINIMUM_STOCK_0 = new ProductTable(0);
    public static final ProductTable MINIMUM_STOCK_1 = new ProductTable(1);
    public static final ProductTable MINIMUM_STOCK_2 = new ProductTable(2);

    private PseudoClass childOfSelected = PseudoClass.getPseudoClass("child-of-selected");

    @FXML
    private void initialize(){

        textFieldSearchProduct.setFocusTraversable(false);
    }

    public void init(){

//        initDBData();

        initTableList();
        setTableColumnsWidth();
        setTableData();

        initButtons();

//        inventoryStage.setOnCloseRequest(windowEvent -> {
//            ConfirmationAlert confirmationAlert = new ConfirmationAlert("Inchidere aplicatie");
//            confirmationAlert.closeWindow(windowEvent);
//        });
    }

    private void initButtons(){

        initTableOptionsButtons(buttonSearch,"search");
        initTableOptionsButtons(buttonAddProduct,"addProduct");

//        initTableOptionsButtons(buttonResetAllStocks,"reset");
//        initTableOptionsButtons(buttonDeleteAll,"delete");
    }

    private void initTableOptionsButtons(Button button, String buttonName){

        Image image = new Image(String.format("/com/forma/inventory/ui/images/%s.png", buttonName));
        ImageView imageView = new ImageView(image);

        button.setGraphic(imageView);

        button.setOnMouseClicked(actionEvent ->{
            switch (buttonName){
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

    private void removeUnits(int tableRow){

        // TODO: confirmation before action
        Product product = getProductFromTableRow(tableRow);

        if(product.getStockInSalon() > 0) {
            product.setStockInSalon(product.getStockInSalon() - 1);
            productService.update(product);
        }
        else{
            // TODO: alert that stock is 0 and cant be negative
        }

        productTableList.clear();
        initTableList();
        refreshTableData();
    }

    private void resetQuantity(int tableRow){

        // TODO: alert if no products are in table

        if(tableRow != Integer.MIN_VALUE) {
            Product product = getProductFromTableRow(tableRow);
//            ConfirmationAlert confirmationAlert = new ConfirmationAlert(String.format(
//                    "Resetati stocul pentru produsul: %s", product.getName()));
//            if(confirmationAlert.confirmAction()) {
//                product.setStockInSalon(product.getStockPerWeek());
//                productService.update(product);
//
//                productTableList.clear();
//                initTableList();
//                refreshTableData();
//            }
        }
        else{
//            ConfirmationAlert confirmationAlert = new ConfirmationAlert(
//                    "Resetati stocul pentru toate produsele");
//            if(confirmationAlert.confirmAction()) {
//                List<Product> productList = getAllProducts();
//                productList.forEach(product -> {
//                    product.setStockInSalon(product.getStockPerWeek());
//                    productService.update(product);
//                });

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

        if(tableRow != Integer.MIN_VALUE) {
            Product product = getProductFromTableRow(tableRow);
//            ConfirmationAlert confirmationAlert = new ConfirmationAlert(String.format(
//                    "Stergeti produsul: %s", product.getName()));
//            if(confirmationAlert.confirmAction()) {
//                product.setStockInSalon(product.getStockPerWeek());
//                productService.delete(product);
//
//                productTableList.clear();
//                initTableList();
//                refreshTableData();
//            }
        }
        else{
//            ConfirmationAlert confirmationAlert = new ConfirmationAlert(
//                    "Stergeti toate produsele");
//            if(confirmationAlert.confirmAction()) {
//                List<Product> productList = getAllProducts();
//                productList.forEach(product -> {
//                    product.setStockInSalon(product.getStockPerWeek());
//                    productService.truncate();
//                });
//
//                productTableList.clear();
//                initTableList();
//                refreshTableData();
//            }
        }
    }

    @FXML
    private void addProduct(){
        openAddOrEditPage("add", Integer.MIN_VALUE);
    }

    private void openAddOrEditPage(String page, int tableRow){
//        if(addOrEditStage == null) {
//
//            try {
//                fxmlLoader = new FXMLLoader(
//                        AddOrEditController.class.getResource("AddOrEdit.fxml"));
//                Parent window = fxmlLoader.load();
//
//                addOrEdit = fxmlLoader.getController();
//                if(tableRow != Integer.MIN_VALUE) {
//                    addOrEdit.setProduct(getProductFromTableRow(tableRow));
//                }
//                addOrEdit.setProductServiceImpl(productService);
//                addOrEdit.setInventory(this);
//                addOrEdit.setInventoryStage(inventoryStage);
//                addOrEdit.setPage(page);
//
//                addOrEditStage = new Stage();
//                addOrEdit.setAddOrEditStage(addOrEditStage);
//                addOrEdit.init();
//
//                addOrEditStage.getIcons().add(new Image("/com/forma/inventory/ui/images/inventoryLogo.png"));
//                Scene addOrEditScene = new Scene(window);
//                addOrEditScene.getStylesheets().add(String.valueOf(AddOrEdit.class.getResource("AddOrEdit.css")));
//                addOrEditStage.setScene(addOrEditScene);
//
//                addOrEditStage.setWidth(640.0);
//                addOrEditStage.setHeight(360.0);
//
//                addOrEditStage.setResizable(false);
//
//                enableButtons(true);
//
//                addOrEditStage.setOnHiding(temp -> {
//                    addOrEditStage = null;
//
//                    enableButtons(false);
//
//                    productTableList.clear();
//                    initTableList();
//                    refreshTableData();
//                });
//
//                addOrEditStage.show();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        else {
//            addOrEditStage.toFront();
//        }
    }

    private void enableButtons(boolean enable){

        textFieldSearchProduct.setDisable(enable);

        buttonSearch.setDisable(enable);
        buttonAddProduct.setDisable(enable);

//        buttonResetAllStocks.setDisable(enable);
//        buttonDeleteAll.setDisable(enable);

        tableViewProduct.setDisable(enable);
    }

    private void initTableList() {
        List<Product> productList = getAllProducts();
        int size = productList.size();
        ProductTable tempProductTable;
        Product product;

        for (int tableRow = 0; tableRow < size; tableRow++) {

            product = productList.get(tableRow);

            Button minusButton = initTableButtons("minus", tableRow);
            Button resetButton = initTableButtons("reset", tableRow);
            Button buttonEdit = initTableButtons("edit", tableRow);
            Button buttonDelete = initTableButtons("delete", tableRow);

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

            tempProductTable = new ProductTable(
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

            productTableList.add(tempProductTable);
        }
    }

    private Button initTableButtons(String imageName, int tableRow){

        Image image = new Image(String.format("/com/forma/inventory/ui/images/%s.png", imageName));
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

    private void setTableData(){

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

                    BooleanBinding booleanBinding0 = row.itemProperty().isEqualTo(MINIMUM_STOCK_0).and(
                            row.itemProperty().isNotNull());
                    BooleanBinding booleanBinding1 = row.itemProperty().isEqualTo(MINIMUM_STOCK_1).and(
                            row.itemProperty().isNotNull());
                    BooleanBinding booleanBinding2 = row.itemProperty().isEqualTo(MINIMUM_STOCK_2).and(
                            row.itemProperty().isNotNull());
                    row.styleProperty().bind(Bindings.when(booleanBinding0)
                            .then("-fx-background-color: #ff0400;\n" +
                                    "-fx-font-size: 1.25em;\n" +
                                    "    -fx-font-weight: bold;\n" +
                                    "    -fx-text-fill: #131516;\n" +
                                    "    -fx-table-cell-border-color: #ff0400;\n" +
                                    "    -fx-border-color: #ff0400;\n" +
                                    "    -fx-cell-size: 5em;")
                            .otherwise(Bindings.when(booleanBinding1)
                                    .then("-fx-background-color: #ff3633;\n" +
                                            "-fx-font-size: 1.25em;\n" +
                                            "    -fx-font-weight: bold;\n" +
                                            "    -fx-text-fill: #131516;\n" +
                                            "    -fx-table-cell-border-color: #ff3633;\n" +
                                            "    -fx-border-color: #ff3633;\n" +
                                            "    -fx-cell-size: 5em;")
                                    .otherwise(Bindings.when(booleanBinding2)
                                            .then("-fx-background-color: #ff6966;\n" +
                                                    "-fx-font-size: 1.25em;\n" +
                                                    "    -fx-font-weight: bold;\n" +
                                                    "    -fx-text-fill: #131516;\n" +
                                                    "    -fx-table-cell-border-color: #ff6966;\n" +
                                                    "    -fx-border-color: #ff6966;\n" +
                                                    "    -fx-cell-size: 5em;")
                                            .otherwise(""))));
                    return row;
                }
        );
    }

//    private void initDBData(){
//
//        String[] productNames = {"Dezinfectant", "Masti", "Apa", "Cafea", "Zahar", "Lapte", "Bomboane",
//                "Bete lemn (cafea)", "Scortisoara", "Folii vopsit mari", "Sul hartie", "Manusi (marime-M)",
//                "Manusi (marime-XS)", "Hartie (Z-uri)", "Prosoape SCAFA", "Folie aluminiu", "Gulere",
//                "Saci de gunoi", "Sampon", "Solutie WC", "Solutie geamuri", "Solutie Mop", "Servetele masa",
//                "Hartie igienica", "Sapun", "Detergent vase", "Bureti"};
//
//        List<Product> productList = new ArrayList<>();
//
//        Arrays.asList(productNames)
//                .forEach(productName -> productList.add(new Product(
//                        productName,
//                        3,
//                        7
//                )));
//        productService.saveOrUpdateAllList(productList);
//    }

    private void refreshTableData(){ tableViewProduct.setItems(FXCollections.observableList(productTableList));}

    private void refreshTableData(List<ProductTable> productTableList){
        tableViewProduct.setItems(FXCollections.observableList(productTableList));
    }

    private ProductTable getTableRow(int tableRow){ return tableViewProduct.getItems().get(tableRow);}

    private Product getProductFromTableRow(int tableRow){

        ProductTable tableProduct = getTableRow(tableRow);

        Product product = new Product(
                tableProduct.getName().getText(),
                tableProduct.getStockInSalon(),
                tableProduct.getStockPerWeek()
        );
        product.setId(tableProduct.getId());
        return product;
    }

    private List<Product> getAllProducts(){ return productService.getAll();}

    public void setInventoryStage(Stage inventoryStage) { this.inventoryStage = inventoryStage;}

    public void setProductService(ProductServiceImpl productService){ this.productService = productService;}

    public void setFxmlLoader(FXMLLoader fxmlLoader) {this.fxmlLoader = fxmlLoader;}
}
