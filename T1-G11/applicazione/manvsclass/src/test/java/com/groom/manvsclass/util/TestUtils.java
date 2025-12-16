package com.groom.manvsclass.util;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

// classe che implementa metodi di comodo per la logica generica degli assert con una configurazione di comparazione personalizzata
// Questo semplifica di molto il confronto tra model nei test (che non presentano metodi equals built-in),
// soprattutto per liste di oggetti model, sfruttando i metodi containsExactlyInAnyOrderElementsOf e containsExactlyElementsOf di AssertJ
public class TestUtils<T> {

    private final RecursiveComparisonConfiguration comparisonConfig;

    public TestUtils(RecursiveComparisonConfiguration comparisonConfig){
        this.comparisonConfig = comparisonConfig;
    }

    // metodo equals per il confronto tra due oggetti TModel ignorando le relazioni
    public void assertEquals(T expected, T actual) {
        assertThat(actual)
                .usingRecursiveComparison(comparisonConfig)
                .isEqualTo(expected);
    }

    // metodo di confronto per due liste di oggetti TModel
    public void assertListEquals(List<T> expected, List<T> actual) {
        Assertions.assertThat(actual)
                .hasSize(expected.size())
                .usingRecursiveFieldByFieldElementComparator(comparisonConfig)
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    // metodo di confronto per due liste di oggetti TModel assicurandosi che l'ordine sia preservato
    public void assertListSameOrderEquals(List<T> expected, List<T> actual) {
        Assertions.assertThat(actual)
                .hasSize(expected.size())
                .usingRecursiveFieldByFieldElementComparator(comparisonConfig)
                .containsExactlyElementsOf(expected);
    }
}
