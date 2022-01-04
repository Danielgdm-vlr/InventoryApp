package com.gdm.formainventoryapp.app.utils.logging;

import com.gdm.formainventoryapp.frontend.utils.alert.CustomAlert;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class CustomLogger {

    private static final Path infoLoggerPath;
    private static final Path exceptionLoggerPath;
    private static final Path dbLoggerPath;

    static{
        infoLoggerPath = Path.of("logs/InfoLogger.txt");
        exceptionLoggerPath = Path.of("logs/ExceptionLogger.txt");
        dbLoggerPath = Path.of("logs/DBLogger.txt");
    }

    public static void log(String logType, String message) {

        try {
            switch (logType) {
                case "info":
                    logMessage(infoLoggerPath, message);
                    break;
                case "exception":
                    logMessage(exceptionLoggerPath, message);
                    break;
                case "db":
                    logMessage(dbLoggerPath, message);
                    break;
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    private static void logMessage(Path path, String message){

        try {
            Path pathToFolder = Path.of(String.format("%1$s%2$s",
                    System.getProperty("user.dir"),
                    "/logs"));
            try {
                Files.createDirectory(pathToFolder);
            }catch (FileAlreadyExistsException fileAlreadyExistsException){
                CustomAlert.showAlert(Alert.AlertType.WARNING,
                        fileAlreadyExistsException.getClass().getSimpleName());
            } catch (IOException ioException) {

                // TODO: create alerts for exceptions
                ioException.printStackTrace();
            }
//            throw new IOException();
           writeMessage(path, message);
        }catch (NoSuchFileException noSuchFileException){
            /*
                this mean the folder "logs" does not exist at this path, so it needs to be created
                Files.createDirectories(path) face un folder nou la locatia specificata
            */

            // TODO: nu stiu daca aici trebuie o alerta

            Path pathToFolder = Path.of(String.format("%1$s%2$s",
                    System.getProperty("user.dir"),
                    "/logs"));
            try {
                Files.createDirectory(pathToFolder);
            }catch (FileAlreadyExistsException fileAlreadyExistsException){
                CustomAlert.showAlert(Alert.AlertType.WARNING,
                                fileAlreadyExistsException.getClass().getSimpleName());
            } catch (IOException ioException) {

                // TODO: create alerts for exceptions
                ioException.printStackTrace();
            }

            /*
                this logs the message to the file after it was created
                because the method Files.writeString(path) face un fisier la pathul respectiv
             */
            try {
                writeMessage(path, message);
            }catch (Exception exception){

                // TODO: create alerts for exceptions
                exception.printStackTrace();
            }
        }catch (IOException ioException){

            // TODO: create alerts for exceptions
            CustomAlert.showAlert(Alert.AlertType.WARNING, ioException.getMessage());
        }
    }

    private static void writeMessage(Path path, String message) throws IOException {

            Files.writeString(path,
                    String.format("%1$s - %2$s:\n%3$s\n",
                            LocalDate.now(),
                            LocalTime.now(),
                            message),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
    }
}
