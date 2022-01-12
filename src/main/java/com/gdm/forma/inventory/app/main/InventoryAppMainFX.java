package com.gdm.forma.inventory.app.main;

import com.gdm.forma.inventory.app.utils.logging.CustomLogger;
import com.gdm.forma.inventory.app.utils.logging.CustomLoggerType;
import com.gdm.forma.inventory.app.utils.service.CustomService;
import com.gdm.forma.inventory.backend.service.impl.ProductServiceImpl;
import com.gdm.forma.inventory.frontend.utils.service.InventoryMenuService;
import com.gdm.forma.inventory.frontend.components.menu.InventoryMenuController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Arrays;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.*;


public class InventoryAppMainFX extends Application{

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
            CustomLogger.log(CustomLoggerType.EXCEPTION,
                    Arrays.toString(CustomService.getExceptionFullMessage(exception)));
            noExceptionsOccurredOnObjectsInstantiation = false;
            exception.printStackTrace();
        }

        if (noExceptionsOccurredOnObjectsInstantiation){
            try{
                loadPage(inventoryMenuStage);
                CustomLogger.log(CustomLoggerType.INFO,
                        "App start up without any exception encountered");
            }catch (Exception exception){
                CustomLogger.log(CustomLoggerType.EXCEPTION,
                        Arrays.toString(CustomService.getExceptionFullMessage(exception)));
            }
        }
        else {

            // TODO: some kind of alert or basic fx page

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
//        inventoryMenuStage.getIcons().add(new Image("/com/gdm/formainventoryapp/frontend/images/inventoryLogo.png"));

        inventoryMenuStage.setWidth(1280.0);
        inventoryMenuStage.setHeight(720.0);
        inventoryScene.getWindow().centerOnScreen();
        inventoryMenuStage.show();
    }
}
