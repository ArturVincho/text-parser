package com.va.reader;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
public class ReadingFile {

    private String fileContent;

    /**
     * Reading a file from a user
     *
     * @param filename String
     * @return fileContent
     */
    public String readFile(String filename) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(filename), StandardCharsets.UTF_8))) {
            String sub;
            while ((sub = br.readLine()) != null) {
                fileContent = String.join(" \n ", this.fileContent, sub);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileContent.replaceAll("null", "").replace("\n", " ");
        return fileContent;
    }

}


