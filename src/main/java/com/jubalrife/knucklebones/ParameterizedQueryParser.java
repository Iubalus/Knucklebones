package com.jubalrife.knucklebones;


import com.jubalrife.knucklebones.ParameterizedQueryLexer.ParameterizedQueryToken;
import com.jubalrife.knucklebones.exception.KnuckleBonesException;

import java.util.LinkedList;

/**
 * query := (Text | parameter)+
 * parameter := COLON Name
 */
public class ParameterizedQueryParser {
    private final ParameterizedQueryLexer lexer;
    private final Listener listener;
    private LinkedList<ParameterizedQueryToken> buffer = new LinkedList();

    public ParameterizedQueryParser(ParameterizedQueryLexer lexer, Listener listener) {
        this.lexer = lexer;
        this.listener = listener;
    }

    public void parse() {
        while (!isEOF()) {
            parseParameter();
            parseText();
        }
    }

    public void parseParameter() {
        ParameterizedQueryToken colon = next();
        if (colon.getType() != ParameterizedQueryLexer.TokenType.COLON) {
            push(colon);
            return;
        }

        ParameterizedQueryToken name = next();
        if (name.getType() != ParameterizedQueryLexer.TokenType.NAME) {
            throw new InvalidParameter(name);
        }

        listener.parameter(colon, name);
    }

    public void parseText() {
        ParameterizedQueryToken text = next();
        if (text.getType() == ParameterizedQueryLexer.TokenType.EOF) {
            return;
        }
        if (text.getType() == ParameterizedQueryLexer.TokenType.NAME) {
            return;//Lexer Prevents this
        }

        if (text.getType() == ParameterizedQueryLexer.TokenType.COLON) {
            push(text);
            return;
        }


        listener.text(text);
    }

    public boolean isEOF() {
        ParameterizedQueryToken eof = next();
        if (eof.getType() != ParameterizedQueryLexer.TokenType.EOF) {
            push(eof);
            return false;
        }
        return true;
    }

    private ParameterizedQueryToken next() {
        if (!buffer.isEmpty()) {
            return buffer.pop();
        }

        return lexer.next();
    }

    private void push(ParameterizedQueryToken token) {
        buffer.push(token);
    }

    public interface Listener {
        void text(ParameterizedQueryToken token);

        void parameter(ParameterizedQueryToken colon, ParameterizedQueryToken name);
    }

    static class ParameterizedQueryException extends KnuckleBonesException {
        public ParameterizedQueryException(String message, int line, int linePosition) {
            super(String.format("[Line: %d, Position %d]: %s", line, linePosition, message));
        }
    }

    static class InvalidParameter extends ParameterizedQueryException {
        public InvalidParameter(ParameterizedQueryToken token) {
            super("[:] Must be followed by a parameter name of the form: [a-zA-Z]+", token.getLine(), token.getPosition());
        }
    }
}
