package com.gdm.forma.inventory.frontend.components.product;

import com.gdm.forma.inventory.backend.domain.entity.Product;
import com.gdm.forma.inventory.backend.service.impl.ProductServiceImpl;
import com.gdm.forma.inventory.frontend.components.menu.InventoryMenuController;
import com.gdm.forma.inventory.frontend.utils.alert.CustomAlert;
import com.gdm.forma.inventory.frontend.utils.alert.CustomAlertShowOption;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


// TODO: finish the class and dont forget about confirmations

public class AddOrEditController {
    @FXML
    private Label labelTitle, labelSubtitle;
    @FXML
    private TextField textFieldProductName, textFieldStockInSalon, textFieldStockPerWeek;
    @FXML
    private Button buttonSaveOrUpdate, buttonCancelAndGoBack;

    private Stage addOrEditStage, inventoryStage;
    private String addOrEditPage = "";
    private Product product;
    private ProductServiceImpl productService;
    private InventoryMenuController inventoryMenuController;

    private static final String DIGITS = "0123456789";
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public AddOrEditController() {}

    public AddOrEditController(String addOrEditPage){ this.addOrEditPage = addOrEditPage;}

    public void setProduct(Product product){ this.product = product;}

    public void setProductServiceImpl(ProductServiceImpl productService){ this.productService = productService;}

    public void setInventoryMenuController(InventoryMenuController inventoryMenuController){
        this.inventoryMenuController = inventoryMenuController;
    }

    public void setAddOrEditStage(Stage addOrEditStage) { this.addOrEditStage = addOrEditStage;}

    public void setInventoryStage(Stage inventoryStage) { this.inventoryStage = inventoryStage;}

    @FXML
    private void initialize(){

        textFieldProductName.setFocusTraversable(false);
        textFieldStockInSalon.setFocusTraversable(false);
        textFieldStockPerWeek.setFocusTraversable(false);
    }

    public void init(){

        addOrEditStage.setOnCloseRequest(windowEvent -> {
            Optional<ButtonType> optionButtonType =
                    Objects.requireNonNull(CustomAlert.alert(Alert.AlertType.CONFIRMATION, CustomAlertShowOption.NO,
                            "Inchideti fereastra?")).showAndWait();
            AtomicReference<ButtonType> buttonType = new AtomicReference<>();
            assert false;
            optionButtonType.ifPresent(buttonType::set);
            if (buttonType.get().equals(ButtonType.OK)) {
                windowEvent.consume();
            }
            else {
                try {
                    Thread.sleep(500);
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        });

        buttonCancelAndGoBack.setOnMouseClicked(mouseEvent -> {
            Optional<ButtonType> optionButtonType =
                    Objects.requireNonNull(CustomAlert.alert(Alert.AlertType.CONFIRMATION, CustomAlertShowOption.NO,
                            "Inchideti fereastra?")).showAndWait();
            AtomicReference<ButtonType> buttonType = new AtomicReference<>();
            assert false;
            optionButtonType.ifPresent(buttonType::set);
            if (buttonType.get().equals(ButtonType.OK)) {
                try {
                    Thread.sleep(500);
                }catch (Exception exception){
                    exception.printStackTrace();
                }
                addOrEditStage.hide();
            }
        });

        buttonSaveOrUpdate.setOnMouseClicked(mouseEvent -> {
            switch(addOrEditPage){
                case "add":
                    addProduct();
                    break;
                case "edit":
                    updateProduct();
                    break;
                default:
                    System.out.println("default");
                    break;
            }
        });
    }

    public void setPage(String addOrEditPage){

        this.addOrEditPage = addOrEditPage;

        switch(addOrEditPage){
            case "add":
                addPage();
                break;
            case "edit":
                editPage();
                break;
            default:
                System.out.println("default");
                break;
        }
    }

    private void addPage(){

        labelTitle.setText("Adauga un produs nou in inventar");
        buttonSaveOrUpdate.setText("Salveaza produsul");
    }

    private void editPage(){

        labelTitle.setText(String.format("Editeaza produsul\n%s",
                product.getName()));
        buttonSaveOrUpdate.setText("Salveaza modificarile");

        textFieldProductName.setText(product.getName());
        textFieldStockInSalon.setText(product.getStockInSalon().toString());
        textFieldStockPerWeek.setText(product.getStockPerWeek().toString());
    }

    private void addProduct(){

        // TODO: add checks for input fields
        // TODO: add confirmation: successful or unsuccessful action

        if (checkUserInputFromTextFieldIsCorrect(textFieldProductName.getText(),
                textFieldStockInSalon.getText(),
                textFieldStockPerWeek.getText())) {
            try {
                product = new Product();

                setProductData();

                productService.saveOrUpdate(product);

                Optional<ButtonType> optionButtonType =
                        Objects.requireNonNull(CustomAlert.alert(Alert.AlertType.INFORMATION, CustomAlertShowOption.NO,
                                String.format(
                                        "Produsul %s a fost adaugat cu succes",
                                        product.getName()))).showAndWait();
                AtomicReference<ButtonType> buttonType = new AtomicReference<>();
                assert false;
                optionButtonType.ifPresent(buttonType::set);
                if (buttonType.get().equals(ButtonType.OK)) {
                    try {
                        Thread.sleep(500);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    addOrEditStage.hide();
                }
            }catch (Exception exception){
                product = new Product();
            }
        }else{
            CustomAlert.alert(Alert.AlertType.WARNING, CustomAlertShowOption.YES,
                    "Campurile nu au fost completate corect!");
        }


    }

    private void updateProduct(){

        // TODO: add confirmation: successful or unsuccessful action
        if(checkUserInputFromTextFieldIsCorrect(textFieldProductName.getText(),
                textFieldStockInSalon.getText(),
                textFieldStockPerWeek.getText())) {
            setProductData();

            productService.update(product);

            Optional<ButtonType> optionButtonType =
                    Objects.requireNonNull(CustomAlert.alert(Alert.AlertType.INFORMATION, CustomAlertShowOption.NO,
                            String.format(
                                    "Produsul %s a fost modificat cu succes",
                                    product.getName()))).showAndWait();
            AtomicReference<ButtonType> buttonType = new AtomicReference<>();
            assert false;
            optionButtonType.ifPresent(buttonType::set);
            if (buttonType.get().equals(ButtonType.OK)) {
                try {
                    Thread.sleep(500);
                }catch (Exception exception){
                    exception.printStackTrace();
                }
                addOrEditStage.hide();
            }
        }else{
            CustomAlert.alert(Alert.AlertType.WARNING, CustomAlertShowOption.YES,
                    "Campurile nu au fost completate corect!");
        }
    }

    private void setProductData(){
        product.setName(textFieldProductName.getText());
        product.setStockInSalon(Integer.parseInt(textFieldStockInSalon.getText()));
        product.setStockPerWeek(Integer.parseInt(textFieldStockPerWeek.getText()));
    }

    private void resetProductData(){
        product.setName("");
        product.setStockInSalon(-1);
        product.setStockPerWeek(-1);
    }

    private void resetTextFieldsData(){
        textFieldProductName.setText("");
        textFieldStockInSalon.setText("");
        textFieldStockPerWeek.setText("");
    }

    private boolean checkUserInputFromTextFieldIsCorrect(String productName, String stockInSalon, String stockPerWeek){
        return checkIsNotStringNullOrEmpty(productName,
                stockInSalon,
                stockPerWeek) &&
                checkStringTextFieldHasCorrectInput(productName) &&
                checkNumericTextFieldHasCorrectInput(stockInSalon,
                        stockPerWeek) &&
                checkNumericTextFieldValueForStockPerWeekIsGreaterThan0(stockPerWeek, stockInSalon) &&
                stockCantBeHigherThanStockPerWeek(stockPerWeek, stockInSalon);
    }

    private static boolean checkIsNotStringNullOrEmpty(String... strings){

        for (String st : strings) {
            if (st == null || st.equals("")) {
                System.out.println(st);
                return false;

            }
        }
        return true;
    }

    private static boolean checkNumericTextFieldHasCorrectInput(String... strings){

        for (String st : strings) {
            for(Character c: st.toCharArray()) {
                if (!DIGITS.contains(c.toString())) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean checkNumericTextFieldValueForStockPerWeekIsGreaterThan0(String stockPerWeek, String stockInSalon){
        return Integer.parseInt(stockPerWeek) > 0 ||
                Integer.parseInt(stockPerWeek) < Integer.parseInt(stockInSalon);
    }

    private static boolean checkStringTextFieldHasCorrectInput(String... strings){

        for (String st : strings) {
            for(Character c: st.toCharArray()) {
                if (!LETTERS.contains(c.toString()) && !LETTERS.toLowerCase().contains(c.toString()) &&
                        !DIGITS.contains(c.toString())) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean stockCantBeHigherThanStockPerWeek(String stockPerWeek, String stockInSalon){
        return Integer.parseInt(stockPerWeek) >= Integer.parseInt(stockInSalon);
    }
}