package org.comptrans;

import org.comptrans.afn.AutomatFinitNedet;
import org.comptrans.helper.LexicalHelper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        tema2();
    }

    private static void tema1() throws Exception {
        File input = new File(Main.class.getClassLoader().getResource("input1.txt").toURI());
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

    private static void tema2() throws Exception {
        try ( FileReader fileReader = new FileReader(Main.class.getClassLoader().getResource("afn.json").getFile()) ) {
            JSONParser jsonParser = new JSONParser();
            Object obj = jsonParser.parse(fileReader);
            JSONObject jsonObject = (JSONObject) obj;

            AutomatFinitNedet automatFinitNedet = new AutomatFinitNedet(jsonObject);

            File input = new File(Main.class.getClassLoader().getResource("input2.txt").toURI());
            Scanner scanner = new Scanner(input);
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(automatFinitNedet.validateStringInAlphabet(line));
            }
        }
    }

}