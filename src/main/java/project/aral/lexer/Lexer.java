package project.aral.lexer;

import project.aral.token.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Lexer {
    private final String text;
    private int position;
    private char currentChar;
    private int line;

    private static final Set<String> KEYWORDS = Set.of(
            "ózgeriwshi", "san", "únsiz", "eger", "ol", "dım", "bolmasa", "bolsa",
            "qayta", "jáne", "aralıq", "shın", "jalǵan", "shıǵar",
            "hám", "yamasa"
    );

    public Lexer(String text) {
        this.text = text;
        this.position = 0;
        this.line = 1;
        this.currentChar = text.length() > 0 ? text.charAt(0) : '\0';
    }

    private void advance() {
        if (currentChar == '\n') {
            line++;
        }

        position++;

        if (position >= text.length()) {
            currentChar = '\0';
        } else {
            currentChar = text.charAt(position);
        }
    }

    private void skipWhitespace() {
        while (currentChar == ' ' || currentChar == '\t' || currentChar == '\n' || currentChar == '\r') {
            advance();
        }
    }

    private String readWord() {
        StringBuilder word = new StringBuilder();

        while (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
            word.append(currentChar);
            advance();
        }

        return word.toString();
    }

    private Token makeWordToken() {
        String word = readWord();

        TokenType type = KEYWORDS.contains(word) ? TokenType.KEYWORD : TokenType.IDENTIFIER;

        return new Token(type, word, line);
    }

    private String readNumber() {
        StringBuilder number = new StringBuilder();

        while (Character.isDigit(currentChar)) {
            number.append(currentChar);
            advance();
        }

        if (currentChar == '.' && position + 1 < text.length()
                && Character.isDigit(text.charAt(position + 1))) {
            number.append(currentChar);
            advance();

            while (Character.isDigit(currentChar)) {
                number.append(currentChar);
                advance();
            }
        }

        return number.toString();
    }

    private String readString() {
        StringBuilder string = new StringBuilder();

        advance();

        while (currentChar != '"' && currentChar != '\0') {
            if (currentChar == '\\' && peek() == '"') {
                string.append('"');
                advance();
                advance();
            } else {
                string.append(currentChar);
                advance();
            }
        }

        if (currentChar == '\0') {
            throw new RuntimeException("Unterminated string at line " + line);
        }

        advance();

        return string.toString();
    }

    private char peek() {
        int peekPos = position + 1;
        if (peekPos >= text.length()) {
            return '\0';
        }
        return text.charAt(peekPos);
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (currentChar != '\0') {
            // whitespaces
            if (currentChar == ' ' || currentChar == '\t' || currentChar == '\n' || currentChar == '\r') {
                skipWhitespace();
                continue;
            }

            // keyword or identifier
            if (Character.isLetter(currentChar)) {
                tokens.add(makeWordToken());
                continue;
            }

            // number
            if (Character.isDigit(currentChar)) {
                tokens.add(new Token(TokenType.NUMBER, readNumber(), line));
                continue;
            }

            // operators & delimiters
            switch (currentChar) {
                case '+':
                    tokens.add(new Token(TokenType.PLUS, "+", line));
                    advance();
                    break;
                case '-':
                    tokens.add(new Token(TokenType.MINUS, "-", line));
                    advance();
                    break;
                case '*':
                    tokens.add(new Token(TokenType.MULTIPLY, "*", line));
                    advance();
                    break;
                case '/':
                    tokens.add(new Token(TokenType.DIVIDE, "/", line));
                    advance();
                    break;
                case '(':
                    tokens.add(new Token(TokenType.LEFT_PAREN, "(", line));
                    advance();
                    break;
                case ')':
                    tokens.add(new Token(TokenType.RIGHT_PAREN, ")", line));
                    advance();
                    break;
                case '{':
                    tokens.add(new Token(TokenType.LEFT_BRACE, "{", line));
                    advance();
                    break;
                case '}':
                    tokens.add(new Token(TokenType.RIGHT_BRACE, "}", line));
                    advance();
                    break;
                case ';':
                    tokens.add(new Token(TokenType.SEMICOLON, ";", line));
                    advance();
                    break;
                case ':':
                    tokens.add(new Token(TokenType.COLON, ":", line));
                    advance();
                    break;
                case ',':
                    tokens.add(new Token(TokenType.COMMA, ",", line));
                    advance();
                    break;
                case '=':
                    if (peek() == '=') {
                        tokens.add(new Token(TokenType.EQUAL_EQUAL, "==", line));
                        advance();
                        advance();
                    } else {
                        tokens.add(new Token(TokenType.EQUAL, "=", line));
                        advance();
                    }
                    break;

                case '>':
                    if (peek() == '=') {
                        tokens.add(new Token(TokenType.GREATER_OR_EQUAL, ">=", line));
                        advance();
                        advance();
                    } else {
                        tokens.add(new Token(TokenType.GREATER, ">", line));
                        advance();
                    }
                    break;

                case '<':
                    if (peek() == '=') {
                        tokens.add(new Token(TokenType.LESS_OR_EQUAL, "<=", line));
                        advance();
                        advance();
                    } else {
                        tokens.add(new Token(TokenType.LESS, "<", line));
                        advance();
                    }
                    break;

                case '!':
                    if (peek() == '=') {
                        tokens.add(new Token(TokenType.NOT_EQUAL, "!=", line));
                        advance();
                        advance();
                    } else {
                        throw new RuntimeException("Unexpected character '!' at line " + line);
                    }
                    break;
                case '&':
                    if (peek() == '&') {
                        tokens.add(new Token(TokenType.AND, "&&", line));
                        advance();
                        advance();
                    } else {
                        throw new RuntimeException("Unexpected character '&' at line " + line);
                    }
                    break;

                case '|':
                    if (peek() == '|') {
                        tokens.add(new Token(TokenType.OR, "||", line));
                        advance();
                        advance();
                    } else {
                        throw new RuntimeException("Unexpected character '|' at line " + line);
                    }
                    break;
                case '"':
                    tokens.add(new Token(TokenType.STRING, readString(), line));
                    break;
                default:
                    throw new RuntimeException("Unknown character: '" + currentChar + "' at line " + line);
            }
        }

        // EOF
        tokens.add(new Token(TokenType.EOF, "", line));
        return tokens;
    }

}