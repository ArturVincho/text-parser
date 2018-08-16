package com.main.anaylzer;

import com.main.replace.ReplaceSymbol;
import com.sun.javafx.util.Logging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class WordsAnalyzer {

    @Autowired
    private ReplaceSymbol replaceSymbol;

    private String finalDuplicate;
    private Logger logger = Logger.getLogger(Logging.class.getName());

    /**
     * Analysis of repetitive words
     * <p>
     * Accept a text string from the ReadingFile class.
     * Find All the same words. Return a string with the same words
     * </p>
     *
     * @param content String
     * @return finalDuplicate String
     */

    public String analysisOfWords(String content) {
        logger.info("Starting finding identical words");
        HashMap<String, Integer> numberOfRepetitions = new HashMap<>();
        List<Integer> mapKeys = new ArrayList<>();
        String replaceCharacter = replaceSymbol.replaceSymbol(content.toLowerCase()).replaceAll("\\pP", " ")
                .replaceAll(" {2}", " ");
        logger.info("All characters except words were deleted");
        HashMap<String, Integer> duplicatesWord = identicalWords(replaceCharacter, numberOfRepetitions);
        filteringNonDuplicateWords(mapKeys, duplicatesWord);
        Collections.sort(mapKeys, Collections.reverseOrder());
        List<Integer> topTenDuplicates = mapKeys.stream().limit(10).collect(Collectors.toList());
        List<String> finalWords = addDuplicateWords(duplicatesWord, topTenDuplicates);
        List<String> collects = new ArrayList<>();
        fillingDuplicateWords(finalWords, collects);
        deleteDuplicateWords(finalWords, collects);
        finalDuplicate = null;
        for (String word : finalWords) {
            finalDuplicate = String.join(" ", this.finalDuplicate, word);
        }
        logger.info("Finished finding identical words");
        return " 10 самых повторяющихся слов: " + finalDuplicate.replaceAll("null", "");
    }

    // Search for the same words and their collection in Map
    private HashMap<String, Integer> identicalWords(String replaceCharacter, HashMap<String, Integer> numberOfRepetitions) {
        HashMap<String, Integer> duplicatesMap = new HashMap<>();
        List<String> duplicateWordsInMap = new ArrayList<>();
        String[] someWords = replaceCharacter.split(" ");
        countingDuplicateWords(numberOfRepetitions, someWords);
        addDuplicateWordsInMap(numberOfRepetitions, duplicatesMap, duplicateWordsInMap);
        return duplicatesMap;
    }

    private void filteringNonDuplicateWords(List<Integer> mapKeys, HashMap<String, Integer> duplicatesWord) {
        for (Map.Entry<String, Integer> setMap : duplicatesWord.entrySet()) {
            if (setMap.getValue() != 1) {
                mapKeys.add(setMap.getValue());
            }
        }
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

    private void fillingDuplicateWords(List<String> finalWords, List<String> collects) {
        for (String change : finalWords) {
            String substring = change.substring(1);
            for (int i = 0; i < finalWords.size(); i++) {
                if (substring.equals(finalWords.get(i))) {
                    collects.add(substring);
                }
            }
        }
    }

    private void deleteDuplicateWords(List<String> finalWords, List<String> collects) {
        for (int i = 0; i < finalWords.size(); i++) {
            for (String collect : collects) {
                if (collect.equals(finalWords.get(i))) {
                    finalWords.remove(i);
                }
            }
        }
    }

    private void countingDuplicateWords(HashMap<String, Integer> numberOfRepetitions, String[] someWords) {
        int key;
        for (String someWord : someWords) {
            if (numberOfRepetitions.containsKey(someWord)) {
                key = numberOfRepetitions.get(someWord);
                numberOfRepetitions.put(someWord, key + 1);

                //Because of a possible blank line in the form of a key,
                // it is necessary to check for the length of the string

            } else if (someWord.trim().length() > 0) {
                numberOfRepetitions.put(someWord, 1);
            }
        }
    }

    /*
        because of the coding features and the first character /ufeff it was necessary to go through the whole map
         and cut the first character in the word if the remaining word coincides with the existing key is added 1
     */
    private void addDuplicateWordsInMap(HashMap<String, Integer> wordsMapping, HashMap<String, Integer> duplicatesMap, List<String> duplicateWordsInMap) {
        for (String setKey : wordsMapping.keySet()) {
            if (wordsMapping.containsKey(setKey) && wordsMapping.get(setKey) == 1) {
                {
                    String substring = setKey.substring(1);
                    duplicateWordsInMap.add(substring);
                }
            }
        }
        for (String checkDuplicate : duplicateWordsInMap) {
            if (wordsMapping.containsKey(checkDuplicate)) {
                Integer value = wordsMapping.get(checkDuplicate);
                wordsMapping.put(checkDuplicate, value + 1);
            }
        }
        for (String duplicateMapKey : wordsMapping.keySet()) {
            if (wordsMapping.get(duplicateMapKey) > 1) {
                duplicatesMap.put(duplicateMapKey, wordsMapping.get(duplicateMapKey));
            }
        }
    }
}
