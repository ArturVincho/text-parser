package com.main.anaylzer;

import com.main.replace.ReplaceSymbol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Stack;

@Component
public class BracketsAnalyzer {
    @Autowired
    private ReplaceSymbol replaceSymbol;


    public String checkString(String content) {
        String replace = replaceSymbol.replaceSymbol(content).replaceAll("\\w|\\d\\s", "");
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
            return "В файле скобки отсутствуют";
        }
        checkForMatchingBraces(brackets, stack, topStackChar);
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
