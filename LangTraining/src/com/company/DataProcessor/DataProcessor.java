package com.company.DataProcessor;

import java.io.*;
import java.util.Scanner;

/**
 * Created by iserbai on 31.01.16.
 */
public class DataProcessor{
    public void processText(String input_str, String output_str) {
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

    public void processData(String output_str) {
        String[] notes = {"Stupid note",
                "Silly note",
                "Template note",
                "Interesting note",
                "Smart note"};
        int[] budget = {10000, 8000, 7500, 4000, 2500};
        boolean[] useful = {true, false, false, true, false};
        try (DataInputStream input = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream(output_str)
                ));
             DataOutputStream output = new DataOutputStream(
                     new BufferedOutputStream(
                             new FileOutputStream(output_str)
                     )
             )) {
            for (int i = 0; i < notes.length; ++i) {
                output.writeBoolean(useful[i]);
                output.writeInt(budget[i]);
                output.writeUTF(notes[i]);
            }
            output.close();
            System.out.println("Data Written");

            while(true) {
                boolean boo = input.readBoolean();
                int money = input.readInt();
                String note = input.readUTF();
                System.out.println("- " + boo + " " + money + " " + note);
            }
        } catch (EOFException ex) {
            System.err.println("Exceptional situation: " + ex.getMessage());
        } catch (IOException ioex) {
            System.err.println("Exceptional situation1: " + ioex.getMessage());
        }
        //No need of final due to usage of try-with-resources construction
    }
}
