package com.kii.skeletonize_doclet;

public class MethodSignature {
    public MethodSignature(String methodDeclaration, String methodComment, String methodBody, String methodAnnotations) {
        this.methodDeclaration = methodDeclaration;
        this.methodComment = methodComment;
        this.methodBody = methodBody;
        this.methodAnnotations = methodAnnotations;
    }
    String methodAnnotations;
    String methodDeclaration;
    String methodComment;
    String methodBody;

    public String getComment(String indent) {
        return Converter.indentComment(indent, methodComment);
    }

}
