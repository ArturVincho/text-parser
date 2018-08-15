package com.main.reader;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ReadingFile {


    private String fileContent;



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


