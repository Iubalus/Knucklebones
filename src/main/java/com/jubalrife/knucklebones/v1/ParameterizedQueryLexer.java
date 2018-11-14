package com.jubalrife.knucklebones.v1;

import javax.annotation.processing.SupportedSourceVersion;

/**
 * Tokens are defined as
 * <ul>
 * <li>COLON: [:]</li>
 * <li>NAME: [a-zA-Z]+</li>
 * <li>TEXT: .*</li>
 * <li>EOF: EOF</li>
 * </ul>
 */
class ParameterizedQueryLexer {
    private final String text;
    private int index;
    private int line;
    private int linePosition;
    private boolean inParameter;

    public ParameterizedQueryLexer(String text) {
        this.text = text == null ? "" : text;
    }

    public ParameterizedQueryToken next() {
        if (index >= text.length())
            return createToken()
                    .setType(TokenType.EOF)
                    .setText("");

        ParameterizedQueryToken token = createToken();
        char current = text.charAt(index);
        if (current == ':') {
            inParameter = true;
            token.setType(TokenType.COLON)
                    .setText(":");
            advance(current);
            return token;
        }

        if (inParameter) {
            String name = consumeNameToken(current);
            if (!name.isEmpty()) {
                inParameter = false;
                return token
                        .setType(TokenType.NAME)
                        .setText(name);
            }
        }

        return token
                .setType(TokenType.TEXT)
                .setText(consumeText(current));
    }

    private ParameterizedQueryToken createToken() {
        return new ParameterizedQueryToken()
                .setPosition(linePosition)
                .setLine(line);
    }

    private String consumeText(char current) {
        StringBuilder tokenValue = new StringBuilder();
        while (index < text.length() && current != ':') {
            current = text.charAt(index);
            if (current != ':') {
                tokenValue.append(current);
                advance(current);
            }
        }
        return tokenValue.toString();
    }

    private String consumeNameToken(char current) {
        StringBuilder name = new StringBuilder();

        while (index < text.length()) {
            current = text.charAt(index);
            if ((current >= 'A' && current <= 'z')) {
                name.append(current);
                advance(current);
            } else {
                break;
            }
        }
        return name.toString();
    }

    private void advance(char current) {
        if (current == '\n') {
            linePosition = 0;
            line++;
        } else {
            linePosition++;
        }

        index++;
    }


    public enum TokenType {
        COLON,
        NAME,
        TEXT,
        EOF
    }

    public static class ParameterizedQueryToken {
        private TokenType type;
        private String text;
        private int line;
        private int position;

        public TokenType getType() {
            return type;
        }

        public ParameterizedQueryToken setType(TokenType type) {
            this.type = type;
            return this;
        }

        public String getText() {
            return text;
        }

        public ParameterizedQueryToken setText(String text) {
            this.text = text;
            return this;
        }

        public int getLine() {
            return line;
        }

        public ParameterizedQueryToken setLine(int line) {
            this.line = line;
            return this;
        }

        public int getPosition() {
            return position;
        }

        public ParameterizedQueryToken setPosition(int position) {
            this.position = position;
            return this;
        }
    }
}
