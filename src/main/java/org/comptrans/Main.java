package org.comptrans;

import org.comptrans.helper.LexicalHelper;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        File input = new File(Main.class.getClassLoader().getResource("input.txt").toURI());
        Scanner scanner = new Scanner(input);

        int lineNumber = 1;

        LexicalHelper lexicalHelper = new LexicalHelper();

        StringBuilder result = new StringBuilder();

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            result.append(lexicalHelper.parseLine(line, lineNumber));
            lineNumber++;
        }

        if(lexicalHelper.multiLineComment) {
            throw new Exception("Multi line comment started at line " + lexicalHelper.multiLineCommentLineNumber + " in input file is unclosed");
        }

        System.out.println(result);
    }
}