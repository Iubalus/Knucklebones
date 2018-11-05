package com.jubalrife.knucklebones;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParameterizedQuery {
    private final String raw;
    private final String query;
    private final List<String> parameterNames;
    private HashMap<String, Object> parameters = new HashMap<>();

    public ParameterizedQuery(String raw, String query, List<String> parameterNames) {
        this.raw = raw;
        this.query = query;
        this.parameterNames = parameterNames;
    }

    public List<Object> getParameters() throws SQLException {
        List<Object> params = new ArrayList<>();
        for (int i = 0; i < parameterNames.size(); i++) {
            params.add( parameters.get(parameterNames.get(i)));
        }
        return params;
    }

    public void setParameters(HashMap<String, Object> parameters) {
        this.parameters = parameters;
    }

    public String getRaw() {
        return raw;
    }

    public String getQuery() {
        return query;
    }

    public static ParameterizedQuery create(String text) {
        QuestionMarkQueryBuilder listener = new QuestionMarkQueryBuilder();

        new ParameterizedQueryParser(new ParameterizedQueryLexer(text), listener).parse();

        return new ParameterizedQuery(text, listener.getQuery(), listener.getParameterNames());
    }

    private static class QuestionMarkQueryBuilder implements ParameterizedQueryParser.Listener {
        StringBuilder query = new StringBuilder();
        List<String> parameterNames = new ArrayList<>();

        @Override
        public void text(ParameterizedQueryLexer.ParameterizedQueryToken token) {
            query.append(token.getText());
        }

        @Override
        public void parameter(ParameterizedQueryLexer.ParameterizedQueryToken colon, ParameterizedQueryLexer.ParameterizedQueryToken name) {
            query.append("?");
            parameterNames.add(name.getText());
        }

        public String getQuery() {
            return query.toString();
        }

        public List<String> getParameterNames() {
            return parameterNames;
        }
    }
}
