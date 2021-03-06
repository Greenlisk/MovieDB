package com.company.DataProcessor;

/**
 * Created by iserbai on 31.01.16.
 */

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthSystem {
    private Console console;
    private Path usersDirPath;

    public AuthSystem(String path) throws InvalidPathException {
        console = System.console();
        if (console  == null) {
            System.err.println("Error getting console");
            System.exit(1);
        }
        usersDirPath = Paths.get(path);
        if ( !Files.isDirectory(usersDirPath)) {
            System.err.println("Users directory not found.");
            System.exit(1);
        }
    }

    //void startSession();
    public boolean authenticate() throws IOException {
        String login = console.readLine("Login: ");
        Path userFilePath = Paths.get(usersDirPath.toString(), login);
            if (!Files.exists(userFilePath)) {
                Files.createFile(userFilePath);
                createUser(userFilePath);
            }
        try (DataInputStream input = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream(userFilePath.toString())
                ))) {
            String filePassword = input.readUTF();
            if (filePassword == null) {
                return false;
            }
            String inputPassword = new String(console.readPassword("Password: "));
            if (inputPassword == null ) {
                return false;
            }

            MessageDigest messageDigestConsole = MessageDigest.getInstance("SHA-256");
            messageDigestConsole.update(inputPassword.getBytes());
            String encryptedPass = new String(messageDigestConsole.digest());
            if (encryptedPass.equals(filePassword)) {
                return true;
            } else {
                console.printf("Wrong password!\n");
                return false;
            }

        } catch (NoSuchAlgorithmException nsaex) {
            System.err.println();
        }
        return true;
    }

    boolean createUser(Path outPath) throws IOException {
        boolean incorrectPassword = true;
        try(DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(outPath.toString())
                )
        )){
        while (incorrectPassword) {
            String password = new String(console.readPassword("Please type new password: "));
            if (password.equals(new String(console.readPassword("Repeat new password: ")))) {
                MessageDigest messageDigestConsole = MessageDigest.getInstance("SHA-256");
                messageDigestConsole.update(password.getBytes());
                String encryptedPass = new String(messageDigestConsole.digest());
                out.writeUTF(encryptedPass);
                incorrectPassword = false;
            }
        }
        } catch ( IOException ioex) {
            throw ioex;
        } catch (NoSuchAlgorithmException nsaex) {
            System.err.println("Encryption algorithm error!");
            System.exit(1);
        } finally {

        }

        return true;
    }
}
