package com.gdm.formainventoryapp.app.main;

import com.gdm.formainventoryapp.app.utils.logging.CustomLogger;
import com.gdm.formainventoryapp.backend.service.impl.ProductServiceImpl;
import com.gdm.formainventoryapp.frontend.utils.alert.CustomAlert;
import com.gdm.formainventoryapp.frontend.utils.service.InventoryMenuService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import com.gdm.formainventoryapp.frontend.components.mainmenu.InventoryMenuController;

import java.io.*;


public class InventoryApplication extends Application{

    private FXMLLoader fxmlLoader;
    private Scene inventoryScene;

    private ProductServiceImpl productService;
    private InventoryMenuController inventoryMenuController;
    private InventoryMenuService inventoryMenuService;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage inventoryMenuStage){

        boolean noExceptionsOccurredOnObjectsInstantiation = true;

        try{
            fxmlLoader = new FXMLLoader();
            productService = new ProductServiceImpl();
        }catch (Exception exception){
//            CustomLogger.log("exception", exception.getMessage());
            noExceptionsOccurredOnObjectsInstantiation = false;
        }

        if (noExceptionsOccurredOnObjectsInstantiation){
            try{
                loadPage(inventoryMenuStage);
            }catch (Exception exception){
//                CustomLogger.log("exception", exception.getMessage());
                exception.printStackTrace();
            }
        }
        else {

            // TODO: some kind of alert
        }
    }

    private void loadPage(Stage inventoryMenuStage) throws IOException {
        fxmlLoader.setLocation(InventoryMenuController.class.getResource("inventory-menu.fxml"));
        Parent window = fxmlLoader.load();
        inventoryScene = new Scene(window);

        setupInventoryMenuController(inventoryMenuStage);

        setupStageAndScene(inventoryMenuStage);
    }

    private void setupInventoryMenuController(Stage inventoryMenuStage){

        inventoryMenuController = fxmlLoader.getController();
        inventoryMenuController.setInventoryMenuStage(inventoryMenuStage);
        inventoryMenuController.setFxmlLoader(fxmlLoader);
        inventoryMenuController.setProductService(productService);
        inventoryMenuController.setInventoryMenuService(inventoryMenuService);
        inventoryMenuController.init();
    }

    private void setupStageAndScene(Stage inventoryMenuStage){
        inventoryMenuStage.setScene(inventoryScene);

//        inventoryScene.getStylesheets().add(
//                String.valueOf(InventoryMenuController.class.getResource("inventory-menu.css")));
        inventoryMenuStage.getIcons().add(new Image("/com/gdm/formainventoryapp/frontend/images/inventoryLogo.png"));

        inventoryMenuStage.setWidth(1280.0);
        inventoryMenuStage.setHeight(720.0);
        inventoryScene.getWindow().centerOnScreen();
        inventoryMenuStage.show();
    }
}
