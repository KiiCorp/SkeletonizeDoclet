package com.kii.skeletonize_doclet.test;

import com.sun.istack.internal.Nullable;

import java.util.Collection;

/**
 * It's my class!
 * WEEEEI!
 */
public class MyClass <T extends Number, U extends String, V> extends Number implements Comparable<Number> {
    public String strProp;
    public final String strProp2 = "Default";

    /**
     * Get Str prop.
     * @return strProp
     */
    public String getStrProp() {
        return strProp;
    }

    public void setStrProp(@Nullable String strProp) {
        this.strProp = strProp;
    }

    private void hoge() {

    }

    public static void staticMethod() {

    }

    public T generic() {
        return null;
    }

    public static <T> void fromArrayToCollection(T[] a, Collection<T> c) {
        for (T o : a) {
            c.add(o); // Correct
        }
    }

    @Override
    public int intValue() {
        return 0;
    }

    @Override
    public long longValue() {
        return 0;
    }

    @Override
    public float floatValue() {
        return 0;
    }

    @Override
    public double doubleValue() {
        return 0;
    }


    @Override
    public int compareTo(Number o) {
        return 0;
    }

    public static class MyInnerrClass {
        public void myInner() {}
    }

}
