package com.main.anaylzer;

import com.main.replace.ReplaceSymbol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class WordsAnalyzer {

    @Autowired
    private ReplaceSymbol replaceSymbol;

    private String finalDuplicate;

    public String someWrite(String content) {
        HashMap<String, Integer> numberOfRepetitions = new HashMap<>();
        List<Integer> mapKeys = new ArrayList<>();
        String replaceCharacter = replaceSymbol.replaceSymbol(content.toLowerCase()).replaceAll("\\pP", " ")
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
            finalDuplicate = String.join(", ", this.finalDuplicate, word);
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

    private HashMap<String, Integer> identicalWords(String replaceCharacter, HashMap<String, Integer> numberOfRepetitions) {
        HashMap<String, Integer> duplicatesMap = new HashMap<>();
        List<String> duplicateWordsInMap = new ArrayList<>();
        String[] someWords = replaceCharacter.split(" ");
        countingDuplicateWords(numberOfRepetitions, someWords);
        addDuplicateWordsInMap(numberOfRepetitions, duplicatesMap, duplicateWordsInMap);
        return duplicatesMap;
    }

    private void countingDuplicateWords(HashMap<String, Integer> numberOfRepetitions, String[] someWords) {
        int key;
        for (String someWord : someWords) {
            if (numberOfRepetitions.containsKey(someWord)) {
                key = numberOfRepetitions.get(someWord);
                numberOfRepetitions.put(someWord, key + 1);
            } else if (someWord.trim().length() > 0) {
                numberOfRepetitions.put(someWord, 1);
            }
        }
    }

    private void addDuplicateWordsInMap(HashMap<String, Integer> numberOfRepetitions, HashMap<String, Integer> duplicatesMap, List<String> duplicateWordsInMap) {
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
        for (String duplicateMapKey : numberOfRepetitions.keySet()) {
            if (numberOfRepetitions.get(duplicateMapKey) > 1) {
                duplicatesMap.put(duplicateMapKey, numberOfRepetitions.get(duplicateMapKey));
            }
        }
    }
}
