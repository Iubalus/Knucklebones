package com.jubalrife.knucklebones.v1.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParameterizedQuery {
    private final String query;
    private final List<Object> parameters;

    public ParameterizedQuery(String query, List<Object> parameters) {
        this.query = query;
        this.parameters = parameters;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public String getQuery() {
        return query;
    }

    public static ParameterizedQuery create(String text, HashMap<String, Object> parameters) {
        QuestionMarkQueryBuilder listener = new QuestionMarkQueryBuilder(parameters);

        new ParameterizedQueryParser(new ParameterizedQueryLexer(text), listener).parse();

        return new ParameterizedQuery(listener.getQuery(), listener.getSqlParameters());
    }

    private static class QuestionMarkQueryBuilder implements ParameterizedQueryParser.Listener {
        StringBuilder query = new StringBuilder();
        private final HashMap<String, Object> parameters;
        private final ArrayList<Object> sqlParameters = new ArrayList<>();

        private QuestionMarkQueryBuilder(HashMap<String, Object> parameters) {this.parameters = parameters;}

        @Override
        public void text(ParameterizedQueryLexer.ParameterizedQueryToken token) {
            query.append(token.getText());
        }

        @Override
        public void parameter(ParameterizedQueryLexer.ParameterizedQueryToken colon, ParameterizedQueryLexer.ParameterizedQueryToken name) {
            String paramName = name.getText();
            Object actualParam = parameters.get(paramName);
            sqlParameters.add(actualParam);
            query.append("?");
        }

        public String getQuery() {
            return query.toString();
        }


        public ArrayList<Object> getSqlParameters() {
            return sqlParameters;
        }
    }
}
