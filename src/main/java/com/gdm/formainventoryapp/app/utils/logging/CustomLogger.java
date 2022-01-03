package com.gdm.formainventoryapp.app.utils.logging;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

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
            Files.writeString(path,
                    String.format("%1$s - %2$s:\n%3$s\n",
                            LocalDate.now(),
                            LocalTime.now(),
                            message),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        }catch (Exception exception){

            // TODO: i think here an alert would work
        }
    }
}
