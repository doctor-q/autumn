package cc.doctor.framework.utils;


import cc.doctor.framework.entity.Function;

import java.util.*;

/**
 * Created by doctor on 2017/7/12.
 */
public class CollectionUtils {

    private CollectionUtils() {
    }

    public static <T> Set<T> asSet(T[] array) {
        Set<T> tSet = new HashSet<>();
        for (T t : array) {
            tSet.add(t);
        }
        return tSet;
    }

    public static <F,T>List<T> transform(Collection<F> froms, Function<F,T> function) {
        List<T> tList = new LinkedList<>();
        for (F from : froms) {
            tList.add(function.transform(from));
        }
        return tList;
    }

    // lazy transform
    public static <F,T>Iterable<T> transform(final Iterable<F> froms, final Function<F,T> function) {
        final Iterator<F> iterator = froms.iterator();
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public T next() {
                        return function.transform(iterator.next());
                    }

                    @Override
                    public void remove() {
                    }
                };
            }
        };
    }

    public abstract static class JoinerFilter<F, T> {
        public abstract T filter(F item);
        public boolean apply(F value) {
            return true;
        }
    }

    public static <F> String join(String on, F ... items) {
        List<F> fs = Arrays.asList(items);
        return join(fs, on);
    }

    public static <F> String join(Iterable<F> items, String on) {
        return join(items, on, null);
    }

    public static <F, T> String join(Iterable<F> items, String on, JoinerFilter<F, T> joinerFilter) {
        StringBuilder join = new StringBuilder();
        for (F item : items) {
            if (joinerFilter != null) {
                if (joinerFilter.apply(item)) {
                    join.append(joinerFilter.filter(item));
                    join.append(on);
                }
            } else {
                join.append(item);
                join.append(on);
            }
        }
        if (join.length() == 0) {
            return join.toString();
        }
        return join.substring(0, join.length() - on.length());
    }
}
