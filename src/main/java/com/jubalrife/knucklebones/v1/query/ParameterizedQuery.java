package com.jubalrife.knucklebones.v1.query;

import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ParameterizedQuery {
    private final String query;
    private final List<Object> parameters;

    private ParameterizedQuery(String query, List<Object> parameters) {
        this.query = query;
        this.parameters = parameters;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public String getQuery() {
        return query;
    }

    public static ParameterizedQuery create(String query, Map<String, Object> parameters) {
        QuestionMarkQueryBuilder listener = new QuestionMarkQueryBuilder(query, parameters);

        new ParameterizedQueryParser(new ParameterizedQueryLexer(query), listener).parse();

        return new ParameterizedQuery(listener.getQuery(), listener.getSqlParameters());
    }

    private static class QuestionMarkQueryBuilder implements ParameterizedQueryParser.Listener {
        private final StringBuilder query = new StringBuilder();
        private final String text;
        private final Map<String, Object> parameters;
        private final ArrayList<Object> sqlParameters = new ArrayList<>();

        private QuestionMarkQueryBuilder(String raw, Map<String, Object> parameters) {
            this.text = raw;
            this.parameters = parameters;
        }

        @Override
        public void text(ParameterizedQueryLexer.ParameterizedQueryToken token) {
            query.append(token.getText());
        }

        @Override
        public void parameter(ParameterizedQueryLexer.ParameterizedQueryToken colon, ParameterizedQueryLexer.ParameterizedQueryToken name) {
            String paramName = name.getText();
            if (!parameters.containsKey(paramName)) {
                throw new KnuckleBonesException.ParameterNotSet(paramName, text);
            }

            Object actualParam = parameters.get(paramName);
            if (actualParam instanceof Collection) {
                Collection collection = (Collection) actualParam;
                if (collection.isEmpty()) {
                    throw new KnuckleBonesException.ListParameterWasEmpty(paramName);
                }

                sqlParameters.addAll(collection);
                String sep = "";
                for (int i = 0; i < collection.size(); i++) {
                    query.append(sep);
                    query.append("?");
                    sep = ",";
                }
            } else {
                sqlParameters.add(actualParam);
                query.append("?");
            }
        }

        public String getQuery() {
            return query.toString();
        }

        public ArrayList<Object> getSqlParameters() {
            return sqlParameters;
        }
    }
}
