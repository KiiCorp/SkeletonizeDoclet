package com.kii.skeletonize_doclet;

public class MethodSignature {
    public MethodSignature(String methodDeclaration, String methodComment, String methodBody) {
        this.methodDeclaration = methodDeclaration;
        this.methodComment = methodComment;
        this.methodBody = methodBody;
    }
    String methodDeclaration;
    String methodComment;
    String methodBody;

    public String getComment(String indent) {
        return Converter.indentComment(indent, methodComment);
    }

}
