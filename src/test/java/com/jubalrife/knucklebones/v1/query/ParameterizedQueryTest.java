package com.jubalrife.knucklebones.v1.query;

import com.jubalrife.knucklebones.v1.exception.KnuckleBonesException;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ParameterizedQueryTest {
    @Test(expected = KnuckleBonesException.ParameterNotSet.class)
    public void expectExceptionWhenAParameterIsSpecifiedButNotProvided() {
        ParameterizedQuery.create(":a", Collections.emptyMap());
    }

    @Test(expected = KnuckleBonesException.ListParameterWasEmpty.class)
    public void expectExceptionWhenAParameterIsAnEmptyList() {
        ParameterizedQuery.create(":a", Collections.singletonMap("a", Collections.emptyList()));
    }

    @Test
    public void givenEmptyParameterMapExpectEmptyParameters() {
        ParameterizedQuery query = ParameterizedQuery.create("query", Collections.emptyMap());
        assertThat(query.getQuery(), is("query"));
        assertThat(query.getParameters(), is(Collections.emptyList()));
    }

    @Test
    public void givenSingleParameterExpectQuestionMarkAndParameterInList() {
        ParameterizedQuery query = ParameterizedQuery.create(":a", Collections.singletonMap("a", 1));
        assertThat(query.getQuery(), is("?"));
        assertThat(query.getParameters(), is(Collections.singletonList(1)));
    }

    @Test
    public void givenSameParameterMultipleTimesExpectQuestionMarksAndParameterInListRepeated() {
        ParameterizedQuery query = ParameterizedQuery.create(":a,:a", Collections.singletonMap("a", 1));
        assertThat(query.getQuery(), is("?,?"));
        assertThat(query.getParameters(), is(Arrays.asList(1, 1)));
    }

    @Test
    public void givenMultipleParametersExpectQuestionMarksAndParameterInList() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("a", 1);
        parameters.put("b", 2);

        ParameterizedQuery query = ParameterizedQuery.create(":a,:b", parameters);

        assertThat(query.getQuery(), is("?,?"));
        assertThat(query.getParameters(), is(Arrays.asList(1, 2)));
    }

    @Test
    public void givenCollectionContainingSingleItemExpectOneQuestionMarkAndValueInList() {
        ParameterizedQuery query = ParameterizedQuery.create(":a", Collections.singletonMap("a", Collections.singletonList(1)));
        assertThat(query.getQuery(), is("?"));
        assertThat(query.getParameters(), is(Collections.singletonList(1)));
    }

    @Test
    public void givenCollectionContainingMultipleItemsExpectCommaSeparatedQuestionMarksAndValuesInList() {
        ParameterizedQuery query = ParameterizedQuery.create(":a", Collections.singletonMap("a", Arrays.asList(1, 2, 3)));
        assertThat(query.getQuery(), is("?,?,?"));
        assertThat(query.getParameters(), is(Arrays.asList(1, 2, 3)));
    }
}