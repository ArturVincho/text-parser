package com.main.reader;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ReadingFile {

    private static final String EXCUSES = "(?<!\\S)(?:это|как|так|и|в|над|к|до|не|на|но|за|то|с|ли|а|во|от|со|для|о|же|ну|вы|бы|что|кто|он|она)(?!\\S)";
    private static final String PRONOUNS = "(?<!\\S)(?:я|мы|ты|вы|он|она|оно|они|мой|моя|мое|мои|наш|наша|наше|наши|твой|твоя|твое|твои|ваш|ваша|ваше|ваши|его|её|их|" + "кто|что|какой|каков|чей|который|сколько|где|когда|куда|откуда|зачем|столько|этот|тот|такой|таков|тут|здесь|сюда|туда|оттуда|отсюда|тогда|поэтому|затем|" + "весь|всякий|все|сам|самый|каждый|любой|другой|иной|всяческий|всюду|везде|всегда|никто|ничто|некого|нечего|никакой|ничей|некто|нечто|некий|некоторый|" + "несколько|кое-кто|кое-что|кое-куда|какой-либо|сколько-нибудь|куда-нибудь|зачем-нибудь|чей-либо)(?!\\S)";
    private String fileContent;
    private String finalDuplicate;


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

    public String checkString(String content) {
        String replace = replaceSymbol(content).replaceAll("\\w|\\d\\s", "");
        String brackets = "";
        for (int i = 0; i < replace.length(); i++) {
            char a = replace.charAt(i);
            if (a == '(' || a == ')' || a == '[' || a == ']' || a == '{' || a == '}') {
                brackets += a;
            }
        }
        Stack<Character> stack = new Stack<>();
        char topStackChar = 0;
        for (int i = 0; i < brackets.length(); i++) {
            if (brackets.length() == 0){
                return "В файле скобки отсутствуют";
            }
            if (!stack.isEmpty()) {
                topStackChar = stack.peek();
            }
            stack.push(brackets.charAt(i));
            if (!stack.isEmpty() && stack.size() > 1) {
                if ((topStackChar == '[' && stack.peek() == ']') ||
                        (topStackChar == '{' && stack.peek() == '}') ||
                        (topStackChar == '(' && stack.peek() == ')')) {
                    stack.pop();
                    stack.pop();
                }
            }
        }
        return stack.isEmpty() ? "Расстаовка скобок верна" : "Расстаовка скобок не верна";
    }


    public String someWrite(String content) {
        HashMap<String, Integer> numberOfRepetitions = new HashMap<>();
        List<Integer> mapKeys = new ArrayList<>();
        String replaceCharacter = replaceSymbol(content.toLowerCase()).replaceAll("\\pP", " ")
                .replaceAll(" {2}", " ");
        HashMap<String, Integer> duplicatesWord = identicalWords(replaceCharacter, numberOfRepetitions);
        for (Map.Entry<String, Integer> setMap : duplicatesWord.entrySet()) {
            if (setMap.getValue() != 1) {
                mapKeys.add(setMap.getValue());
            }
        }
        Collections.sort(mapKeys, Collections.reverseOrder());
        List<Integer> topTenDuplicates = mapKeys.stream().limit(10).collect(Collectors.toList());
        List<String> finalWords = addDuplicateWords(duplicatesWord, topTenDuplicates);
        for (String word : finalWords) {
            this.finalDuplicate = String.join(", ", this.finalDuplicate, word);
        }
        return " 10 самых повторяющихся слов: " + finalDuplicate.replaceAll("null,", "");
    }

    private List<String> addDuplicateWords(HashMap<String, Integer> numberOfRepetitions, List<Integer> topTenDuplicates) {
        List<String> words = new ArrayList<>();
        List<String> finalDuplicateWord = new ArrayList<>();
        for (Integer topTenDuplicate : topTenDuplicates) {
            for (String keyMap : numberOfRepetitions.keySet()) {
                if (numberOfRepetitions.get(keyMap) == 1) {
                    continue;
                }
                if (finalDuplicateWord.size() == 10) {
                    return finalDuplicateWord;
                }
                if (numberOfRepetitions.get(keyMap).equals(topTenDuplicate)) {
                    words.add(keyMap);
                    finalDuplicateWord = words.stream().distinct().collect(Collectors.toList());
                }
            }
        }
        return finalDuplicateWord;
    }

    private String replaceSymbol(String content) {
        return content.replaceAll("\\n", "").replaceAll("\\r", " ")
                .replaceAll(" {2}", " ").replaceAll(EXCUSES, "").replaceAll(PRONOUNS, "").replaceAll("(?<!\\S)(?:null| null)(?!\\S)", "");
    }

    private HashMap<String, Integer> identicalWords(String replaceCharacter, HashMap<String, Integer> numberOfRepetitions) {
        int key;
        HashMap<String, Integer> duplicatesMap = new HashMap<>();
        String[] someWords = replaceCharacter.split(" ");
        for (String someWord : someWords) {
            if (numberOfRepetitions.containsKey(someWord)) {
                key = numberOfRepetitions.get(someWord);
                numberOfRepetitions.put(someWord, key + 1);
            } else if (someWord.trim().length() > 0) {
                numberOfRepetitions.put(someWord, 1);
            }
        }
        List<String> duplicateWordsInMap = new ArrayList<>();
        for (String setKey : numberOfRepetitions.keySet()) {
            if (numberOfRepetitions.containsKey(setKey) && numberOfRepetitions.get(setKey) == 1) {
                {
                    String substring = setKey.substring(1);
                    duplicateWordsInMap.add(substring);
                }
            }
        }
        for (String checkDuplicate : duplicateWordsInMap) {
            if (numberOfRepetitions.containsKey(checkDuplicate)) {
                Integer value = numberOfRepetitions.get(checkDuplicate);
                numberOfRepetitions.put(checkDuplicate, value + 1);
            }
        }
        for (String duplicateMapKey: numberOfRepetitions.keySet()){
            if(numberOfRepetitions.get(duplicateMapKey) > 1){
                duplicatesMap.put(duplicateMapKey, numberOfRepetitions.get(duplicateMapKey));
            }
        }
        return duplicatesMap;
    }
}


