package com.mandark.jira.spi.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Odd ball value mapping utilities
 */
public final class Values {

    private Values() {
        super();
        // Util class
    }


    // Util Methods
    // ------------------------------------------------------------------------

    // Basic

    public static <V> V get(V value, V defaultValue) {
        return Objects.isNull(value) ? defaultValue : value;
    }

    public static <V, R> R get(final V value, final Function<V, R> valueMapper) {
        return Objects.isNull(value) ? null : valueMapper.apply(value);
    }

    public static <V, U, R> R get(final V value1, final U value2, final BiFunction<V, U, R> valueMapper) {
        return Objects.isNull(value1) ? null : valueMapper.apply(value1, value2);
    }


    // Primitives

    public static boolean getBoolean(final Boolean value, final boolean defaultValue) {
        return Objects.isNull(value) ? defaultValue : value;
    }

    public static int getInteger(final Integer value, final int defaultValue) {
        return Objects.isNull(value) ? defaultValue : value;
    }

    public static long getLong(final Long value, final long defaultValue) {
        return Objects.isNull(value) ? defaultValue : value;
    }

    public static double getDouble(final Double value, final double defaultValue) {
        return Objects.isNull(value) ? defaultValue : value;
    }


    // Collections : List

    public static <V> List<V> getList(final Collection<V> values) {
        return getList(values, Function.identity());
    }

    public static <V, R> List<R> getList(final Collection<V> values, Function<V, R> valueMapper) {
        // Sanity checks
        if (Objects.isNull(values) || values.isEmpty()) {
            return new ArrayList<>();
        }

        final List<R> resultList = values.stream() //
                .filter(Objects::nonNull) //
                .map(valueMapper) //
                .collect(Collectors.toList());
        return resultList;
    }


    // Collections : Set

    public static <V> Set<V> getSet(final Collection<V> values) {
        return getSet(values, Function.identity());
    }

    public static <V, R> Set<R> getSet(final Collection<V> values, Function<V, R> valueMapper) {
        // Sanity checks
        if (Objects.isNull(values) || values.isEmpty()) {
            return new HashSet<>();
        }

        final Set<R> resultSet = values.stream() //
                .filter(Objects::nonNull) //
                .map(valueMapper) //
                .collect(Collectors.toSet());
        return resultSet;
    }


    // Collections : Map

    public static <K, V> Map<K, V> getMap(final Map<K, V> valuesMap) {
        return getMap(valuesMap, Function.identity());
    }

    public static <K, V, R> Map<K, R> getMap(final Map<K, V> valuesMap, Function<V, R> valueMapper) {
        // Sanity checks
        if (Objects.isNull(valuesMap) || valuesMap.isEmpty()) {
            return new HashMap<>();
        }

        final Map<K, R> resultMap = valuesMap.entrySet() //
                .stream() //
                .collect(Collectors.toMap( //
                        Map.Entry::getKey, //
                        e -> Objects.isNull(e) ? null : valueMapper.apply(e.getValue()) //
                ));
        return resultMap;
    }


}
