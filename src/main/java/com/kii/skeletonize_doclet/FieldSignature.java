package com.kii.skeletonize_doclet;

public class FieldSignature {
    public FieldSignature(String fieldDeclaration, String fieldComment) {
        this.fieldDeclaration = fieldDeclaration;
        this.fieldComment = fieldComment;
    }
    String fieldDeclaration;
    String fieldComment;

    public String getComment(String indent) {
        return Converter.indentComment(indent, fieldComment);
    }

}
