package org.comptrans.lexer;

public class LexicalHelper {

    public interface TokenType {
        String KEY_WORD = "key_word";
        String DELIMITER = "delimiter";
        String COMMENT = "comment";
        String IDENTIFIER = "identifier";
        String OPERATOR = "operator";
        String INT_CONSTANT = "int_constant";
        String FLOAT_CONSTANT = "float_constant";
        String LITERAL = "literal";
    }
    public boolean multiLineComment = false;
    public int multiLineCommentLineNumber = -1;

    public String parseLine(String line, int lineNumber) throws Exception {

        if(!validateLine(line)) {
            throw new Exception("Line number " + lineNumber + " contains illegal characters!");
        }

        StringBuilder output = new StringBuilder();

        char[] lineCharArray = line.toCharArray();

        boolean lineComment = false;

        // if there is "something" in line
        boolean string = false;

        String token = "";
        for (int i = 0 ; i < lineCharArray.length ; i++) {
            char nextCharacter = (i + 1 < lineCharArray.length) ? lineCharArray[i + 1] : '\0';
            if(lineCharArray[i] == ' ' && !string) {
                continue;
            }
            token += lineCharArray[i];
            if(!lineComment && !this.multiLineComment) {
                if ((token + nextCharacter).equals("//")) {
                    lineComment = true;
                    i++;
                    token = "";
                } else if ((token + nextCharacter).equals("/*")) {
                    this.multiLineComment = true;
                    this.multiLineCommentLineNumber = lineNumber;
                    i++;
                    token = "";
                } else if ((token + nextCharacter).equals("*/")) {
                    throw new Exception("Line number " + lineNumber + " contains multiline comment ending without multiline comment context!");
                } else if (JavaLanguageConstants.javaKeyWords.contains(token) && !JavaLanguageConstants.javaKeyWords.contains(token + nextCharacter)) {
                    output.append(constructOutputForToken(token, TokenType.KEY_WORD, lineNumber));
                    token = "";
                } else if (JavaLanguageConstants.delimiters.contains(token)) {
                    output.append(constructOutputForToken(token, TokenType.DELIMITER, lineNumber));
                    token = "";
                } else if (token.matches("[a-zA-Z]+")
                        && !Character.isAlphabetic(nextCharacter)
                        && !Character.isDigit(nextCharacter)) {
                    output.append(constructOutputForToken(token, TokenType.IDENTIFIER, lineNumber));
                    token = "";
                } else if (JavaLanguageConstants.operators.contains(token) &&
                        !JavaLanguageConstants.operators.contains(token + nextCharacter)) {
                    output.append(constructOutputForToken(token, TokenType.OPERATOR, lineNumber));
                    token = "";
                } else if (token.matches("[0-9]+") &&
                        !token.contains(".") &&
                        nextCharacter != '.' &&
                        !Character.isDigit(nextCharacter)) {
                    output.append(constructOutputForToken(token, TokenType.INT_CONSTANT, lineNumber));
                    token = "";
                } else if (token.matches("[0-9.]+") &&
                        token.contains(".") &&
                        !Character.isDigit(nextCharacter)) {
                    if (token.equals(".")) {
                        output.append(constructOutputForToken(token, TokenType.DELIMITER, lineNumber));
                    } else {
                        output.append(constructOutputForToken(token, TokenType.FLOAT_CONSTANT, lineNumber));
                    }
                    token = "";
                } else if (token.equals("\"")) {
                    string = true;
                } else if (string && token.charAt(token.length()-1) == '\"' && token.length() > 1) {
                    output.append(constructOutputForToken(token.substring(1, token.length()-1), TokenType.LITERAL, lineNumber));
                    string = false;
                    token = "";
                }
            } else if (lineComment) {
                output.append(constructOutputForToken(line.substring(i), TokenType.COMMENT, lineNumber));
                break;
            } else {
                if (!(line.substring(i).contains("*/"))) {
                    output.append(constructOutputForToken(line.substring(i).trim(), TokenType.COMMENT, lineNumber));
                    break;
                } else {
                    for (int j = i + 1 ; j < lineCharArray.length ; j++) {
                        token += lineCharArray[j];
                        if (token.endsWith("*/")) {
                            this.multiLineComment = false;
                            output.append(constructOutputForToken(token.substring(0, token.length()-2).trim(), TokenType.COMMENT, lineNumber));
                            token = "";
                            i = j + 2;
                        }
                    }
                }
            }
        }

        return output.toString();
    }

    public String constructOutputForToken(String token, String tokenType, int lineNumber) {
        return "'" + token + "', " + tokenType + "; " + token.length() + "; linia " + lineNumber + "\n";
    }

    public boolean validateLine(String line) {
        String[] lineValidation = line.split("[^\\x00-\\x7F]");
        return lineValidation.length < 2;
    }
}
