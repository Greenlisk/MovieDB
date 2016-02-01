/**
 * Created by iserbai on 28.01.16.
 */
package com.company;
import com.company.DataProcessor.*;

import java.io.IOException;


public class Main {
    public static void main(String[] args){
        /*DataProcessor dataProcessor = new DataProcessor();
        dataProcessor.processText("input_file.txt", "output_file.txt");
        dataProcessor.processData("input_file.data");*/
        try {
            AuthSystem authSystem = new AuthSystem("/home/iserbai/Projects/Java/Java-training-project/LangTraining/UsersDir");
            if (authSystem.authenticate()) {
                System.out.println("Authentication succeeded");
            } else {
                System.out.println("Authentication failed");
            }
        } catch (IOException ioex) {
            System.err.println("Something wrong with files))): " + ioex.getMessage());
            StackTraceElement[] stackTraceElements  = ioex.getStackTrace();
            for (StackTraceElement element : stackTraceElements) {
                System.err.println(element.getMethodName());
            }

        }
    }

}

