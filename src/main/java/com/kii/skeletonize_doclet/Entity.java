package com.kii.skeletonize_doclet;

import java.util.List;

public interface Entity extends Renderer {

    /** "class", "enum" or "interface" */
    String getClassifier();

    /** Name of the package to which the entity belongs */
    String getPackageName();

    /** Modifiers */
    String getModifiers();

    /** Non qualified name */
    String getName();

    /** Generic type owned by the entity */
    String getGenerics();

    /** Comment for the entity */
    String getComment();

    /** Interfaces conforms to */
    String getImplementsDeclaration();

    /** Extends */
    String getExtendsDeclaration();

    /** Methods */
    List<MethodSignature> getMethods();

    /** Fields */
    List<FieldSignature> getFields();

}
