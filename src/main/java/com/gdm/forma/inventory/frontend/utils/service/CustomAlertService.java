package com.gdm.forma.inventory.frontend.utils.service;

import com.gdm.forma.inventory.app.utils.service.CustomService;
import com.gdm.forma.inventory.frontend.utils.alert.CustomAlert;
import com.gdm.forma.inventory.frontend.utils.alert.CustomAlertShowOption;
import javafx.scene.control.Alert;


public class CustomAlertService {

    public static void getExceptionAlert(Exception exception){

        CustomAlert.exception(Alert.AlertType.ERROR, CustomAlertShowOption.YES,
                CustomService.getExceptionFullMessage(exception));
    }
}