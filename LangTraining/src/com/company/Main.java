/**
 * Created by iserbai on 28.01.16.
 */
package com.company;

import java.io.*;



public class Main {
    public static void main(String[] args) {
        try (BufferedReader input =
                     new BufferedReader(new FileReader("input_file.txt"));
             PrintWriter output = new PrintWriter(new FileWriter("output_file.txt"))) {
            String str;

            System.out.println("Byte stream: ");
            while((str = input.readLine()) != null) {
                System.out.println(str);
                output.println(str);
            }

        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            System.out.println("StackTrace : ");
            StackTraceElement[] stackElements = ex.getStackTrace();
            for (StackTraceElement str : stackElements) {
                System.out.print(str.getLineNumber() + ".");
                System.out.println("" + str.getMethodName());
            }
        } finally {
            System.out.println("\nClosing streams...");
        }

    }
}
