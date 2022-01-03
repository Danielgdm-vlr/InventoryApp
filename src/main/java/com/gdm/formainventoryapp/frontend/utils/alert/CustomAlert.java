package com.gdm.formainventoryapp.frontend.utils.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;


public class CustomAlert {

    private static final String confirmationAlertCSS;
    private static final String informationAlertCSS;
    private static final String warningAlertCSS;

    static{

        confirmationAlertCSS = String.format("%1$s/%2$s",
                "com/gdm/formainventoryapp/frontend/utils/alert",
                "confirmation-alert.css");
        informationAlertCSS = String.format("%1$s/%2$s",
                "com/gdm/formainventoryapp/frontend/utils/alert",
                "information-alert.css");
        warningAlertCSS = String.format("%1$s/%2$s",
                "com/gdm/formainventoryapp/frontend/utils/alert",
                "warning-alert.css");
    }

    public static Alert getAlert(Alert.AlertType alertType, String message){
        switch (alertType){
            case CONFIRMATION:
                return returnAlert(message, confirmationAlertCSS, alertType, "Confirmare");
            case INFORMATION:
                return returnAlert(message, informationAlertCSS, alertType, "Informare");
            case WARNING:
                return returnAlert(message, warningAlertCSS, alertType, "Atentie!");
            default:
                // TODO: default empty allert
                return null;
        }

    }

    private static Alert returnAlert(String message, String cssPath, Alert.AlertType alertType, String headerText){

        return setupAlert(message, cssPath, alertType, headerText);
    }

    public static void showAlert(Alert.AlertType alertType, String message){
        switch (alertType){
            case CONFIRMATION:
                voidAlert(message, confirmationAlertCSS, alertType, "Confirmare");
                break;
            case INFORMATION:
                voidAlert(message, informationAlertCSS, alertType, "Informare");
                break;
            case WARNING:
                voidAlert(message, warningAlertCSS, alertType, "Atentie!");
                break;
            default:
                // TODO: default empty allert
        }
    }

    private static void voidAlert(String message, String cssPath, Alert.AlertType alertType, String headerText){

        setupAlert(message, cssPath, alertType, headerText).showAndWait();
    }

    private static Alert setupAlert(String message, String cssPath, Alert.AlertType alertType, String headerText){

        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.setHeaderText(headerText);

        alert.initStyle(StageStyle.TRANSPARENT);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(cssPath);
        dialogPane.getStyleClass().add("body");
        dialogPane.getScene().setFill(Color.TRANSPARENT);

        return alert;
    }

}
