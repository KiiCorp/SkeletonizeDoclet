package com.kii.skeletonize_doclet;

import java.util.ArrayList;
import java.util.List;

public class ClassSignature {

    public ClassSignature() {
        this.methods = new ArrayList<>();
        this.fields = new ArrayList<>();
        this.innerClasses = new ArrayList<>();
    }

    /** package name of the class **/
    public String packageName;
    /** Non qualified class name */
    public String name;
    /** Generic types */
    public String classGenerics;
    /** Comment for the class */
    public String comment;
    /** Base  class */
    public String baseClass;
    /** Interfaces */
    public String interfaces;
    /** Methods */
    public List<MethodSignature> methods;
    /** Fields */
    public List<FieldSignature> fields;
    /** Inner classes */
    public List<ClassSignature> innerClasses;
}
