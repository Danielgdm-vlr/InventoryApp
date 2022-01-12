package com.gdm.forma.inventory.app.utils.logging;

import com.gdm.forma.inventory.frontend.utils.service.CustomAlertService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class CustomLogger {

    private static final Path systemPathToLogsFolder =
            Path.of(String.format("%s/logs",
                    System.getProperty("user.dir")));
    private static final Path infoLoggerPath =
            Path.of("logs/InfoLogger.txt");
    private static final Path exceptionLoggerPath =
            Path.of("logs/ExceptionLogger.txt");
    private static final Path dbLoggerPath =
            Path.of("logs/DBLogger.txt");

    private static boolean folderExists;

    public static void log(CustomLoggerType customLoggerType, String logMessage){

        switch (customLoggerType){
            case INFO:
                logAction(infoLoggerPath, logMessage);
                break;
            case EXCEPTION:
                logAction(exceptionLoggerPath, logMessage);
                break;
            case DB:
                logAction(dbLoggerPath, logMessage);
                break;
        }
    }

    private static void logAction(Path pathToFile, String logMessage){

        /*
        first check if the folder "logs" is present
        if it is, write to the file pointed to by the path
        else, create the folder then the method Files.writeString will create the txt file if it not found
         */

        if(!checkIfFolderExists()){
            /*
            tre sa facem folderul
             */
            try {
                Files.createDirectory(systemPathToLogsFolder);
                folderExists = true;
            } catch (IOException ioException) {
                CustomAlertService.getExceptionAlert(ioException);
            }
        }

        if (folderExists){
            try {
                writeNewMessage(pathToFile, logMessage);
            }catch (IOException ioException){
                CustomAlertService.getExceptionAlert(ioException);
            }
        }
    }

    private static void writeNewMessage(Path pathToFile, String logMessage) throws IOException{

        if(!Files.exists(pathToFile)){
            /*
            trebuie initial facut fisierul
             */
            String[] tempTokens = logMessage.split(",");
            AtomicReference<String> newLogMessage = new AtomicReference<>("");
            Arrays.stream(tempTokens).forEach(tempToken -> {
                assert false;
                newLogMessage.set(String.format("%s",
                        tempToken));
            });

            logMessage = newLogMessage.get();
            writeTheMessage(pathToFile, logMessage);
        }
        else{
            /*
            incerc sa scriu mereu la inceputul fisierului
             */
            try {
                Stream<String> lines = Files.lines(pathToFile);
                List<String> lineList = new ArrayList<>();
                lines.forEach(lineList::add);
                Files.delete(pathToFile);

                StringBuilder stringBuilder = new StringBuilder();
//                lineList.forEach(line -> stringBuilder.append(String.format("%s\n", line)));
                int size = lineList.size();
                for(int index = 0; index < size; index++){
                    if(index == size - 1){
                        stringBuilder.append(String.format("%s", lineList.get(index)));
                    }
                    else {
                        stringBuilder.append(String.format("%s\n", lineList.get(index)));
                    }
                }

                String[] tempTokens = logMessage.split(",");
                AtomicReference<String> newLogMessage = new AtomicReference<>("");
//                Arrays.stream(tempTokens).forEach(tempToken -> {
//                    assert false;
//                    newLogMessage.set(String.format("%1$s%2$s",
//                            newLogMessage.get(),
//                            tempToken));
//                });

                if(tempTokens.length == 3) {
                    for (int index = 0; index < 3; index++) {
                        if (index == 2) {
                            newLogMessage.set(String.format("%s", tempTokens[index]));
                        } else {
                            newLogMessage.set(String.format("%s\n", tempTokens[index]));
                        }
                    }

                    logMessage = newLogMessage.get();
                }

                logMessage = String.format("%1$s\n%2$s",
                        logMessage,
                        stringBuilder);

                writeTheMessage(pathToFile, logMessage);
            }catch (Exception exception){
                CustomAlertService.getExceptionAlert(exception);
            }
        }
    }

    private static void writeTheMessage(Path pathToFile, String logMessage) throws IOException{

        Files.writeString(pathToFile,
                String.format("          Date: %1$s - Time: %2$s\n%3$s",
                        LocalDate.now(),
                        LocalTime.now()
                                .truncatedTo(ChronoUnit.SECONDS)
                                .format(DateTimeFormatter.ISO_LOCAL_TIME),
                        logMessage),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    private static boolean checkIfFolderExists(){

        if(Files.exists(systemPathToLogsFolder)){
            folderExists = true;
            return true;
        }

        folderExists = false;
        return false;
    }
}