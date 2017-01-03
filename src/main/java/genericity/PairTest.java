package genericity;

import java.util.*;

/**
 * Created by yek on 2016-12-29.
 */
public class PairTest {
    public static void main(String[] args) {
        GregorianCalendar[] birthdays = {
                new GregorianCalendar(2011,Calendar.JANUARY,12),
                new GregorianCalendar(2011,Calendar.JANUARY,10),
                new GregorianCalendar(2011,Calendar.FEBRUARY,11)
        };
        Pair<GregorianCalendar> pair = ArrayAlg.minmax(birthdays);
        System.out.println(pair.getFirst().getTime());
        System.out.println(pair.getSecond().getTime());
    }
}

class ArrayAlg {
    public static <T extends Comparable> Pair<T> minmax(T[] a) {
        if (a == null || a.length == 0)
            return null;
        T min = a[0];
        T max = a[0];
        for (int i = 1; i < a.length; i++) {
            if (min.compareTo(a[i]) > 0) min = a[i];
            if (max.compareTo(a[i]) < 0) max = a[i];
        }

        return new Pair<T>(min, max);
    }
}

class Pair<T> {
    private T first;
    private T second;

    public Pair() {
        first = null;
        second = null;
    }

    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public T getSecond() {
        return second;
    }

    public void setSecond(T second) {
        this.second = second;
    }
}
