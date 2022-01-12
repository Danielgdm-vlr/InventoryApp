package com.gdm.forma.inventory.frontend.utils.alert;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

public class CustomAlert {

    // TODO: complete path
    private static final String pathToErrorCSS = "";
    private static final String pathToWarningCSS = "";
    private static final String pathToConfirmationCSS = "";
    private static final String pathToInformationCSS = "";
    private static final String pathToNoneCSS = "";

    public static Alert alert(Alert.AlertType alertType, CustomAlertShowOption customAlertShowOption,
                              String alertMessage){

        switch (alertType){
            case WARNING:
                return getAlert(Alert.AlertType.WARNING, customAlertShowOption,
                        pathToWarningCSS, alertMessage, null);
            case CONFIRMATION:
                return getAlert(Alert.AlertType.CONFIRMATION, customAlertShowOption,
                        pathToConfirmationCSS, alertMessage, null);
            case INFORMATION:
                return getAlert(Alert.AlertType.INFORMATION, customAlertShowOption,
                        pathToInformationCSS, alertMessage, null);
            case NONE:
                return getAlert(Alert.AlertType.NONE, customAlertShowOption,
                        pathToNoneCSS, alertMessage, null);
        }

        return null;
    }

    public static Alert exception(Alert.AlertType alertType, CustomAlertShowOption customAlertShowOption,
                                  String[] arrayAlertMessage){

        return getAlert(Alert.AlertType.ERROR, customAlertShowOption,
                pathToErrorCSS, null, arrayAlertMessage);
    }

    private static Alert getAlert(Alert.AlertType alertType, CustomAlertShowOption customAlertShowOption,
                                  String pathToCSS, String alertMessage, String[] arrayAlertMessage){

        Alert alert = new Alert(alertType);
        DialogPane dialogPane = alert.getDialogPane();

        setAlertHeaderText(alertType, alert);

        if(alertType.equals(Alert.AlertType.ERROR)){

            String informationAboutException = setInformationAboutException(arrayAlertMessage);

            Label label = new Label(informationAboutException);
            label.setWrapText(true);

            TextArea textArea = new TextArea(arrayAlertMessage[2]);
            textArea.setWrapText(true);
            textArea.setEditable(false);
            textArea.setVisible(false);

            Button button = new Button("Vezi mesajul de eroare");
            button.setOnMouseClicked(mouseEvent -> {
                textArea.setVisible(true);
            });

            VBox vBox = new VBox();
            vBox.getChildren().addAll(label, button, textArea);

            dialogPane.setContent(vBox);
        }
        else {
            alert.setContentText(alertMessage);
        }

//        setAlertCSS(alert, dialogPane, pathToCSS);

        if(customAlertShowOption.equals(CustomAlertShowOption.YES)){
            alert.showAndWait();
            return null;
        }
        else {
            return alert;
        }
    }

    private static void setAlertHeaderText(Alert.AlertType alertType, Alert alert){
        switch (alertType){
            case ERROR:
                alert.setHeaderText("Eroare");
                break;
            case WARNING:
                alert.setHeaderText("Atentie");
                break;
            case CONFIRMATION:
                alert.setHeaderText("Confirma actiunea");
                break;
            case INFORMATION:
                alert.setHeaderText("Informare");
                break;
            case NONE:
                alert.setHeaderText("what");
                break;
        }
    }

    private static String setInformationAboutException(String[] alertMessage){
        String informationAboutException = String.format("Exceptie detectata - %1$s",
                alertMessage[0]);
        if(!alertMessage[1].equals("null")) {
            informationAboutException = String.format("%1$s: %2$s",
                    informationAboutException,
                    alertMessage[1]);
        }

        return informationAboutException;
    }

    private static void setAlertCSS(Alert alert, DialogPane dialogPane, String pathToCSS){
        alert.initStyle(StageStyle.TRANSPARENT);
        dialogPane.getStylesheets().add(pathToCSS);
        dialogPane.getStyleClass().add("body");
        dialogPane.getScene().setFill(Color.TRANSPARENT);
    }
}