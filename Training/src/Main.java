/**
 * Created by iserbai on 28.01.16.
 */
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        FileInputStream input;
        FileOutputStream output;
        try {
            input = new FileInputStream("input_file.txt");
            output = new FileOutputStream("output_file.txt");

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            System.out.println("StackTrace : ");
             StackTraceElement[] stackElements = ex.getStackTrace();
            for (StackTraceElement str : stackElements) {
                System.out.print("" + str.getLineNumber());
                System.out.println("" + str.getMethodName());
            }
        }



    }
}
