package com.jubalrife.knucklebones.v1.query;

import com.jubalrife.knucklebones.v1.query.ParameterizedQueryLexer;
import com.jubalrife.knucklebones.v1.query.ParameterizedQueryParser;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ParameterizedQueryParserTest {

    @Test(expected = ParameterizedQueryParser.InvalidParameter.class)
    public void invalidParameterNoName() {
        parse(":");
    }

    @Test(expected = ParameterizedQueryParser.InvalidParameter.class)
    public void invalidParameterColonNumber() {
        parse(":8");
    }

    @Test(expected = ParameterizedQueryParser.InvalidParameter.class)
    public void invalidParameterColonNameColon() {
        parse(":a:");
    }

    @Test
    public void expectQueryRebuilt() {
        assertThat(parse(""), is(""));
        assertThat(parse("text"), is("text"));
        assertThat(parse(":a"), is(":a"));
        assertThat(parse("text :a:a"), is("text :a:a"));
    }

    private String parse(String input) {
        QueryRebuilder query = new QueryRebuilder();
        new ParameterizedQueryParser(new ParameterizedQueryLexer(input), query).parse();
        return query.toString();
    }

    private static class QueryRebuilder implements ParameterizedQueryParser.Listener {
        StringBuilder query = new StringBuilder();

        @Override
        public void text(ParameterizedQueryLexer.ParameterizedQueryToken token) {
            query.append(token.getText());
        }

        @Override
        public void parameter(ParameterizedQueryLexer.ParameterizedQueryToken colon, ParameterizedQueryLexer.ParameterizedQueryToken name) {
            query.append(":");
            query.append(name.getText());
        }

        public String toString() {
            return query.toString();
        }
    }
}