/**
 * Created by iserbai on 28.01.16.
 */
package com.company;
import com.company.DataProcessor.*;

import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        /*DataProcessor dataProcessor = new DataProcessor();
        dataProcessor.processText("input_file.txt", "output_file.txt");
        dataProcessor.processData("input_file.data");*/
        try {
            AuthSystem authSystem = new AuthSystem("~/Projects/Java/Java-training-project/LangTraining/UsersDir");
            authSystem.authenticate();
        } catch (IOException ioex) {
            System.err.println("Something wrong with files)))");
        }
    }

}

