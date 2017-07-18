package com.kii.skeletonize_doclet.test;

public enum MyEnum {
    /** One */
    ONE("one"),
    /** Two */
    TWO("two");
    private String name;
    private MyEnum(String name) {
        this.name = name;
    }

}
