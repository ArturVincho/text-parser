package com.main.controller;

import com.main.anaylzer.BracketsAnalyzer;
import com.main.anaylzer.WordsAnalyzer;
import com.main.reader.ReadingFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UploadController {

    @Autowired
    private ReadingFile readingFile;
    @Autowired
    private WordsAnalyzer wordsAnalyzer;
    @Autowired
    private BracketsAnalyzer bracketsAnalyzer;
    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "";

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload") // //new annotation since 4.3
    public String fileAnalyzed(@RequestParam("file") MultipartFile file,
                               RedirectAttributes redirectAttributes) {
        if (fileNotFound(file, redirectAttributes)) return "redirect:/uploadError";

        try {
            // Get the file and save it somewhere
            Path path = getPath(file);
            String newFile = new String(path.toString().getBytes(), "UTF-8");
            String readFile = readingFile.readFile(newFile);
            String someWrite = wordsAnalyzer.someWrite(readFile);
            redirectAttributes.addFlashAttribute("message",
                    "Ваш файл загружен и проанализирован. " + "Название вашего файла: " + file.getOriginalFilename());
            redirectAttributes.addFlashAttribute("messageText", "В вашем файле " + someWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/uploadStatus";
    }


    /**
     * @param file
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/analyze")
    public String bracketsAnalyzed(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (fileNotFound(file, redirectAttributes)) return "redirect:/uploadError";
        try {
            Path path = getPath(file);
            String readFile = readingFile.readFile(path.toFile().toString());
            String analyze = bracketsAnalyzer.checkString(readFile);
            redirectAttributes.addFlashAttribute("message",
                    "Ваш файл загружен и проанализирован. " + "Название вашего файла: " + file.getOriginalFilename());
            redirectAttributes.addFlashAttribute("messageAnalyze", "Расстановка скобок в файле: " + analyze);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/uploadStatus";
    }

    @GetMapping("/uploadStatus")
    public String uploadStatus() {
        return "uploadStatus";
    }


    @GetMapping("/uploadError")
    public String localhost() {
        return "uploadError";
    }

    /**
     * @param file
     * @param redirectAttributes
     * @return
     */
    private boolean fileNotFound(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Пожалуйста, выберите файл и повторите еще раз");
            return true;
        }
        return false;
    }

    private Path getPath(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
        Files.write(path, bytes);
        return path;
    }


}