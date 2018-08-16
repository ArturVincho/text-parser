package com.main.anaylzer;

import com.main.replace.ReplaceSymbol;
import com.sun.javafx.util.Logging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Stack;
import java.util.logging.Logger;

@Component
public class BracketsAnalyzer {

    @Autowired
    private ReplaceSymbol replaceSymbol;

    private Logger logger = Logger.getLogger(Logging.class.getName());

    /**
     * Analysis of the correctness of the separation of brackets
     * <p>
     * Accept a text string from the ReadingFile class.
     * Using a stack, check the matching of brackets
     * </p>
     *
     * @param content String
     * @return return a message about the correctness of the brackets
     */

    public String checkString(String content) {
        logger.info("Starting analyze brackets");
        String replace = replaceSymbol.replaceSymbol(content).replaceAll("\\w|\\d\\s", "");
        logger.info("Removed all letters and extra characters");
        String brackets = "";
        for (int i = 0; i < replace.length(); i++) {
            char a = replace.charAt(i);
            if (a == '(' || a == ')' || a == '[' || a == ']' || a == '{' || a == '}') {
                brackets += a;
            }
        }
        Stack<Character> stack = new Stack<>();
        char topStackChar = 0;
        if (brackets.length() == 0) {
            logger.info("There is no brackets in the file");
            return "В файле скобки отсутствуют";
        }
        checkForMatchingBraces(brackets, stack, topStackChar);
        logger.info("Brackets analysis is finished");
        return stack.isEmpty() ? "расстановка скобок верна." : "расстановка скобок не верна.";
    }

    private void checkForMatchingBraces(String brackets, Stack<Character> stack, char topStackChar) {
        for (int i = 0; i < brackets.length(); i++) {
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
    }
}
