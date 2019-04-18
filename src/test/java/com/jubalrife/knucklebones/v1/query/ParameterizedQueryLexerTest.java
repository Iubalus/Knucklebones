package com.jubalrife.knucklebones.v1.query;

import com.jubalrife.knucklebones.v1.query.ParameterizedQueryLexer;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ParameterizedQueryLexerTest {
    @Test
    public void givenNullExpectEOFyTokenOnNextToken() {
        ParameterizedQueryLexer lexer = new ParameterizedQueryLexer(null);
        ParameterizedQueryLexer.ParameterizedQueryToken token = lexer.next();

        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.EOF));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(0));
        assertThat(token.getText(), is(""));
    }

    @Test
    public void givenEmptyStringExpectEOFTokenOnNextToken() {
        ParameterizedQueryLexer lexer = new ParameterizedQueryLexer("");
        ParameterizedQueryLexer.ParameterizedQueryToken token = lexer.next();

        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.EOF));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(0));
        assertThat(token.getText(), is(""));
    }

    @Test
    public void givenSingleNonColonStringExpectTextTokenThenEOFToken() {
        ParameterizedQueryLexer lexer = new ParameterizedQueryLexer("a");
        ParameterizedQueryLexer.ParameterizedQueryToken token = lexer.next();

        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.TEXT));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(0));
        assertThat(token.getText(), is("a"));

        token = lexer.next();
        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.EOF));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(1));
        assertThat(token.getText(), is(""));
    }

    @Test
    public void givenSingleColonStringExpectColonTokenThenEOFToken() {
        ParameterizedQueryLexer lexer = new ParameterizedQueryLexer(":");
        ParameterizedQueryLexer.ParameterizedQueryToken token = lexer.next();

        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.COLON));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(0));
        assertThat(token.getText(), is(":"));

        token = lexer.next();
        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.EOF));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(1));
        assertThat(token.getText(), is(""));
    }

    @Test
    public void givenTwoColonStringExpectTwoColonTokenThenEOFToken() {
        ParameterizedQueryLexer lexer = new ParameterizedQueryLexer("::");
        ParameterizedQueryLexer.ParameterizedQueryToken token = lexer.next();

        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.COLON));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(0));
        assertThat(token.getText(), is(":"));

        token = lexer.next();
        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.COLON));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(1));
        assertThat(token.getText(), is(":"));

        token = lexer.next();
        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.EOF));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(2));
        assertThat(token.getText(), is(""));
    }

    @Test
    public void givenColonNameStringExpectColonTokenNameTokenThenEOFToken() {
        ParameterizedQueryLexer lexer = new ParameterizedQueryLexer(":name");
        ParameterizedQueryLexer.ParameterizedQueryToken token = lexer.next();

        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.COLON));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(0));
        assertThat(token.getText(), is(":"));

        token = lexer.next();
        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.NAME));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(1));
        assertThat(token.getText(), is("name"));

        token = lexer.next();
        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.EOF));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(5));
        assertThat(token.getText(), is(""));
    }

    @Test
    public void givenColonNameColonStringExpectColonTokenNameTokenThenEOFToken() {
        ParameterizedQueryLexer lexer = new ParameterizedQueryLexer(":n:");
        ParameterizedQueryLexer.ParameterizedQueryToken token = lexer.next();

        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.COLON));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(0));
        assertThat(token.getText(), is(":"));

        token = lexer.next();
        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.NAME));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(1));
        assertThat(token.getText(), is("n"));

        token = lexer.next();
        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.COLON));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(2));
        assertThat(token.getText(), is(":"));

        token = lexer.next();
        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.EOF));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(3));
        assertThat(token.getText(), is(""));
    }

    @Test
    public void givenTextColonStringExpectTextTokenColonTokenEOFToken() {
        ParameterizedQueryLexer lexer = new ParameterizedQueryLexer("a:");
        ParameterizedQueryLexer.ParameterizedQueryToken token = lexer.next();

        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.TEXT));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(0));
        assertThat(token.getText(), is("a"));

        token = lexer.next();
        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.COLON));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(1));
        assertThat(token.getText(), is(":"));

        token = lexer.next();
        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.EOF));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(2));
        assertThat(token.getText(), is(""));
    }

    @Test
    public void givenColonTextStringExpectColonTokenTextTokenEOFToken() {
        ParameterizedQueryLexer lexer = new ParameterizedQueryLexer(": ");
        ParameterizedQueryLexer.ParameterizedQueryToken token = lexer.next();

        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.COLON));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(0));
        assertThat(token.getText(), is(":"));

        token = lexer.next();
        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.TEXT));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(1));
        assertThat(token.getText(), is(" "));

        token = lexer.next();
        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.EOF));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(2));
        assertThat(token.getText(), is(""));
    }

    @Test
    public void givenLineBreaksExpectLineToBeIncreasedEOFToken() {
        ParameterizedQueryLexer lexer = new ParameterizedQueryLexer("\n\n");
        ParameterizedQueryLexer.ParameterizedQueryToken token = lexer.next();

        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.TEXT));
        assertThat(token.getLine(), is(0));
        assertThat(token.getPosition(), is(0));
        assertThat(token.getText(), is("\n\n"));

        token = lexer.next();
        assertThat(token.getType(), is(ParameterizedQueryLexer.TokenType.EOF));
        assertThat(token.getLine(), is(2));
        assertThat(token.getPosition(), is(0));
        assertThat(token.getText(), is(""));
    }
}