package com.gdm.formainventoryapp.app.main;

import com.gdm.formainventoryapp.app.utils.logging.CustomLogger;
import com.gdm.formainventoryapp.backend.service.impl.ProductServiceImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import com.gdm.formainventoryapp.frontend.components.mainmenu.InventoryMenuController;

import java.io.*;


public class InventoryApplication extends Application{
    private FXMLLoader fxmlLoader;

    private ProductServiceImpl productService;
    private InventoryMenuController inventoryMenuController;

    public static void main(String[] args){

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        boolean noExceptionsOccurred = true;

        try{
            fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(InventoryMenuController.class.getResource("X"));
            Parent window = fxmlLoader.load();

//            productService = new ProductServiceImpl();
        }catch (Exception exception){
            CustomLogger.log("info", "does it work");
            noExceptionsOccurred = false;
        }

        if (noExceptionsOccurred){
            System.out.println("No exceptions occurred");
        }
        else {
            System.out.println("Exceptions occurred");
        }

        try{
            fxmlLoader.setLocation(InventoryMenuController.class.getResource("X"));
            Parent window = fxmlLoader.load();
        }catch (Exception exception){
            CustomLogger.log("exception", "does it work here too?");
        }

        System.exit(1);
    }
}
