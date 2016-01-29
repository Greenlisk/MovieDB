/**
 * Created by iserbai on 28.01.16.
 */
package com.company;

import java.io.*;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        try (Scanner input =
                     new Scanner(new BufferedReader(new FileReader("input_file.txt")));
             PrintWriter output = new PrintWriter(new FileWriter("output_file.txt"))) {
            String str;

            System.out.println("Byte stream: ");
            int mul = 1;
            while(input.hasNext()) {
                if (input.hasNextInt()) {
                    mul *= input.nextInt();
                }

                if(input.hasNextBoolean()) {
                    mul *= input.nextBoolean() ? 1 : 0;
                }
                if (input.hasNext()) {
                    System.out.println(input.next());
                }
                //output.println(str);
            }
            System.out.println("Result: " + mul);

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
