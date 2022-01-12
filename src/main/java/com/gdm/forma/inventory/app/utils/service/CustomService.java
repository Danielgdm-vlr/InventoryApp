package com.gdm.forma.inventory.app.utils.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class CustomService {

    public static String[] getExceptionFullMessage(Exception exception){
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);

        List<StackTraceElement> stackTraceElementList =
                new ArrayList<>(List.of(exception.getStackTrace()));

//                Arrays.stream(ioException.getStackTrace()).forEach(
//                        stackTraceElement -> {
//                            if(stackTraceElement.toString().contains("com.gdm.formainventoryapp")){
//                                stackTraceElementList.add(stackTraceElement);
//                            }
//                        }
//                );

        return new String[]{exception.getClass().getSimpleName(),
                exception.getMessage(),
                String.valueOf(stackTraceElementList)};
    }
}
