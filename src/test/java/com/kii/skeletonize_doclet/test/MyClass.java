package com.kii.skeletonize_doclet.test;

import com.sun.istack.internal.Nullable;

/**
 * It's my class!
 * WEEEEI!
 */
public class MyClass {
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
}
